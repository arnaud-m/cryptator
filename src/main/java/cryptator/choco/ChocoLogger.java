/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.choco;

import java.util.Formatter;
import java.util.Locale;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.slf4j.Logger;

import cryptator.JULogUtil.LoggerType;
import cryptator.specs.IChocoModel;

public final class ChocoLogger {

    private final Logger logger;

    public ChocoLogger(final LoggerType loggerType) {
        super();
        this.logger = loggerType.getLogger(ChocoLogger.class);
    }

    public void logOnModel(final IChocoModel m) {
        logOnModel(m.getModel());
    }

    public void logOnModel(final Model model) {
    	logger.atDebug().setMessage("Model diagnostics:\n{}").addArgument( () -> toDimacs(model)).log();
        logger.trace("Pretty model:{}", model);      
    }

    public void logOnSolution(final Solution solution) {
       logger.atTrace().setMessage("Solver solution:\n{}").addArgument(() -> {solution.record();return solution;}).log();
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

    public void logOnSolver(final Model model) {
    	logger.atInfo().setMessage("Solver diagnostics:\n{}").addArgument( () -> toDimacs(model.getSolver())).log();
    }

    public static String toDimacs(final Model model) {
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
