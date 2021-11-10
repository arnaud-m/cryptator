/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import static cryptator.tree.TreeUtils.writePostorder;

import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import cryptator.cmd.CryptaBiConsumer;
import cryptator.cmd.OptionsParser;
import cryptator.config.CryptatorConfig;
import cryptator.parser.CryptaParserException;
import cryptator.parser.CryptaParserWrapper;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolver;
import cryptator.solver.CryptaSolverException;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolver;
import cryptator.tree.TreeUtils;

public class Cryptator {

	public static final Logger LOGGER = Logger.getLogger(Cryptator.class.getName());

	private Cryptator() {}


	public static void main(String[] args) {
		JULogUtil.configureLoggers();

		CryptatorOptionsParser optparser = new CryptatorOptionsParser();
		if ( !optparser.parseOptions(args)) return;
		final CryptatorConfig config = optparser.getConfig();

		final ICryptaSolver solver = buildSolver(config);

		final CryptaParserWrapper parser = new CryptaParserWrapper();
		
		int exitStatus = 0;
		for (String cryptarithm : config.getArguments()) {
			exitStatus += solve(cryptarithm, parser, solver, config);
		}
		JULogUtil.flushLogs();
		System.exit(exitStatus);
	}

	private static class CryptatorOptionsParser extends OptionsParser<CryptatorConfig> {

		public CryptatorOptionsParser() {
			super(Cryptator.class, new CryptatorConfig());
		}

		@Override
		protected void configureLoggers() {
			super.configureLoggers();
			if(config.isVerbose()) {
				JULogUtil.setLevel(Level.CONFIG, getLogger(), CryptaSolver.LOGGER);
			}
		}	
	}

	private static final ICryptaSolver buildSolver(CryptatorConfig config) {
		final CryptaSolver solver = new CryptaSolver(config.useBignum());
		solver.limitSolution(config.getSolutionLimit());
		solver.limitTime(config.getTimeLimit());
		return solver;
	}

	public static ICryptaNode parseCryptarithm(String cryptarithm, CryptaParserWrapper parser, Logger logger) throws CryptaParserException  {
		final ICryptaNode node = parser.parse(cryptarithm);
		logger.log(Level.INFO, "Parse cryptarithm {0} [OK]", cryptarithm);
		if(logger.isLoggable(Level.CONFIG)) {
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			writePostorder(node, os);
			logger.log(Level.CONFIG, "Display postorder internal cryptarithm:\n{0}", os);
		}
		return node;

	}

	private static long solve(String cryptarithm, CryptaParserWrapper parser, ICryptaSolver solver , CryptatorConfig config) {
		try {
			final ICryptaNode node = parseCryptarithm(cryptarithm, parser, LOGGER);

			if(LOGGER.isLoggable(Level.INFO)) {
				LOGGER.log(Level.INFO, "Cryptarithm features:\n{0}", TreeUtils.computeFeatures(node));
			}
			
			final CryptaBiConsumer consumer = buildBiConsumer(config);
			final boolean solved = solver.solve(node, config, s -> consumer.accept(node, s)) ;
			String status = "ERROR";
			if(consumer.getErrorCount() == 0) {
				status = solved ? "OK" : "KO";
			}
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
		if(config.isCheckSolution()) consumer.withSolutionCheck(config.getArithmeticBase());
		if(config.isExportGraphiz()) consumer.withGraphvizExport();
		return consumer;
	}
	

}
