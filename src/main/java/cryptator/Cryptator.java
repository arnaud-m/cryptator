/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.util.OptionalInt;
import java.util.logging.Level;
import java.util.logging.Logger;

import cryptator.cmd.CryptaBiConsumer;
import cryptator.cmd.OptionsParserWithLog;
import cryptator.config.CryptaCmdConfig;
import cryptator.config.CryptatorConfig;
import cryptator.parser.CryptaParserException;
import cryptator.parser.CryptaParserWrapper;
import cryptator.solver.AdaptiveSolver;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolver;
import cryptator.solver.CryptaSolverException;
import cryptator.solver.crypt.CryptSolver;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolver;

public final class Cryptator {

    public static final Logger LOGGER = Logger.getLogger(Cryptator.class.getName());

    private Cryptator() {
    }

    public static void main(final String[] args) {
        JULogUtil.configureDefaultLoggers();
        final int exitCode = doMain(args);
        System.exit(exitCode);
    }

    public static int doMain(final String[] args) {
        try {
            final CryptatorOptionsParser optparser = new CryptatorOptionsParser();
            final OptionalInt parserExitCode = optparser.parseOptions(args);
            if (parserExitCode.isPresent()) {
                return parserExitCode.getAsInt();
            }
            final CryptatorConfig config = optparser.getConfig();

            final ICryptaSolver solver = buildSolver(config);

            final CryptaParserWrapper parser = new CryptaParserWrapper();

            int exitCode = 0;
            for (String cryptarithm : config.getArguments()) {
                exitCode += solve(cryptarithm, parser, solver, config);
            }
            return exitCode;
        } finally {
            JULogUtil.flushLogs();
        }
    }

    private static class CryptatorOptionsParser extends OptionsParserWithLog<CryptatorConfig> {

        private static final String ARG_NAME = "CRYPTARITHMS...";

        CryptatorOptionsParser() {
            super(Cryptator.class, new CryptatorConfig(), ARG_NAME, JULogUtil.getDefaultLogManager());
        }
    }

    public static ICryptaSolver createSolver(final CryptaCmdConfig config) {
        switch (config.getSolverType()) {
        case SCALAR:
            return new CryptaSolver(false);
        case BIGNUM:
            return new CryptaSolver(true);
        case CRYPT:
            return new CryptSolver();
        case ADAPT:
            return new AdaptiveSolver(false);
        case ADAPTC:
            return new AdaptiveSolver(true);
        default:
            return new CryptaSolver(false);
        }
    }

    private static ICryptaSolver buildSolver(final CryptatorConfig config) {
        final ICryptaSolver solver = createSolver(config);
        solver.limitSolution(config.getSolutionLimit());
        solver.limitTime(config.getTimeLimit());
        return solver;
    }

    public static ICryptaNode parseCryptarithm(final String cryptarithm, final CryptaParserWrapper parser,
            final Logger logger) throws CryptaParserException {
        final ICryptaNode node = parser.parse(cryptarithm);
        logger.log(Level.INFO, "Parse cryptarithm [OK]\n{0}", cryptarithm);
        return node;

    }

    private static long solve(final String cryptarithm, final CryptaParserWrapper parser, final ICryptaSolver solver,
            final CryptatorConfig config) {
        try {
            final ICryptaNode node = parseCryptarithm(cryptarithm, parser, LOGGER);

            final CryptaBiConsumer consumer = buildBiConsumer(config);
            final boolean solved = solver.solve(node, config, consumer);
            String status = "ERROR";
            if (consumer.getErrorCount() == 0) {
                status = solved ? "OK" : "KO";
            }
            consumer.logOnLastSolution();
            LOGGER.log(Level.INFO, "Solve cryptarithm {0} [{1}]", new Object[] {cryptarithm, status});
            return consumer.getErrorCount();
        } catch (CryptaParserException e) {
            LOGGER.log(Level.SEVERE, e, () -> "Parse cryptarithm " + cryptarithm + " [FAIL]");
        } catch (CryptaModelException e) {
            LOGGER.log(Level.SEVERE, "Model cryptarithm [FAIL]", e);
        } catch (CryptaSolverException e) {
            LOGGER.log(Level.SEVERE, "Solve cryptarithm [FAIL]", e);
        }
        return 1;
    }

    private static CryptaBiConsumer buildBiConsumer(final CryptatorConfig config) {
        CryptaBiConsumer consumer = new CryptaBiConsumer(LOGGER);
        consumer.withSolutionLog();
        if (config.isCheckSolution()) {
            consumer.withSolutionCheck(config.getArithmeticBase());
        }
        if (config.isExportGraphiz()) {
            consumer.withGraphvizExport();
        }
        return consumer;
    }

}
