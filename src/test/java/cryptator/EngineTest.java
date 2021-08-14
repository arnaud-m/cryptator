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

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import cryptator.game.CryptaGameEngine;
import cryptator.game.CryptaGameException;
import cryptator.parser.CryptaParserWrapper;
import cryptator.solver.CryptaModel;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaModeler;
import cryptator.solver.CryptaSolver;
import cryptator.specs.ICryptaNode;

public class EngineTest {

	public CryptaConfig config = new CryptaConfig();

	public final CryptaParserWrapper parser = new CryptaParserWrapper();

	public final CryptaModeler modeler = new CryptaModeler();

	public final CryptaGameEngine engine = new CryptaGameEngine();

	public EngineTest() {}
	
	@Before
	public void setDefaultConfig() {
		CryptaGameEngine.LOGGER.setLevel(Level.WARNING);
		CryptaSolver.LOGGER.setLevel(Level.WARNING);
		config = new CryptaConfig();
	}
	
	private void setUpEngine(String cryptarithm) throws CryptaModelException, CryptaGameException {
		final ICryptaNode node = parser.parse(cryptarithm);
		final CryptaModel model = modeler.model(node, config);
		engine.setUp(model);	
	}

	// TODO Test Decision parser ? Create Decision Class ?
	
	private final void testGoodDecision(char symbol, CryptaOperator reOperator, int value) throws CryptaGameException {
		assertTrue(engine.takeDecision(symbol, reOperator, value));
		assertFalse(engine.isSolved());
	}
	
	private final void testBadDecision(char symbol, CryptaOperator reOperator, int value) throws CryptaGameException {
		assertFalse(engine.takeDecision(symbol, reOperator, value));
		assertFalse(engine.isSolved());
	}
	
	@Test
	public void testSendMoreMoney() throws CryptaModelException, CryptaGameException {
		setUpEngine("send + more = money");
			
		testBadDecision('s', CryptaOperator.NEQ, 9);
		testGoodDecision('r', CryptaOperator.EQ, 8);
		testBadDecision('d', CryptaOperator.NEQ, 7);
		testBadDecision('e', CryptaOperator.LT, 5);
		testBadDecision('e', CryptaOperator.GT, 5);
		testBadDecision('y', CryptaOperator.GEQ, 3);
		testBadDecision('y', CryptaOperator.LEQ, 1);
		testGoodDecision('m', CryptaOperator.EQ, 1);
		testBadDecision('n', CryptaOperator.NEQ, 6);
		
		assertTrue(engine.takeDecision('o', CryptaOperator.EQ, 0));
		assertTrue(engine.isSolved());
	}
	
	@Test
	public void testDonaldGeraldRobert() throws CryptaModelException, CryptaGameException {
		setUpEngine("donald+gerald=robert");
		
		testBadDecision('a', CryptaOperator.NEQ, 4);
		testGoodDecision('b', CryptaOperator.LT, 4);
		testGoodDecision('d', CryptaOperator.GT, 4);
		testBadDecision('t', CryptaOperator.GEQ, 4);
		testGoodDecision('t', CryptaOperator.EQ, 0);
		testGoodDecision('r', CryptaOperator.EQ, 7);
		testGoodDecision('b', CryptaOperator.EQ, 3);
		testBadDecision('e', CryptaOperator.LT, 9);
		testBadDecision('g', CryptaOperator.GT, 1);
		testBadDecision('g', CryptaOperator.LT, 1);
		testGoodDecision('l', CryptaOperator.EQ, 8);
		testBadDecision('n', CryptaOperator.GT, 6);
		testBadDecision('n', CryptaOperator.LT, 6);
		testBadDecision('o', CryptaOperator.GT, 2);
		testGoodDecision('d', CryptaOperator.GT, 4);
		testGoodDecision('d', CryptaOperator.EQ, 5);
		
		assertTrue(engine.takeDecision('o', CryptaOperator.EQ, 2));
		assertTrue(engine.isSolved());
		
	}
	
}
