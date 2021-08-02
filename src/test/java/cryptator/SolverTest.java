/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import cryptator.parser.CryptaParserException;
import cryptator.parser.CryptaParserWrapper;
import cryptator.solver.CryptaSolver;
import cryptator.specs.ICryptaEvaluation;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolver;
import cryptator.tree.CryptaEvaluation;
import cryptator.tree.CryptaEvaluationException;

public class SolverTest {

	public final CryptaConfig config = new CryptaConfig();
	
    public final CryptaParserWrapper parser = new CryptaParserWrapper();

	public final ICryptaSolver solver= new CryptaSolver();

	public final ICryptaEvaluation eval = new CryptaEvaluation();

	public SolverTest() {}

	@Before
	public void setDefaultConfig() {
		config.setArithmeticBase(10);
		config.allowLeadingZeros(false);
		config.setSolutionLimit(100);
	}
	
	private void testCryptarithm(String cryptarithm) throws CryptaParserException {
		ICryptaNode node = parser.parse(cryptarithm);
		solver.solve(node, config, (s) -> {
			System.out.println(s);
			try {
				assertEquals(1, eval.evaluate(node, s, config.getArithmeticBase()));
			} catch (CryptaEvaluationException e) {
				e.printStackTrace();
				fail();
			}
		} );
	}

	@Test
	public void testSendMoreMoney1() throws CryptaParserException {
		testCryptarithm("send+more=money");
	}
	
	@Test
	public void testSendMoreMoney2() throws CryptaParserException {
		config.setArithmeticBase(16);
		testCryptarithm("send+more=money");
	}
	
	@Test
	public void testSendMoreMoney3() throws CryptaParserException {
		config.allowLeadingZeros(true);
		testCryptarithm("send+more=money");
	}


}
