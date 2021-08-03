/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionHandlerFilter;

import cryptator.parser.CryptaParserWrapper;
import cryptator.solver.CryptaModel2;
import cryptator.solver.CryptaSolver;
import cryptator.specs.ICryptaNode;
import cryptator.tree.GraphizExporter;
import org.chocosolver.solver.Solver;

import static cryptator.solver.SolverUtils.contraint;

public class Cryptator {

	private static final Logger LOGGER = Logger.getLogger(Cryptator.class.getName());

	public Cryptator() {}

	public static void main(String[] args) throws Exception {
		
		final CryptatorConfig config = parseOptions(args);
		if(config == null) {
			LOGGER.log(Level.SEVERE, "Parse options [FAIL]");
			return;
		} else {
			LOGGER.log(Level.INFO, "Parse options [OK]");
		}
		
		final CryptaParserWrapper parser = new CryptaParserWrapper();

		final GraphizExporter graphviz = new GraphizExporter();

		final CryptaSolver solver = new CryptaSolver();
		solver.limitSolution(config.getSolutionLimit());
		solver.limitTime(config.getTimeLimit());

		for (String cryptarithm : config.getArguments()) {
			final ICryptaNode node = parser.parse(cryptarithm);
			LOGGER.log(Level.INFO, "Parse cryptarithm {0} [OK]", cryptarithm);
			
			solver.solve(node, config, (s) -> {
				System.out.println(s);
				// TODO Pretty print solution in CONSOLE
				if(config.isExportGraphiz()) graphviz.print(node, s, System.out);
				// TODO Check solution by evaluation
			} );		
		}
	}

	public static CryptatorConfig parseOptions(String[] args) {
		final CryptatorConfig config = new CryptatorConfig();
		final CmdLineParser parser = new CmdLineParser(config);
		try {
			// parse the arguments.
			parser.parseArgument(args);

			// you can parse additional arguments if you want.
			// parser.parseArgument("more","args");

			// after parsing arguments, you should check
			// if enough arguments are given.
			if( ! config.getArguments().isEmpty()) return config;

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

		return null;

	}



}
