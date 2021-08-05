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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.logging.Level;

import org.junit.Before;
import org.junit.Ignore;
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

	public CryptaConfig config = new CryptaConfig();

	public final CryptaParserWrapper parser = new CryptaParserWrapper();

	public final ICryptaSolver solver= new CryptaSolver();

	public final ICryptaEvaluation eval = new CryptaEvaluation();

	public SolverTest() {}

	@Before
	public void setDefaultConfig() {
		CryptaSolver.LOGGER.setLevel(Level.WARNING);
		config = new CryptaConfig();
		solver.limitSolution(100);
		solver.limitTime(2000);
	}

	private void testCryptarithmWithSolutions(String cryptarithm) throws CryptaParserException, CryptaModelException, CryptaSolverException {
		ICryptaNode node = parser.parse(cryptarithm);
		assertTrue(
				solver.solve(node, config, (s) -> {
					//System.out.println(s);
					try {
						assertEquals(1, eval.evaluate(node, s, config.getArithmeticBase()));
					} catch (CryptaEvaluationException e) {
						e.printStackTrace();
						fail();
					}
				} )
				);
	}

	private void testCryptarithmWithoutSolutions(String cryptarithm) throws CryptaParserException, CryptaModelException, CryptaSolverException {
		ICryptaNode node = parser.parse(cryptarithm);
		assertFalse(
				solver.solve(node, config, (s) -> {
					fail("solution should not exist.");
				} )
				);
	}

	@Test
	public void testSendMoreMoney1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		solver.limitSolution(0);
		solver.limitTime(0);
		config.useHornerScheme(true);
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
		config.useHornerScheme(true);
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
		solver.limitSolution(0);
		solver.limitTime(0);
		testCryptarithmWithSolutions("donald + gerald = robert");
	}

	@Test
	public void testDonaldGeraldRobert2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		config.setArithmeticBase(2);
		testCryptarithmWithoutSolutions("donald + gerald = robert");
	}
	
	@Test
	public void testDonaldGeraldRobert3() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		config.setArithmeticBase(2);
		config.useHornerScheme(true);
		testCryptarithmWithoutSolutions("donald + gerald = robert");
	}
	
	@Test
	public void testDonaldGeraldRobert4() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		config.setArithmeticBase(2);
		config.allowLeadingZeros();
		config.setRelaxMinDigitOccurence(1);
		testCryptarithmWithoutSolutions("donald + gerald = robert");
	}
	
	@Test
	public void testDonaldGeraldRobert5() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		config.setArithmeticBase(2);
		config.allowLeadingZeros();
		config.setRelaxMinDigitOccurence(1);
		config.setRelaxMaxDigitOccurence(1);
		testCryptarithmWithoutSolutions("donald + gerald = robert");
	}

	@Test
	@Ignore
	public void testEMC2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		testCryptarithmWithSolutions("nrgy = MC ^ 2");
	}

	@Test
	public void testBinEMC2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		config.setArithmeticBase(2);
		testCryptarithmWithoutSolutions("nrgy = MC ^ 2");
	}

	@Test
	public void testQuaEMC2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		config.setArithmeticBase(4);
		testCryptarithmWithSolutions("nrgy = MC ^ 2");
	}

	@Test
	public void testModulo() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		testCryptarithmWithSolutions("B = BAC % AC");
	}

	@Test
	public void testGE() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		config.useHornerScheme(true);
		testCryptarithmWithSolutions("BAC > B * A * C");
	}

	@Test
	public void testGQ() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		testCryptarithmWithSolutions("BAC >= B * A * C");
	}

	@Test
	public void testLE() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		config.useHornerScheme(true);
		testCryptarithmWithSolutions("B * A / C < B + A * C");
	}

	@Test
	public void testLQ() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		testCryptarithmWithSolutions("B + A * C <= B * A / C");
	}

	@Test
	public void testNE1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		testCryptarithmWithoutSolutions("A != A");
	}

	@Test
	public void testNE2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		testCryptarithmWithSolutions("A != B");
	}

	@Test
	public void testSpeed() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		testCryptarithmWithSolutions("s = m/s + km / h");
	}
	
	@Test
	public void testBinSpeed() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		config.setArithmeticBase(2);
		testCryptarithmWithoutSolutions("s = m/s + km / h");
	}

}
