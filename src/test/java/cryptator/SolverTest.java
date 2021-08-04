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

import java.util.logging.Level;

import org.junit.Before;
import org.junit.Test;

import cryptator.parser.CryptaParserException;
import cryptator.parser.CryptaParserWrapper;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolver;
import cryptator.solver.CryptaSolverException;
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
		CryptaSolver.LOGGER.setLevel(Level.WARNING);
		config.setArithmeticBase(10);
		config.allowLeadingZeros(false);
		solver.limitSolution(100);
		solver.limitTime(2000);
	}
	
	private void testCryptarithmWithSolutions(String cryptarithm) throws CryptaParserException, CryptaModelException, CryptaSolverException {
		ICryptaNode node = parser.parse(cryptarithm);
		solver.solve(node, config, (s) -> {
			// System.out.println(s);
			try {
				assertEquals(1, eval.evaluate(node, s, config.getArithmeticBase()));
			} catch (CryptaEvaluationException e) {
				e.printStackTrace();
				fail();
			}
		} );
	}
	
	private void testCryptarithmWithoutSolutions(String cryptarithm) throws CryptaParserException, CryptaModelException, CryptaSolverException {
		ICryptaNode node = parser.parse(cryptarithm);
		solver.solve(node, config, (s) -> {
			fail("solution should not exist.");
		} );
	}

	@Test
	public void testSendMoreMoney1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		testCryptarithmWithSolutions("send+more=money");
	}
	
	@Test
	public void testSendMoreMoney2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		config.setArithmeticBase(16);
		testCryptarithmWithSolutions("send+more=money");
	}
	
	@Test
	public void testSendMoreMoney3() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		config.allowLeadingZeros(true);
		testCryptarithmWithSolutions("send+more=money");
	}

	@Test
	public void testBigCatLion1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		testCryptarithmWithSolutions("big+cat=lion");
	}

	@Test
	public void testBigCatLion2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		config.setArithmeticBase(16);
		testCryptarithmWithSolutions("big+cat=lion");
	}

	@Test
	public void testBigCatLion3() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		config.allowLeadingZeros(true);
		testCryptarithmWithSolutions("big+cat=lion");
	}

	@Test
	public void testBigCatBig1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		testCryptarithmWithoutSolutions("big+cat=big");
	}
	
	@Test
	public void testBigCatBig2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		testCryptarithmWithoutSolutions("big*cat=big");
	}
	
	@Test
	public void testBigCatBig3() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		config.allowLeadingZeros(true);
		testCryptarithmWithoutSolutions("big*cat=big");
	}
	
	@Test
	public void testDonaldGeraldRobert1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		testCryptarithmWithSolutions("donald + gerald = robert");
	}
	
	@Test
	public void testDonaldGeraldRobert2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		config.setArithmeticBase(2);
		testCryptarithmWithSolutions("donald + gerald = robert");
	}
	
}
