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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.BeforeClass;
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

public class BignumTest {

	public CryptaConfig config = new CryptaConfig();

	public final CryptaParserWrapper parser = new CryptaParserWrapper();

	public final ICryptaSolver solver= new CryptaSolver(true);

	public final ICryptaEvaluation eval = new CryptaEvaluation();

	public BignumTest() {}

	@BeforeClass
	public static void configureTestLoggers() {
		JULogUtil.configureTestLoggers();
	}

	
	@Before
	public void setDefaultConfig() {
		config = new CryptaConfig();
		solver.limitSolution(2);
		solver.limitTime(5);
	}

	private void testCryptarithm(String cryptarithm) throws CryptaParserException, CryptaModelException, CryptaSolverException {
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
		assertEquals(cryptarithm, 1, sols[0]);
	}
	

	@Test
	public void testBigNum1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		testCryptarithm("ARISTOCRATIC + PRESCRIPTION + PROSOPOPOEIA + PROTECTORATE = TRANSOCEANIC");
	}
	
	@Test
	public void testBigNum2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		testCryptarithm("NARRAGANSETT + NONOGENARIAN + OSTEOPOROSIS + PROPORTIONATE + TRANSPOSITION = RETROGRESSION");
	}
	
	@Test
	public void testBigNum3() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		testCryptarithm("INTERNESCINE + OSTEOPOROSIS + PERSPIRATION + POSTPROCESSOR + PROSCRIPTION = TRANSPIRATION");
	}
	
	@Test
	public void testBigNum4() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		testCryptarithm("ANTIPERSPIRANT + INTERSTITIAL + PRESENTATION + PROPORTIONATE + TRANSLITERATE = TRANSPORTATION");
	}
	
	@Test
	public void testBigNum5() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		testCryptarithm("ARISTOCRATIC + POSTPOSITION + PROCRASTINATE + PROPRIOCEPTION + PROSCRIPTION + PROTECTORATE + TRANSOCEANIC = INTERPRETATION");
	}
	
	@Test
	public void testBigNum6() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		testCryptarithm("INAPPROPRIATE + OSTEOPOROSIS + PROCRASTINATE + PROSOPOPOEIA + RECONNAISSANCE + SPECTROSCOPE + TRANSCRIPTION = PROPRIOCEPTION");
	}
	
	@Test
	public void testBigNum7() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		testCryptarithm("APPERCEPTION + ARISTOCRATIC + INTERPRETATION + PRESENTATION + PROPRIOCEPTION + PROSCRIPTION + TRANSPIRATION + TRANSPOSITION = CONCESSIONAIRE ");
	}
	
	@Test
	public void testBigNum8() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		testCryptarithm("PERSPIRATION + POSTPROCESSOR + PRACTITIONER + PROSCRIPTION + PROSOPOPOEIA" + 
				" + RECONNAISSANCE + TRANSOCEANIC + TRANSPOSITION = CONCESSIONAIRE");
	}
	
}
