/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.util.OptionalInt;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import cryptator.JULogUtil.LoggerType;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(Cryptator.class);
    
    private Cryptator() {
    }

    public static void main(final String[] args) {
        JULogUtil.configureDefaultLoggers();
        
//        LOGGER.trace("TRACE test");
//        LOGGER.debug("DEBUG test");
//        LOGGER.info("INFO test {}", 1);
//        LOGGER.warn("WARN test");
//        LOGGER.error("ERROR test");
        
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
            super(new CryptatorConfig(), Cryptator.class.getName(), ARG_NAME, JULogUtil.getCryptatorLogManager());
        }
    }

    public static ICryptaSolver createSolver(final CryptaCmdConfig config, final LoggerType loggerType) {
        switch (config.getSolverType()) {
        case SCALAR:
            return new CryptaSolver(false, loggerType);
        case BIGNUM:
            return new CryptaSolver(true, loggerType);
        case CRYPT:
        	// TODO Adapt to logger type ? 
            return new CryptSolver();
        case ADAPT:
            return new AdaptiveSolver(false, loggerType);
        case ADAPTC:
            return new AdaptiveSolver(true, loggerType);
        default:
            return new CryptaSolver(false, loggerType);
        }
    }

    private static ICryptaSolver buildSolver(final CryptatorConfig config) {
        final ICryptaSolver solver = createSolver(config, LoggerType.PRIMARY);
        solver.limitSolution(config.getSolutionLimit());
        solver.limitTime(config.getTimeLimit());
        return solver;
    }

    public static ICryptaNode parseCryptarithm(final String cryptarithm, final CryptaParserWrapper parser,
            final Logger logger) throws CryptaParserException {
        final ICryptaNode node = parser.parse(cryptarithm);
        logger.info("Parse cryptarithm [OK]\n{}", cryptarithm);
        return node;

    }

    private static long solve(final String cryptarithm, final CryptaParserWrapper parser, final ICryptaSolver solver,
            final CryptatorConfig config) {
        try {
            final ICryptaNode node = parseCryptarithm(cryptarithm, parser, LOGGER);

            final CryptaBiConsumer consumer = buildBiConsumer(config);
            final boolean solved = solver.solve(node, config, consumer).isFeasible();
            String status = "ERROR";
            if (consumer.getErrorCount() == 0) {
                status = solved ? "OK" : "KO";
            }
            consumer.logOnLastSolution();
            LOGGER.info("Solve cryptarithm {} [{}]", cryptarithm, status);
            return consumer.getErrorCount();
        } catch (CryptaParserException e) {
        	LOGGER.error("Parse cryptarithm {} [FAIL]", cryptarithm, e);
        } catch (CryptaModelException e) {
            LOGGER.error( "Model cryptarithm [FAIL]", e);
        } catch (CryptaSolverException e) {
            LOGGER.error("Solve cryptarithm [FAIL]", e);
        }
        return 1;
    }

    private static CryptaBiConsumer buildBiConsumer(final CryptatorConfig config) {
    	CryptaBiConsumer consumer = new CryptaBiConsumer(LoggerType.PRIMARY);
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
