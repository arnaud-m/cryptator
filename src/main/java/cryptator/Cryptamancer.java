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

import cryptator.game.CryptaGameEngine;
import cryptator.game.CryptaGameException;
import cryptator.parser.CryptaParserWrapper;
import cryptator.solver.CryptaModeler;
import cryptator.specs.ICryptaGameEngine;
import cryptator.specs.ICryptaNode;

public class Cryptamancer {

	static {
		// TODO how to configure logging for the tests ? 
		Cryptator.configureLoggers();
	}

	private static final Logger LOGGER = Logger.getLogger(Cryptamancer.class.getName());

	private static Boolean parseDecision(Scanner s, ICryptaGameEngine engine) throws CryptaGameException {
		//s.nextLine();
		if(s.hasNext()) {
			final String symbol = s.next();
			if(symbol.length() == 1 && s.hasNext()) {
				final CryptaOperator operator =  CryptaOperator.valueOfToken(s.next());
				if(s.hasNextInt()) {
					final int value = s.nextInt();
					return engine.takeDecision(symbol.charAt(0), operator, value);
				}
			}
		}
		return null;
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
		final Scanner scanner = new Scanner(System.in);
		final CryptaParserWrapper parser = new CryptaParserWrapper();
		final ICryptaNode node = parser.parse("send+more=money");
		final CryptaConfig config = new CryptaConfig();
		final CryptaModeler modeler= new CryptaModeler();
		// TODO Log Config ?
		CryptaGameEngine engine = new CryptaGameEngine(modeler.model(node, config));
		engine.setUp();
		int n = 1;
		while(n < 5) {
			LOGGER.log(Level.INFO, "Turn {0}\nEnter a decision:", n);
			try {
				Boolean answer = parseDecision(scanner, engine);
				if(answer == null) 	LOGGER.warning("Cannot parse the decision.");
				else {
					if (answer.booleanValue()) LOGGER.info("decision accepted.");
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
		engine.tearDown();

	}

}
