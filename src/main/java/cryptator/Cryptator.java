/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.util.function.BiConsumer;
import java.util.logging.Level;
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

	private static final Logger LOGGER = Logger.getLogger(Cryptator.class.getName());
	
	public Cryptator() {}

	public static void main(String[] args) {

		final CryptatorConfig config = parseOptions(args);
		if(config == null) return;

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
			LOGGER.info("Parse options [OK]");

			// you can parse additional arguments if you want.
			// parser.parseArgument("more","args");

			//			// after parsing arguments, you should check
			//			// if enough arguments are given.
			//			if( ! config.getArguments().isEmpty()) {
			//				return config;
			//			} else LOGGER.log(Level.WARNINGSEVERE, "Parse options [OK]");
			return config;
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
		LOGGER.info("Parse options [FAIL]");
		return null;

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
			solver.solve(node, config, (s) -> {consumer.accept(node, s);});
			LOGGER.log(Level.INFO, "Solve cryptarithm {0} [OK]", cryptarithm);
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
			LOGGER.info(s.toString());
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
			String check = "ERROR";
			try {
				check = eval.evaluate(n, s, base) != 0 ? "OK" : "FAIL";
			} catch (CryptaEvaluationException e) {
				LOGGER.log(Level.WARNING, "Eval cryptarithm exception thrown", e);
			}
			LOGGER.log(Level.INFO, "Eval cryptarithm solution [{0}]", check);		
		}

	}

	private static class GraphvizConsumer implements BiConsumer<ICryptaNode, ICryptaSolution> {

		public final GraphizExporter graphviz= new GraphizExporter();

		@Override
		public void accept(ICryptaNode n, ICryptaSolution s) {
			graphviz.print(n, s, System.out);
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
