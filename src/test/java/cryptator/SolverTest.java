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

import org.junit.Before;
import org.junit.BeforeClass;
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

	@BeforeClass
	public static void configureTestLoggers() {
		JULogUtil.configureTestLoggers();
	}

	
	@Before
	public void setDefaultConfig() {
		config = new CryptaConfig();
		solver.limitSolution(100);
		solver.limitTime(2000);
	}

	private int testCryptarithmWithSolutions(String cryptarithm) throws CryptaParserException, CryptaModelException, CryptaSolverException {
		final int[] sols = new int[1];
		final ICryptaNode node = parser.parse(cryptarithm);
		assertTrue(cryptarithm, 
				solver.solve(node, config, (s) -> {
					//System.out.println(s);
					sols[0]++;
					try {
						assertEquals(1, eval.evaluate(node, s, config.getArithmeticBase()));
					} catch (CryptaEvaluationException e) {
						e.printStackTrace();
						fail();
					}
				} )
				);
		return sols[0];
	}
	

	private void testCryptarithmWithoutSolutions(String cryptarithm) throws CryptaParserException, CryptaModelException, CryptaSolverException {
		final ICryptaNode node = parser.parse(cryptarithm);
		assertFalse(cryptarithm,
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
		assertEquals(1, testCryptarithmWithSolutions("send+more=money"));
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
	public void testSendMoreMoney4() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		assertEquals(1, testCryptarithmWithSolutions(" -send -more= -money"));
	}
	
	@Test
	public void testSendMoreMoney5() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		assertEquals(1, testCryptarithmWithSolutions(" (-send) + (-more) = (-money)"));
	}

	@Test
	public void testSendMoreMoney6() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		assertEquals(1, testCryptarithmWithSolutions("(-(-send)) + (-(-more)) = (-(-money))"));
	}
	
	@Test
	public void testSendMoreMoney7() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		assertEquals(1, testCryptarithmWithSolutions("(-send) - more = -money"));
	}

	@Test
	public void testSendMoreMoney8() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		assertEquals(1, testCryptarithmWithSolutions("(-(-send)) - (-more) = money"));
	}
	
	@Test
	public void testSendMoreMoney9() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		assertEquals(1, testCryptarithmWithSolutions("(send+more=money)"));
		assertEquals(1, testCryptarithmWithSolutions("(((send+more)=(money)))"));
	}
	
	@Test
	public void testBigCatLionSolutionLimit() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		solver.limitSolution(5);
		assertEquals(5, testCryptarithmWithSolutions("big + cat = lion"));
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

	// From http://colin.barker.pagesperso-orange.fr/crypta.htm
	@Test
	public void testBarker1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		assertEquals(1, testCryptarithmWithSolutions("oregon+georgia+indiana=arizona"));
	}
	
	@Test
	public void testBarker2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		assertEquals(1, testCryptarithmWithSolutions("carter+reagan+lincoln=clinton"));
	}
	
	@Test
	public void testBarker3() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		assertEquals(1, testCryptarithmWithSolutions("pear+plum+apple+grape+lemon=orange"));
	}
	
	@Test
	public void testBarker4() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		assertEquals(1, testCryptarithmWithSolutions("copper*neon=iron*silver"));
	}
	
	@Test
	public void testBarker5() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		assertEquals(1, testCryptarithmWithSolutions("cinq*six=trente"));
	}
	
	@Test
	@Ignore // Hypothesis: integer overflow
	public void testBarker6() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		assertEquals(1, testCryptarithmWithSolutions("iron*radium=neon*sodium"));
	}
	
	
	
	
	
}
