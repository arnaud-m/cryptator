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

import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;

import cryptator.config.CryptaConfig;
import cryptator.specs.ICryptaModeler;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ICryptaSolver;

public final class CryptaSolver implements ICryptaSolver {

    public static final Logger LOGGER = Logger.getLogger(CryptaSolver.class.getName());

    private ICryptaModeler modeler;

    private long timeLimit = 0;

    private long solutionLimit = 0;

    public CryptaSolver() {
        this(false);
    }

    public CryptaSolver(boolean useBignum) {
        super();
        modeler = useBignum ? new CryptaBignumModeler() : new CryptaModeler();
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    @Override
    public void limitTime(long limit) {
        this.timeLimit = limit;
    }

    public long getSolutionLimit() {
        return solutionLimit;
    }

    @Override
    public void limitSolution(long limit) {
        this.solutionLimit = limit;
    }

    public void setBignum() {
        modeler = new CryptaBignumModeler();
    }

    public void unsetBignum() {
        modeler = new CryptaModeler();
    }

    private void logOnSolution(CryptaModel m) {
        if (LOGGER.isLoggable(Level.CONFIG)) {
            final Solution sol = new Solution(m.getModel());
            sol.record();
            LOGGER.log(Level.CONFIG, "Display internal solver solution.\n{0}", sol);
        }
    }

    @Override
    public boolean solve(ICryptaNode cryptarithm, CryptaConfig config, Consumer<ICryptaSolution> solutionConsumer)
            throws CryptaModelException {
        final CryptaModel m = modeler.model(cryptarithm, config);
        LOGGER.log(Level.CONFIG, "Display model{0}", m.getModel());
        final Solver s = m.getModel().getSolver();
        if (timeLimit > 0) {
            s.limitTime(timeLimit * 1000); // in ms
        }
        int solutionCount = 0;
        if (solutionLimit > 0) {
            while ((solutionCount < solutionLimit) && s.solve()) {
                logOnSolution(m);
                solutionConsumer.accept(m.recordSolution());
                solutionCount++;
            }
        } else {
            while (s.solve()) {
                logOnSolution(m);
                solutionConsumer.accept(m.recordSolution());
                solutionCount++;
            }
        }
        LOGGER.log(Level.INFO, "{0}", s.getMeasures());
        return solutionCount > 0;
    }

}
