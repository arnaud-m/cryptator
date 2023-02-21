/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
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

    private final CryptaSolvingTester t = new CryptaSolvingTester(true);

    public BignumTest() {
    }

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
        t.testUNIQUE(
                "ARISTOCRATIC + POSTPOSITION + PROCRASTINATE + PROPRIOCEPTION + PROSCRIPTION + PROTECTORATE + TRANSOCEANIC = INTERPRETATION");
    }

    @Test
    public void testBigNum6() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNIQUE(
                "INAPPROPRIATE + OSTEOPOROSIS + PROCRASTINATE + PROSOPOPOEIA + RECONNAISSANCE + SPECTROSCOPE + TRANSCRIPTION = PROPRIOCEPTION");
    }

    @Test
    public void testBigNum7() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNIQUE(
                "APPERCEPTION + ARISTOCRATIC + INTERPRETATION + PRESENTATION + PROPRIOCEPTION + PROSCRIPTION + TRANSPIRATION + TRANSPOSITION = CONCESSIONAIRE ");
    }

    @Test
    public void testBigNum8() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNIQUE("PERSPIRATION + POSTPROCESSOR + PRACTITIONER + PROSCRIPTION + PROSOPOPOEIA"
                + " + RECONNAISSANCE + TRANSOCEANIC + TRANSPOSITION = CONCESSIONAIRE");
    }
    // TEST BigNum for crossword
    /*
     * ARISTOCRATIC + PRESCRIPTION + PROSOPOPOEIA + PROTECTORATE = TRANSOCEANIC + +
     * + + + PRESCRIPTION + PROSOPOPOEIA + PROTECTORATE + ARISTOCRATIC =
     * TRANSOCEANIC + + + + + PROSOPOPOEIA + PROTECTORATE + ARISTOCRATIC +
     * PRESCRIPTION = TRANSOCEANIC + + + + + PROTECTORATE + ARISTOCRATIC +
     * PRESCRIPTION + PROSOPOPOEIA = TRANSOCEANIC = = = = = TRANSOCEANIC +
     * TRANSOCEANIC + TRANSOCEANIC + TRANSOCEANIC = OSNETOPPCESSC
     */

    @Test
    public void testBigNumCrossWord1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
// "symbols":"ACEINOPRST","solutions":[[1,0,5,7,4,3,2,6,8,9]
        var horizontal = "ARISTOCRATIC + PRESCRIPTION + PROSOPOPOEIA + PROTECTORATE = TRANSOCEANIC;"
                + "PRESCRIPTION + PROSOPOPOEIA + PROTECTORATE + ARISTOCRATIC = TRANSOCEANIC;"
                + "PROSOPOPOEIA + PROTECTORATE + ARISTOCRATIC + PRESCRIPTION = TRANSOCEANIC;"
                + "PROTECTORATE + ARISTOCRATIC + PRESCRIPTION + PROSOPOPOEIA = TRANSOCEANIC;"
                + "TRANSOCEANIC + TRANSOCEANIC + TRANSOCEANIC + TRANSOCEANIC = OSNETOPPCESSC;";
        var vertical = "ARISTOCRATIC + PRESCRIPTION + PROSOPOPOEIA + PROTECTORATE = TRANSOCEANIC;"
                + "PRESCRIPTION + PROSOPOPOEIA + PROTECTORATE + ARISTOCRATIC = TRANSOCEANIC;"
                + "PROSOPOPOEIA + PROTECTORATE + ARISTOCRATIC + PRESCRIPTION = TRANSOCEANIC;"
                + "PROTECTORATE + ARISTOCRATIC + PRESCRIPTION + PROSOPOPOEIA = TRANSOCEANIC;"
                + "TRANSOCEANIC + TRANSOCEANIC + TRANSOCEANIC + TRANSOCEANIC = OSNETOPPCESSC;";
        t.testUNIQUE(horizontal + vertical);
    }

    /*
     * GMAGEGEEGAMGM + GXWYAEYACWCYMP + GXMWAYEXWGMA = GMAGCYABBMXGBX + + + +
     * GXWYEPGWXYWYWX + PYWXWEYPYWPYWE + YWPYEWXWYXWY = CXCYWYPEXYAYYM = = = =
     * GMMGPWAAPXAGXM + BWYWMWBWBWACGW + WWYCYCPXCMMG != YAGXCBWXCXEXEM
     */
    @Test
    public void testBigNumCrossWord2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        var horizontal = "GMAGEGEEGAMGM+GXWYAEYACWCYMP+GXMWAYEXWGMA=GMAGCYABBMXGBX;"
                + "GXWYEPGWXYWYWX+PYWXWEYPYWPYWE+YWPYEWXWYXWY=CXCYWYPEXYAYYM;"
                + "GMMGPWAAPXAGXM+BWYWMWBWBWACGW+WWYCYCPXCMMG=YAGXCBWXCXEXEM;";
        var vertical = "GMAGEGEEGAMGM+GXWYEPGWXYWYWX=GMMGPWAAPXAGXM;" + "GXWYAEYACWCYMP+PYWXWEYPYWPYWE=BWYWMWBWBWACGW;"
                + "GXMWAYEXWGMA+YWPYEWXWYXWY=WWYCYCPXCMMG;" + "GMAGCYABBMXGBX+CXCYWYPEXYAYYM=YAGXCBWXCXEXEM";
        t.testUNIQUE(horizontal + vertical);
    }

    @Test
    public void testBigNumUNSAT() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        // The second expression falsifies the first
        var str = "ARISTOCRATIC + PRESCRIPTION + PROSOPOPOEIA + PROTECTORATE = TRANSOCEANIC;"
                + "AAAAAAAAAAAAAAAAAAAA + " + "AAAAAAAAAAAAAAAAAAAA = " + "AAAAAAAAAAAAAAAAAAAA; ";
        t.testUNSAT(str);
    }

    @Test
    public void testBigNumSAT1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        // The second expressions make the first expression unique
        var str = "AAAAAAAAAAAAAAAAAAAA + " + "AAAAAAAAAAAAAAAAAAAA + " + "AAAAAAAAAAAAAAAAAAAA + "
                + "AAAAAAAAAAAAAAAAAAAA = " + "BBBBBBBBBBBBBBBBBBBB; A + A + A + A = B";
        t.testSAT(str);
        t.testNotUNIQUE(str);
    }

    @Test
    public void testBigNumUnique() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        // Three expression where the third make the evaluation unique
        var str = "AAAAAAAAAAAAAAAAAAAA + " + "AAAAAAAAAAAAAAAAAAAA + " + "AAAAAAAAAAAAAAAAAAAA + "
                + "AAAAAAAAAAAAAAAAAAAA = " + "DDDDDDDDDDDDDDDDDDDD; " + "ABAAAAAAAABAAAA + AABBABB = ABAAAAAABBDCBCC;"
                + "A + A + A + A + A = E";
        t.testSAT(str);
        t.testUNIQUE(str);
    }

    // Tests with ints
    @Test
    public void testIntegers01() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNSAT("s='1'; send+more=money");
    }

    @Test
    public void testIntegers02() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNIQUE("m='1'; send+more=money");
    }

    @Test
    public void testIntegers03() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testNotUNIQUE("a='1'; a+b=d");
    }

    @Test
    public void testIntegers04() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNIQUE("a='1'; b='2'+a; a+b=d");
    }

    @Test
    public void testIntegers05() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNIQUE("'11'+'33'='44'");
    }

    @Test
    public void testIntegers06() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        var test = "r='4'; false=true+maybe;s='3'; y=f+s;u='6'; ";
        t.testSAT(test);
        t.testUNIQUE(test);
    }

    @Test
    public void testIntegers07() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNSAT("'11'+'33'='45'");
    }

    @Test
    public void testIntegers08() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        var test = "r='4'; false=true+maybe;s='3'; y=f+s+'12';u='6'; ";
        t.testUNSAT(test);
    }

    @Test
    public void testSetBase1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        var test = "r='36' ";
        t.config.setArithmeticBase(40);
        t.testUNIQUE(test);
    }

    @Test
    public void testSetBase2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        var test = "1AB52='109394'";
        t.config.setArithmeticBase(16);
        t.testUNIQUE(test);
    }

    // Tests from SolverTest class
    @Test
    public void testSendMoreMoneyList2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testNotUNIQUE("send+more=money; a+b=c");
    }

    @Test
    public void testSendMoreMoneyList3() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testSAT("send+more=money; a+b=c");
    }

    @Test
    public void testSendMoreMoneyList4() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNSAT("send+more=money; s+e=n");
    }

    @Test
    public void testSendMoreMoneyList5() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNSAT("send+more=money;;; s+e=n");
    }

    @Test
    public void testSendMoreMoneyList6() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNSAT("send+more=money; s+e=n;");
    }
}
