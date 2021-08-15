/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionHandlerFilter;

import cryptator.game.CryptaGameDecision;
import cryptator.game.CryptaGameEngine;
import cryptator.game.CryptaGameException;
import cryptator.parser.CryptaParserException;
import cryptator.parser.CryptaParserWrapper;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaModeler;
import cryptator.specs.ICryptaGameEngine;
import cryptator.specs.ICryptaNode;

public class Cryptamancer {

	static {
		// TODO how to configure logging for the tests ? 
		Cryptator.configureLoggers();
	}

	private static final Logger LOGGER = Logger.getLogger(Cryptamancer.class.getName());

	public static CryptamancerConfig parseOptions(String[] args) {
		final CryptamancerConfig config = new CryptamancerConfig();
		final CmdLineParser parser = new CmdLineParser(config);
		try {
			// parse the arguments.
			parser.parseArgument(args);

			if(Cryptator.checkConfiguration(config)) {
				LOGGER.config("Parse options [OK]");
				return config;
			}
		} catch( CmdLineException e ) {
			System.err.println(e.getMessage());
		}

		// if there's a problem in the command line,
		// you'll get this exception. this will report
		// an error message.
		System.err.println("java Cryptamancer [options...] CRYPTARITHM");
		// print the list of available options
		parser.printUsage(System.err);
		System.err.println();

		// print option sample. This is useful some time
		System.err.println("  Example: java Cryptator"+parser.printExample(OptionHandlerFilter.ALL));
		LOGGER.severe("Parse options [FAIL]");
		return null;
	}

	private final static void configureLogging(CryptamancerConfig config) {
		if(config.isVerbose()) {
			LOGGER.setLevel(Level.FINE);
			CryptaGameEngine.LOGGER.setLevel(Level.FINE);
		}
	}
	
	public static ICryptaNode parseCryptarithm(CryptamancerConfig config) {
		final CryptaParserWrapper parser = new CryptaParserWrapper();	
		final String cryptarithm = config.getArguments().get(0);
		LOGGER.log(Level.INFO, "play with the cryptarithm: {0}", cryptarithm);
		try {
			return parser.parse(cryptarithm);
		} catch (CryptaParserException e) {
			LOGGER.log(Level.SEVERE, "failed to parse the cryptarithm.");
			return null;
		}
	}
	
	public static ICryptaGameEngine buildEngine(ICryptaNode node, CryptaConfig config) {
		LOGGER.log(Level.CONFIG, "display model configuration\n{0}", config);
		final CryptaModeler modeler= new CryptaModeler();
		try {
			final CryptaGameEngine engine = new CryptaGameEngine();
			engine.setUp(modeler.model(node, config));
			return engine;
		} catch (CryptaGameException|CryptaModelException e) {
			LOGGER.log(Level.SEVERE, "failed to build the game engine", e);
			return null;
		}
	}
		
	
	private static void play(ICryptaGameEngine engine) {
		final Scanner scanner = new Scanner(System.in);
		int n = 1;
		while( (!engine.isSolved())) {
			LOGGER.log(Level.INFO, "Turn {0}\nEnter a decision:", n);
			try {
				final CryptaGameDecision decision = CryptaGameDecision.parseDecision(scanner);
				if(decision == null) LOGGER.warning("Cannot parse the decision.");
				else {
				final boolean answer = engine.takeDecision(decision);
				if (answer) LOGGER.info("decision accepted.");
				else LOGGER.info("decision rejected.");
				final DisplayPartialSolution display = new DisplayPartialSolution();
				engine.forEachSymbolDomain(display);
					LOGGER.log(Level.INFO, "display the current partial solution.\n{0}", display);
				}
			} catch (CryptaGameException e) {
				LOGGER.log(Level.WARNING, "failure while taking the decision.", e);
			}
			n++;
		}
	}
	
	static private class DisplayPartialSolution implements BiConsumer<Character, String> {

		
		private int length = 2;
		
		private final StringBuilder b1 = new StringBuilder();
		
		private final StringBuilder b2 = new StringBuilder();
		
		@Override
		public void accept(Character t, String u) {
			length = Math.max(length, u.length());
			String format = "%" + length + "s|";
			b1.append(String.format(format, t));
			b2.append(String.format(format, u));
		}

		@Override
		public String toString() {
			return b1.toString() + "\n" + b2.toString();
		}	
	}
	
	public static void main(String[] args) throws Exception {
		
		final CryptamancerConfig config = parseOptions(args);
		if(config == null) return;
		
		configureLogging(config);

		if( config.getArguments().size() != 1) {
			LOGGER.severe("Parse single cryptarithm argument [FAIL]");
			return;
		}
		
		final ICryptaNode node = parseCryptarithm(config);
		if(node == null) return;
		
		
		final ICryptaGameEngine engine = buildEngine(node, config);
		if(engine == null) return;
		
		play(engine);

		engine.tearDown();
	}

}
