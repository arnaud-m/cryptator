/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionHandlerFilter;

import cryptator.parser.CryptaParserException;
import cryptator.parser.CryptaParserWrapper;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolver;
import cryptator.solver.CryptaSolverException;
import cryptator.specs.ICryptaEvaluation;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ICryptaSolver;
import cryptator.tree.CryptaEvaluation;
import cryptator.tree.CryptaEvaluationException;
import cryptator.tree.GraphizExporter;

public class Cryptator {


	static {
		InputStream stream = Cryptator.class.getClassLoader().
				getResourceAsStream("logging.properties");
		try {
			LogManager.getLogManager().readConfiguration(stream);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static final Logger LOGGER = Logger.getLogger(Cryptator.class.getName());

	public Cryptator() {}

	public static void main(String[] args) {

		final CryptatorConfig config = parseOptions(args);
		if(config == null) return;

		configureLogging(config);

		if( config.getArguments().isEmpty()) {
			LOGGER.severe("Parse cryptarithm arguments [FAIL]");
			return;
		}

		final ICryptaSolver solver = buildSolver(config);

		final CryptaParserWrapper parser = new CryptaParserWrapper();

		final BiConsumer<ICryptaNode, ICryptaSolution> consumer = buildBiConsumer(config);

		for (String cryptarithm : config.getArguments()) {
			solve(cryptarithm, parser, solver, config, consumer);
		}
	}

	public static CryptatorConfig parseOptions(String[] args) {
		final CryptatorConfig config = new CryptatorConfig();
		final CmdLineParser parser = new CmdLineParser(config);
		try {
			// parse the arguments.
			parser.parseArgument(args);
			
			if(checkConfiguration(config)) {
				LOGGER.config("Parse options [OK]");
				return config;
			}
		} catch( CmdLineException e ) {
			System.err.println(e.getMessage());
		}

		// if there's a problem in the command line,
		// you'll get this exception. this will report
		// an error message.
		System.err.println("java Cryptator [options...] CRYPTARITHMS");
		// print the list of available options
		parser.printUsage(System.err);
		System.err.println();

		// print option sample. This is useful some time
		System.err.println("  Example: java Cryptator"+parser.printExample(OptionHandlerFilter.ALL));
		LOGGER.severe("Parse options [FAIL]");
		return null;
	}

	private final static void configureLogging(CryptatorConfig config) {
		if(config.isVerbose()) {
			LOGGER.setLevel(Level.FINE);
			CryptaSolver.LOGGER.setLevel(Level.FINE);
		}
	}

	private static boolean checkConfiguration(CryptatorConfig config) {
		if( config.getArguments().isEmpty()) LOGGER.severe("Parse cryptarithm arguments [FAIL]");
		else if(config.getArithmeticBase() < 2) LOGGER.severe("Invalid Arithmetic base option: less than 2");
		else if(config.getRelaxMinDigitOccurence() < 0 || config.getRelaxMaxDigitOccurence() < 0) LOGGER.severe("Digit occurences cannot be negative.");
		else return true;
		return false;
	}

	private final static ICryptaSolver buildSolver(CryptatorConfig config) {
		final ICryptaSolver solver = new CryptaSolver();
		solver.limitSolution(config.getSolutionLimit());
		solver.limitTime(config.getTimeLimit());
		return solver;
	}

	private static void solve(String cryptarithm, CryptaParserWrapper parser, ICryptaSolver solver , CryptatorConfig config, BiConsumer<ICryptaNode, ICryptaSolution> consumer) {
		try {
			final ICryptaNode node = parser.parse(cryptarithm);
			LOGGER.log(Level.INFO, "Parse cryptarithm {0} [OK]", cryptarithm);
			final String status = solver.solve(node, config, (s) -> {consumer.accept(node, s);}) ? "OK" : "KO";
			LOGGER.log(Level.INFO, "Solve cryptarithm {0} [{1}]", new Object[] {cryptarithm, status});
		} catch (CryptaParserException e) {
			LOGGER.log(Level.SEVERE, "Parse cryptarithm " + cryptarithm + " [FAIL]", e);
		} catch (CryptaModelException e) {
			LOGGER.log(Level.SEVERE, "Model cryptarithm [FAIL]", e);
		} catch (CryptaSolverException e) {
			LOGGER.log(Level.SEVERE, "Solve cryptarithm [FAIL]", e);
		}
	}

	private static class DefaultConsumer implements BiConsumer<ICryptaNode, ICryptaSolution> {


		@Override
		public void accept(ICryptaNode n, ICryptaSolution s) {
			LOGGER.log(Level.INFO, "Solution found.\n{0}", s);
		}

	}

	private static class CheckConsumer implements BiConsumer<ICryptaNode, ICryptaSolution> {

		public final int base;

		public final ICryptaEvaluation eval = new CryptaEvaluation();

		public CheckConsumer(int base) {
			super();
			this.base = base;
		}

		@Override
		public void accept(ICryptaNode n, ICryptaSolution s) {
			try {
				if(eval.evaluate(n, s, base) != 0) LOGGER.config("Eval cryptarithm solution [OK]"); 
				else  LOGGER.warning("Eval cryptarithm solution [KO]"); 
			} catch (CryptaEvaluationException e) {
				LOGGER.log(Level.WARNING, "Eval cryptarithm solution [FAIL]", e);
			}		
		}

	}

	private static class GraphvizConsumer implements BiConsumer<ICryptaNode, ICryptaSolution> {

		public final GraphizExporter graphviz= new GraphizExporter();

		@Override
		public void accept(ICryptaNode n, ICryptaSolution s) {
			graphviz.print(n, s, System.out);
			LOGGER.config("Export cryptarithm solution [OK]");
		}

	}

	public static BiConsumer<ICryptaNode, ICryptaSolution> buildBiConsumer(final CryptatorConfig config) {
		BiConsumer<ICryptaNode, ICryptaSolution> consumer = new DefaultConsumer();

		if(config.isCheckSolution()) {
			consumer = consumer.andThen( new CheckConsumer(config.getArithmeticBase()));
		}
		if(config.isExportGraphiz()) {
			consumer = consumer.andThen(new GraphvizConsumer());
		}
		return consumer;
	}


}
