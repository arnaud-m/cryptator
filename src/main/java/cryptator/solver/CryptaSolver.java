/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import java.util.function.Consumer;

import org.chocosolver.solver.Solver;

import cryptator.choco.ChocoLogger;
import cryptator.config.CryptaConfig;
import cryptator.specs.ICryptaModeler;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;

public final class CryptaSolver extends AbstractCryptaSolver {

    private static final ChocoLogger CLOG = new ChocoLogger(LOGGER);

    private ICryptaModeler modeler;

    public CryptaSolver() {
        this(false);
    }

    public CryptaSolver(final boolean useBignum) {
        super();
        modeler = useBignum ? new CryptaBignumModeler() : new CryptaModeler();
    }

    public void setBignum() {
        modeler = new CryptaBignumModeler();
    }

    public void unsetBignum() {
        modeler = new CryptaModeler();
    }

    @Override
    public boolean solve(final ICryptaNode cryptarithm, final CryptaConfig config,
            final Consumer<ICryptaSolution> solutionConsumer) throws CryptaModelException {
        final CryptaModel m = modeler.model(cryptarithm, config);
        logOnCryptarithm(cryptarithm);
        logOnConfiguration(config);
        CLOG.logOnModel(m);

        final Solver s = m.getSolver();
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
        CLOG.logOnSolver(m);
        return solutionCount > 0;
    }

}
