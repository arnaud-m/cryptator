/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen;

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

import cryptator.cmd.WordArray;
import cryptator.config.CryptagenConfig;
import cryptator.solver.AdaptiveSolver;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolverException;
import cryptator.specs.ICryptaGenerator;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ICryptaSolver;
import cryptator.tree.TreeUtils;

public class CryptaListGenerator implements ICryptaGenerator {

	private WordArray words;

	private CryptagenConfig config;

	private Logger logger;
	
	private AtomicInteger errorCount;
	
	public CryptaListGenerator(WordArray words, CryptagenConfig config, Logger logger) {
		super();
		this.words = words;
		this.config = config;
		this.logger = logger;
		this.errorCount = new AtomicInteger();
	}
	
	
	public final int getErrorCount() {
		return errorCount.intValue();
	}

	private CryptaGenModel buildModel() {
		final CryptaGenModel gen = new CryptaGenModel(words.getWords());
		gen.postMemberCardConstraints(config.getMinLeftOperands(), config.getMaxLeftOperands());
		gen.postMaxDigitCountConstraint(config.getArithmeticBase());
		gen.postLeftMinCardConstraints(config.getArithmeticBase());
		if(words.hasRightMember()) gen.postRigtMemberConstraint();
		if(words.isDoublyTrue()) gen.postDoublyTrueConstraint(words.getLB());
		return gen;
	}
	
	private Consumer<ICryptaNode> buildConsumer(CryptaGenModel gen, BiConsumer<ICryptaNode, ICryptaSolution> consumer) {
		final Consumer<ICryptaNode> cons = new LogConsumer(gen);
		return config.isDryRun() ? cons : cons.andThen(new GenerateConsumer(new AdaptiveSolver(), consumer));
	}
	
	private static void sequentialSolve(CryptaGenModel gen, Consumer<ICryptaNode> cons) {
		final Solver s = gen.getModel().getSolver();
		while(s.solve()) {	
			cons.accept(gen.recordCryptarithm());
		}
	}
	
	private static void parallelSolve(final CryptaGenModel gen, final Consumer<ICryptaNode> cons, final int nthreads) {
		final ExecutorService executor = Executors.newFixedThreadPool(nthreads);
		final Solver s = gen.getModel().getSolver();
		while(s.solve()) {
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
	public void generate(BiConsumer<ICryptaNode, ICryptaSolution> consumer) throws CryptaModelException {
		final CryptaGenModel gen = buildModel();
		logger.log(Level.FINE, "Display model{0}", gen.getModel());
		
		final Consumer<ICryptaNode> cons = buildConsumer(gen, consumer);
		
		final int nthreads = config.getNthreads();
		if(nthreads == 1) sequentialSolve(gen, cons);
		else parallelSolve(gen, cons, nthreads);
		
		if(logger.isLoggable(Level.FINE)) logger.log(Level.FINE, "{0}", gen.getModel().getSolver().getMeasures());
	}

	// FIXME are consumers thread-safe ? they are used in parallel !
	private class LogConsumer implements Consumer<ICryptaNode> {
		
		private final Solution solution;
		
				
		public LogConsumer(CryptaGenModel gen) {
			super();
			solution = new Solution(gen.getModel());
		}

		@Override
		public void accept(ICryptaNode t) {
			if(logger.isLoggable(Level.CONFIG)) {
				logger.log(Level.CONFIG, "candidate: {0}", TreeUtils.writeInorder(t));
				if(logger.isLoggable(Level.FINE)) {
					solution.record();
					logger.log(Level.FINE, "candidate from solver:\n{0}", solution);
				}
			}
		}

	}

	
	private static class SolutionCollect implements Consumer<ICryptaSolution> {

		private int solutionCount;

		private ICryptaSolution solution;

		@Override
		public void accept(ICryptaSolution u) {
			solutionCount++;
			this.solution = u;
		}	

		public final boolean hasUniqueSolution() {
			return solutionCount == 1;
		}

		public final ICryptaSolution getSolution() {
			return solution;
		}
	}

	private class GenerateConsumer implements Consumer<ICryptaNode> {

		private final ICryptaSolver solver;

		private final BiConsumer<ICryptaNode, ICryptaSolution> internal;

		public GenerateConsumer(ICryptaSolver solver, BiConsumer<ICryptaNode, ICryptaSolution> internal) {
			super();
			this.solver = solver;
			this.internal = internal;
			this.solver.limitSolution(2);
		}

		@Override
		public void accept(ICryptaNode t) {
			try {
				final SolutionCollect collect = new SolutionCollect();
				solver.solve(t, config, collect);
				if(collect.hasUniqueSolution()) {
					internal.accept(t, collect.getSolution());
				}
			} catch (CryptaModelException|CryptaSolverException e) {
				errorCount.incrementAndGet();
				logger.log(Level.WARNING, "failed to solve the cryptarithm", e);
			}
		}
	}

}
