/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import org.junit.BeforeClass;
import org.junit.Test;

import cryptator.parser.CryptaParserException;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolverException;

public class BignumTest {

	public final CryptaSolvingTester t = new CryptaSolvingTester(true);
	
	public BignumTest() {}

	@BeforeClass
	public static void configureTestLoggers() {
		JULogUtil.configureTestLoggers();
	}
	

	@Test
	public void testBigNum1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.testUNIQUE("ARISTOCRATIC + PRESCRIPTION + PROSOPOPOEIA + PROTECTORATE = TRANSOCEANIC");
	}
	
	@Test
	public void testBigNum2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.testUNIQUE("NARRAGANSETT + NONOGENARIAN + OSTEOPOROSIS + PROPORTIONATE + TRANSPOSITION = RETROGRESSION");
	}
	
	@Test
	public void testBigNum3() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.testUNIQUE("INTERNESCINE + OSTEOPOROSIS + PERSPIRATION + POSTPROCESSOR + PROSCRIPTION = TRANSPIRATION");
	}
	
	@Test
	public void testBigNum4() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.testUNIQUE("ANTIPERSPIRANT + INTERSTITIAL + PRESENTATION + PROPORTIONATE + TRANSLITERATE = TRANSPORTATION");
	}
	
	@Test
	public void testBigNum5() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.testUNIQUE("ARISTOCRATIC + POSTPOSITION + PROCRASTINATE + PROPRIOCEPTION + PROSCRIPTION + PROTECTORATE + TRANSOCEANIC = INTERPRETATION");
	}
	
	@Test
	public void testBigNum6() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.testUNIQUE("INAPPROPRIATE + OSTEOPOROSIS + PROCRASTINATE + PROSOPOPOEIA + RECONNAISSANCE + SPECTROSCOPE + TRANSCRIPTION = PROPRIOCEPTION");
	}
	
	@Test
	public void testBigNum7() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.testUNIQUE("APPERCEPTION + ARISTOCRATIC + INTERPRETATION + PRESENTATION + PROPRIOCEPTION + PROSCRIPTION + TRANSPIRATION + TRANSPOSITION = CONCESSIONAIRE ");
	}
	
	@Test
	public void testBigNum8() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.testUNIQUE("PERSPIRATION + POSTPROCESSOR + PRACTITIONER + PROSCRIPTION + PROSOPOPOEIA" + 
				" + RECONNAISSANCE + TRANSOCEANIC + TRANSPOSITION = CONCESSIONAIRE");
	}
	
}
