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

	public static final Logger LOGGER = Logger.getLogger(Cryptamancer.class.getName());

	
	static class CryptamancerOptionsParser extends OptionsParser<CryptamancerConfig> {

		public CryptamancerOptionsParser() {
			super(Cryptamancer.class, new CryptamancerConfig());
		}

		@Override
		protected void configureLoggers() {
			super.configureLoggers();
			if(config.isVerbose()) {
				JULogUtil.setLevel(Level.CONFIG, getLogger(), CryptaGameEngine.LOGGER);
			}
		}
		
		public String getArgumentName() {
			return "CRYPTARITHM";
		}
		
		@Override
		protected boolean checkArguments() {
			return config.getArguments().size() == 1;
		}
	}

	public static ICryptaNode parseCryptarithm(CryptamancerConfig config) {
		final String cryptarithm = config.getArguments().get(0);
		try {
			return Cryptator.parseCryptarithm(
					cryptarithm,
					new CryptaParserWrapper(),
					LOGGER);
		} catch (CryptaParserException e) {
			LOGGER.log(Level.SEVERE, "Parse cryptarithm " + cryptarithm + " [FAIL]", e);
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
		JULogUtil.configureLoggers();
		
		CryptamancerOptionsParser optparser = new CryptamancerOptionsParser();
		if( ! optparser.parseOptions(args)) return;
		final CryptamancerConfig config = optparser.getConfig();
			
		final ICryptaNode node = parseCryptarithm(config);
		if(node == null) return;
		
		
		final ICryptaGameEngine engine = buildEngine(node, config);
		if(engine == null) return;
		
		play(engine);

		engine.tearDown();
	}

}
