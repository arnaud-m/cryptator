/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.choco;

import java.util.Formatter;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;

import cryptator.specs.IChocoModel;

public final class ChocoLogger {

    private final Logger logger;

    public ChocoLogger(Logger logger) {
        super();
        this.logger = logger;
    }

    public void logOnModel(final IChocoModel m) {
        logOnModel(m.getModel());
    }

    public void logOnModel(final Model model) {
        if (logger.isLoggable(Level.CONFIG)) {
            logger.log(Level.CONFIG, "Model diagnostics:\n{0}", toDimacs(model));
            logger.log(Level.FINE, "Pretty model:{0}", model);
        }
    }

    public void logOnSolution(final Solution solution) {
        if (logger.isLoggable(Level.FINER)) {
            solution.record();
            logger.log(Level.FINER, "Solver solution:\n{0}", solution);
        }
    }

    public void logOnSolution(final IChocoModel m) {
        logOnSolution(m.getModel());
    }

    public void logOnSolution(final Model model) {
        logOnSolution(new Solution(model));
    }

    public void logOnSolver(final IChocoModel m) {
        logOnSolver(m.getModel());
    }

    public void logOnSolver(Model model) {
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, "Solver diagnostics:\n{0}", toDimacs(model.getSolver()));
        }
    }

    public static String toDimacs(Model model) {
        final StringBuilder b = new StringBuilder();
        Formatter fmt = new Formatter(b, Locale.US);
        fmt.format("c MODEL_NAME %s", model.getName());
        fmt.format("%nd VARIABLES %d", model.getNbVars());
        fmt.format("%nd CONSTRAINTS %d", model.getNbCstrs());
        fmt.format("%nd BOOL_VARS %d", model.getNbBoolVar());
        fmt.format("%nd INT_VARS %d", model.getNbIntVar(false));
        fmt.close();
        return b.toString();
    }

    public static String toDimacs(final Solver s) {
        final StringBuilder b = new StringBuilder(256);
        Formatter fmt = new Formatter(b, Locale.US);
        fmt.format("s %s", s.getSearchState());
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
}
