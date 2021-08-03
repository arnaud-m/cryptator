/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.io.InputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import cryptator.game.CryptaGameEngine;
import cryptator.game.CryptaGameException;
import cryptator.parser.CryptaParserWrapper;
import cryptator.solver.CryptaModeler;
import cryptator.specs.ICryptaGameEngine;
import cryptator.specs.ICryptaNode;

public class Cryptamancer {

	private static final Logger LOGGER = Logger.getLogger(Cryptamancer.class.getName());



	private static Boolean parseDecision(Scanner s, ICryptaGameEngine engine) throws CryptaGameException {
		//s.nextLine();
		if(s.hasNext()) {
			final String symbol = s.next();
			System.out.println(symbol);
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
			LOGGER.log(Level.INFO, "Turn {0} : enter a decision:", n);
			try {
				Boolean answer = parseDecision(scanner, engine);
				if(answer == null) 	LOGGER.warning("Cannot parse the decision.");
				else if (answer.booleanValue()) LOGGER.info("decision accepted.");
				else LOGGER.info("decision rejected.");
			} catch (CryptaGameException e) {
				LOGGER.log(Level.WARNING, "failure while taking the decision.", e);
			}
			n++;
		}
		engine.tearDown();

	}

}
