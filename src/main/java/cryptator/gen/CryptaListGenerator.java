/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;

import cryptator.choco.ChocoLogger;
import cryptator.cmd.CryptaBiConsumer;
import cryptator.cmd.WordArray;
import cryptator.config.CryptagenConfig;
import cryptator.solver.AdaptiveSolver;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolver;
import cryptator.solver.CryptaSolverException;
import cryptator.specs.IChocoModel;
import cryptator.specs.ICryptaGenSolver;
import cryptator.specs.ICryptaGenerator;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ICryptaSolver;
import cryptator.tree.TreeUtils;

public class CryptaListGenerator implements ICryptaGenerator {

    private final WordArray words;

    private final CryptagenConfig config;

    private final Logger logger;

    private final ChocoLogger clog;

    private final AtomicInteger errorCount;

    public CryptaListGenerator(final WordArray words, final CryptagenConfig config, final Logger logger) {
        super();
        this.words = words;
        this.config = config;
        this.logger = logger;
        this.clog = new ChocoLogger(logger);
        this.errorCount = new AtomicInteger();
    }

    public final int getErrorCount() {
        return errorCount.intValue();
    }

    private AbstractCryptaListModel createGenModel() {
        if (config.getGridSize() > 0) {
            return new CryptaGenCrossword(config.getGridSize(), words.getWords(), config.isLightModel());
        } else if (config.isMultModel()) {
            return new CryptaGenMult(words.getWords());
        } else if (config.isLongMultModel()) {
            return new CryptaGenLongMult(words.getWords(), config.getArithmeticBase());
        } else {
            return new CryptaGenAdd(words.getWords(), config.isLightModel());
        }
    }

    private ICryptaGenSolver buildGenSolver() {
        final AbstractCryptaListModel gen = createGenModel();
        gen.buildModel();
        gen.postWordCountConstraints(Math.max(config.getMinLeftOperands(), 2) + 1, config.getMaxLeftOperands() + 1);
        gen.postMaxSymbolCountConstraint(config.getArithmeticBase());
        gen.postPrecisionConstraints(config.getArithmeticBase());

        if (!config.isLightPropagation()) {
            gen.postHeavyConstraints(config.getArithmeticBase());
        }
        if (words.hasRightMember()) {
            gen.postFixedRightMemberConstraints();
        }
        if (words.isDoublyTrue()) {
            gen.postDoublyTrueConstraints(words.getLB());
        }
        return gen;
    }

    private Consumer<ICryptaNode> buildConsumer(final IChocoModel gen,
            final BiConsumer<ICryptaNode, ICryptaSolution> consumer) {
        final Consumer<ICryptaNode> cons = new LogConsumer(gen);
        final ICryptaSolver solver = config.useBignum() ? new CryptaSolver(true) : new AdaptiveSolver();
        return config.isDryRun() ? cons : cons.andThen(new GenerateConsumer(solver, consumer));
    }

    private void sequentialSolve(final ICryptaGenSolver gen, final Consumer<ICryptaNode> cons) {
        final Solver s = gen.getSolver();
        while (s.solve()) {
            cons.accept(gen.recordCryptarithm());
        }
    }

    private static void parallelSolve(final ICryptaGenSolver gen, final Consumer<ICryptaNode> cons,
            final int nthreads) {
        final ExecutorService executor = Executors.newFixedThreadPool(nthreads);
        final Solver s = gen.getSolver();
        while (s.solve()) {
            final ICryptaNode cryptarithm = gen.recordCryptarithm();
            executor.execute(() -> cons.accept(cryptarithm));
        }
        try {
            if (!executor.awaitTermination(10000, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public long generate(final BiConsumer<ICryptaNode, ICryptaSolution> consumer) throws CryptaModelException {
        final ICryptaGenSolver gen = buildGenSolver();
        clog.logOnModel(gen);

        final Consumer<ICryptaNode> cons = buildConsumer(gen, consumer);

        final int nthreads = config.getNthreads();
        if (nthreads == 1) {
            sequentialSolve(gen, cons);
        } else {
            parallelSolve(gen, cons, nthreads);
        }
        clog.logOnSolver(gen);
        return gen.getSolver().getSolutionCount();
    }

    // FIXME are consumers thread-safe ? they are used in parallel !
    private class LogConsumer implements Consumer<ICryptaNode> {

        private final Solution solution;

        LogConsumer(final IChocoModel gen) {
            super();
            solution = new Solution(gen.getModel());
        }

        @Override
        public void accept(final ICryptaNode t) {
            clog.logOnSolution(solution);
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "Candidate cryptarithm:\n{0}", TreeUtils.writeInorder(t));
            }
        }
    }

    private class GenerateConsumer implements Consumer<ICryptaNode> {

        private final ICryptaSolver solver;

        private final BiConsumer<ICryptaNode, ICryptaSolution> internal;

        GenerateConsumer(final ICryptaSolver solver, final BiConsumer<ICryptaNode, ICryptaSolution> internal) {
            super();
            this.solver = solver;
            this.internal = internal;
            this.solver.limitSolution(2);
        }

        private CryptaBiConsumer buildBiConsumer() {
            CryptaBiConsumer consumer = new CryptaBiConsumer(logger);
            if (config.isCheckSolution()) {
                consumer.withSolutionCheck(config.getArithmeticBase());
            }
            return consumer;
        }

        @Override
        public void accept(final ICryptaNode t) {
            try {
                final CryptaBiConsumer collect = buildBiConsumer();
                solver.solve(t, config, collect);
                if (collect.getErrorCount() == 0) {
                    final Optional<ICryptaSolution> solution = collect.getUniqueSolution();
                    if (solution.isPresent()) {
                        internal.accept(t, solution.get());
                    }
                } else {
                    logger.log(Level.WARNING, "Solve the candidate cryptarithm [ERROR]");
                }
            } catch (CryptaModelException | CryptaSolverException e) {
                errorCount.incrementAndGet();
                logger.log(Level.WARNING, "Solve the candidate cryptarithm [FAIL]", e);
            }
        }
    }

}
