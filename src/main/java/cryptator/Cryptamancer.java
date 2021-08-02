/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import cryptator.game.CryptaGameEngine;
import cryptator.parser.CryptaParserWrapper;
import cryptator.solver.CryptaModeler;
import cryptator.specs.ICryptaNode;

public class Cryptamancer {

	public Cryptamancer() {}

	public static void main(String[] args) throws Exception {
		final CryptaParserWrapper parser = new CryptaParserWrapper();
    	final ICryptaNode node = parser.parse("send+more=money");
		final CryptaConfig config = new CryptaConfig();
		config.setSolutionLimit(1);
		final CryptaModeler modeler= new CryptaModeler();
		CryptaGameEngine engine = new CryptaGameEngine(modeler.model(node, config));
		engine.setUp();
		System.out.println(engine.takeDecision('s', CryptaOperator.EQ, 9));
		System.out.println(engine.takeDecision('o', CryptaOperator.EQ, 1));
		System.out.println(engine.takeDecision('r', CryptaOperator.GEQ, 4));
		System.out.println(engine.takeDecision('y', CryptaOperator.GT, 4));
		System.out.println(engine.takeDecision('y', CryptaOperator.GT, 6));
		
		engine.tearDown();
		
	}

}
