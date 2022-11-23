/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import java.util.Formatter;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;

import cryptator.config.CryptaConfig;
import cryptator.specs.ICryptaModeler;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ICryptaSolver;

public final class CryptaSolver implements ICryptaSolver {

    private static final int MS = 1000;

    public static final Logger LOGGER = Logger.getLogger(CryptaSolver.class.getName());

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

    private void logOnSolution(final CryptaModel m) {
        if (LOGGER.isLoggable(Level.CONFIG)) {
            final Solution sol = new Solution(m.getModel());
            sol.record();
            LOGGER.log(Level.CONFIG, "Display internal solver solution.\n{0}", sol);
        }
    }

    @Override
    public boolean solve(final ICryptaNode cryptarithm, final CryptaConfig config,
            final Consumer<ICryptaSolution> solutionConsumer) throws CryptaModelException {
        final CryptaModel m = modeler.model(cryptarithm, config);

        LOGGER.log(Level.INFO, "Model diagnostics:{0}", toDimacs(m.getModel()));
        LOGGER.log(Level.CONFIG, "Printed model:{0}", m.getModel());

        final Solver s = m.getModel().getSolver();
        if (timeLimit > 0) {
            s.limitTime(timeLimit * MS); // in ms
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
        LOGGER.log(Level.INFO, "Solver diagnostics:{0}", toDimacs(s));
        return solutionCount > 0;
    }

    public static String toDimacs(final Solver s) {
        final StringBuilder b = new StringBuilder(256);
        Formatter fmt = new Formatter(b, Locale.US);
        fmt.format("%ns %s", s.getSearchState());
        if (s.hasObjective()) {
            fmt.format("%no %3.f", s.getBoundsManager().getBestSolutionValue());
        }
        fmt.format(
                "%nd NBSOLS %d%nd TIME %.3f%nd NODES %d%nd BACKTRACKS %d%nd BACKJUMPS %d%nd FAILURES %d%nd RESTARTS %d",
                s.getSolutionCount(), s.getTimeCount(), s.getNodeCount(), s.getBackTrackCount(), s.getBackjumpCount(),
                s.getFailCount(), s.getRestartCount());
        fmt.close();
        return b.toString();
    }

    public static String toDimacs(Model model) {
        final StringBuilder b = new StringBuilder();
        Formatter fmt = new Formatter(b, Locale.US);
        fmt.format("%nc MODEL_NAME %s", model.getName());
        fmt.format("%nd VARIABLES %d", model.getNbVars());
        fmt.format("%nd CONSTRAINTS %d", model.getNbCstrs());
        fmt.format("%nd BOOL_VARS %d", model.getNbBoolVar());
        fmt.format("%nd INT_VARS %d", model.getNbIntVar(false));
        fmt.close();
        return b.toString();
    }

}
