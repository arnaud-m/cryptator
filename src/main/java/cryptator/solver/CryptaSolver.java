/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.chocosolver.solver.Solver;

import cryptator.choco.ChocoLogger;
import cryptator.config.CryptaConfig;
import cryptator.specs.ICryptaModeler;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ICryptaSolver;
import cryptator.tree.CryptaFeatures;
import cryptator.tree.TreeUtils;

public final class CryptaSolver implements ICryptaSolver {

    private static final int MS = 1000;

    public static final Logger LOGGER = Logger.getLogger(CryptaSolver.class.getName());

    private static final ChocoLogger CLOG = new ChocoLogger(LOGGER);

    private ICryptaModeler modeler;

    private long timeLimit = 0;

    private long solutionLimit = 0;

    public CryptaSolver() {
        this(false);
    }

    public CryptaSolver(final boolean useBignum) {
        super();
        modeler = useBignum ? new CryptaBignumModeler() : new CryptaModeler();
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    @Override
    public void limitTime(final long limit) {
        this.timeLimit = limit;
    }

    public long getSolutionLimit() {
        return solutionLimit;
    }

    @Override
    public void limitSolution(final long limit) {
        this.solutionLimit = limit;
    }

    public void setBignum() {
        modeler = new CryptaBignumModeler();
    }

    public void unsetBignum() {
        modeler = new CryptaModeler();
    }

    private static void logOnCryptarithm(final ICryptaNode cryptarithm) {
        if (LOGGER.isLoggable(Level.CONFIG)) {
            final CryptaFeatures feat = TreeUtils.computeFeatures(cryptarithm);
            LOGGER.log(Level.CONFIG, "Declare instance:\ni {0}\nc POST_ORDER {1}",
                    new Object[] {feat.buildInstanceName(), TreeUtils.writePostorder(cryptarithm)});
            LOGGER.log(Level.CONFIG, "Cryptarithm features:\n{0}", feat);
        }
    }

    private static void logOnConfiguration(final CryptaConfig config) {
        LOGGER.log(Level.CONFIG, "Configuration:\n{0}", config);
    }

    @Override
    public boolean solve(final ICryptaNode cryptarithm, final CryptaConfig config,
            final Consumer<ICryptaSolution> solutionConsumer) throws CryptaModelException {
        final CryptaModel m = modeler.model(cryptarithm, config);
        logOnCryptarithm(cryptarithm);
        logOnConfiguration(config);
        CLOG.logOnModel(m.getModel());

        final Solver s = m.getModel().getSolver();
        if (timeLimit > 0) {
            s.limitTime(timeLimit * MS); // in ms
        }
        int solutionCount = 0;
        if (solutionLimit > 0) {
            while ((solutionCount < solutionLimit) && s.solve()) {
                CLOG.logOnSolution(m.getModel());
                solutionConsumer.accept(m.recordSolution());
                solutionCount++;
            }
        } else {
            while (s.solve()) {
                CLOG.logOnSolution(m.getModel());
                solutionConsumer.accept(m.recordSolution());
                solutionCount++;
            }
        }
        CLOG.logOnSolver(m.getModel());
        return solutionCount > 0;
    }

}
