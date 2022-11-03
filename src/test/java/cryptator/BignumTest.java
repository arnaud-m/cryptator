/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import cryptator.parser.CryptaParserException;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolver;
import cryptator.solver.CryptaSolverException;
import org.junit.BeforeClass;
import org.junit.Test;

public class BignumTest {

	public final CryptaSolvingTester t = new CryptaSolvingTester(new CryptaSolver(true));

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
    // TEST BigNum for crossword
    /*
     * ARISTOCRATIC + PRESCRIPTION + PROSOPOPOEIA + PROTECTORATE = TRANSOCEANIC
     *       +             +               +               +             +
     * PRESCRIPTION + PROSOPOPOEIA + PROTECTORATE + ARISTOCRATIC = TRANSOCEANIC
     *       +             +               +               +             +
     * PROSOPOPOEIA + PROTECTORATE + ARISTOCRATIC + PRESCRIPTION = TRANSOCEANIC
     *       +             +               +               +             +
     * PROTECTORATE + ARISTOCRATIC + PRESCRIPTION + PROSOPOPOEIA = TRANSOCEANIC
     *       =             =               =               =             =
     * TRANSOCEANIC + TRANSOCEANIC + TRANSOCEANIC + TRANSOCEANIC = OSNETOPPCESSC
     */

    @Test
    public void testBigNumCrossWord1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
//		"symbols":"ACEINOPRST","solutions":[[1,0,5,7,4,3,2,6,8,9]
        var horizontal = "ARISTOCRATIC + PRESCRIPTION + PROSOPOPOEIA + PROTECTORATE = TRANSOCEANIC;" +
                "PRESCRIPTION + PROSOPOPOEIA + PROTECTORATE + ARISTOCRATIC = TRANSOCEANIC;" +
                "PROSOPOPOEIA + PROTECTORATE + ARISTOCRATIC + PRESCRIPTION = TRANSOCEANIC;" +
                "PROTECTORATE + ARISTOCRATIC + PRESCRIPTION + PROSOPOPOEIA = TRANSOCEANIC;" +
                "TRANSOCEANIC + TRANSOCEANIC + TRANSOCEANIC + TRANSOCEANIC = OSNETOPPCESSC;";
        var vertical = "ARISTOCRATIC + PRESCRIPTION + PROSOPOPOEIA + PROTECTORATE = TRANSOCEANIC;" +
                "PRESCRIPTION + PROSOPOPOEIA + PROTECTORATE + ARISTOCRATIC = TRANSOCEANIC;" +
                "PROSOPOPOEIA + PROTECTORATE + ARISTOCRATIC + PRESCRIPTION = TRANSOCEANIC;" +
                "PROTECTORATE + ARISTOCRATIC + PRESCRIPTION + PROSOPOPOEIA = TRANSOCEANIC;" +
                "TRANSOCEANIC + TRANSOCEANIC + TRANSOCEANIC + TRANSOCEANIC = OSNETOPPCESSC;";
        t.testUNIQUE(horizontal+vertical);
    }

	/*
	GMAGEGEEGAMGM	+	GXWYAEYACWCYMP	+	GXMWAYEXWGMA	=	GMAGCYABBMXGBX
		+					+					+					+
	GXWYEPGWXYWYWX	+	PYWXWEYPYWPYWE	+	YWPYEWXWYXWY	=	CXCYWYPEXYAYYM
		=					=					=					=
	GMMGPWAAPXAGXM	+	BWYWMWBWBWACGW	+	WWYCYCPXCMMG	!=	YAGXCBWXCXEXEM
	 */
	@Test
	public void testBigNumCrossWord2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		var horizontal = "GMAGEGEEGAMGM+GXWYAEYACWCYMP+GXMWAYEXWGMA=GMAGCYABBMXGBX;" +
				"GXWYEPGWXYWYWX+PYWXWEYPYWPYWE+YWPYEWXWYXWY=CXCYWYPEXYAYYM;" +
				"GMMGPWAAPXAGXM+BWYWMWBWBWACGW+WWYCYCPXCMMG=YAGXCBWXCXEXEM;";
		var vertical = "GMAGEGEEGAMGM+GXWYEPGWXYWYWX=GMMGPWAAPXAGXM;" +
				"GXWYAEYACWCYMP+PYWXWEYPYWPYWE=BWYWMWBWBWACGW;" +
				"GXMWAYEXWGMA+YWPYEWXWYXWY=WWYCYCPXCMMG;" +
				"GMAGCYABBMXGBX+CXCYWYPEXYAYYM=YAGXCBWXCXEXEM";
		t.testUNIQUE(horizontal+vertical);
	}

	@Test
	public void testBigNumUNSAT() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		// The second expression falsifies the first
		var str = "ARISTOCRATIC + PRESCRIPTION + PROSOPOPOEIA + PROTECTORATE = TRANSOCEANIC;" +
				"AAAAAAAAAAAAAAAAAAAA + " +
				"AAAAAAAAAAAAAAAAAAAA = " +
				"AAAAAAAAAAAAAAAAAAAA; ";
		t.testUNSAT(str);
	}

	@Test
	public void testBigNumSAT1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		// The second expressions make the first expression unique
		var str ="AAAAAAAAAAAAAAAAAAAA + " +
				"AAAAAAAAAAAAAAAAAAAA + " +
				"AAAAAAAAAAAAAAAAAAAA + " +
				"AAAAAAAAAAAAAAAAAAAA = " +
				"BBBBBBBBBBBBBBBBBBBB; A + A + A + A = B";
		t.testSAT(str);
		t.testNotUNIQUE(str);
	}

	@Test
	public void testBigNumUnique() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		// Three expression where the third make the evaluation unique
		var str = "AAAAAAAAAAAAAAAAAAAA + " +
				"AAAAAAAAAAAAAAAAAAAA + " +
				"AAAAAAAAAAAAAAAAAAAA + " +
				"AAAAAAAAAAAAAAAAAAAA = " +
				"DDDDDDDDDDDDDDDDDDDD; " +
				"ABAAAAAAAABAAAA + AABBABB = ABAAAAAABBDCBCC;" +
				"A + A + A + A + A = E";
		t.testSAT(str);
		t.testUNIQUE(str);
	}

	// Tests from SolverTest class
	@Test
	public void testSendMoreMoneyList2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.config.setHornerScheme(true);
		t.testNotUNIQUE("send+more=money; a+b=c");
	}

	@Test
	public void testSendMoreMoneyList3() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.config.setHornerScheme(true);
		t.testSAT("send+more=money; a+b=c");
	}

	@Test
	public void testSendMoreMoneyList4() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.config.setHornerScheme(true);
		t.testUNSAT("send+more=money; s+e=n");
	}

	@Test
	public void testSendMoreMoneyList5() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.config.setHornerScheme(true);
		t.testUNSAT("send+more=money;;; s+e=n");
	}

	@Test
	public void testSendMoreMoneyList6() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.config.setHornerScheme(true);
		t.testUNSAT("send+more=money; s+e=n;");
	}
}
