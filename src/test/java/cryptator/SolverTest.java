/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import cryptator.config.CryptaConfig;
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

final class CryptaSolvingTester {

    protected final CryptaParserWrapper parser = new CryptaParserWrapper();
    protected final ICryptaSolver solver;
    protected final ICryptaEvaluation eval = new CryptaEvaluation();
    protected CryptaConfig config = new CryptaConfig();

    CryptaSolvingTester(final boolean useBignum) {
        this(new CryptaSolver(useBignum));
    }

    CryptaSolvingTester(final ICryptaSolver solver) {
        super();
        this.solver = solver;
    }

    public void reset() {
        config = new CryptaConfig();
        solver.limitSolution(0);
        solver.limitTime(0);
    }

    public int testSolve(final String cryptarithm, final boolean hasSolution)
            throws CryptaModelException, CryptaSolverException {
        final AtomicInteger solutionCount = new AtomicInteger();
        final ICryptaNode node = parser.parse(cryptarithm);
        assertEquals(cryptarithm, hasSolution, solver.solve(node, config, (s) -> {
            // System.out.println(s);
            solutionCount.incrementAndGet();
            try {
                assertEquals(BigInteger.ONE, eval.evaluate(node, s, config.getArithmeticBase()));
            } catch (CryptaEvaluationException e) {
                e.printStackTrace();
                fail();
            }
        }));
        return solutionCount.get();
    }

    public int testSAT(final String cryptarithm) throws CryptaModelException, CryptaSolverException {
        return testSolve(cryptarithm, true);
    }

    public void testSAT(final String cryptarithm, final int solutionCount)
            throws CryptaModelException, CryptaSolverException {
        assertEquals("solution count " + cryptarithm, solutionCount, testSolve(cryptarithm, true));
    }

    public void testUNSAT(final String... cryptarithms) throws CryptaModelException, CryptaSolverException {
        for (String cryptarithm : cryptarithms) {
            assertEquals("solution count " + cryptarithm, 0, testSolve(cryptarithm, false));
        }
    }

    public void testUNIQUE(final String... cryptarithms) throws CryptaModelException, CryptaSolverException {
        for (String cryptarithm : cryptarithms) {
            testSAT(cryptarithm, 1);
        }
    }

    public void testNotUNIQUE(final String... cryptarithms) throws CryptaModelException, CryptaSolverException {
        for (String cryptarithm : cryptarithms) {
            assertTrue("solution count " + cryptarithm, testSolve(cryptarithm, true) > 1);
        }
    }

    public void testResource(final String resourcePath)
            throws CryptaParserException, CryptaModelException, CryptaSolverException {
        final InputStream in = getClass().getClassLoader().getResourceAsStream(resourcePath);
        final Scanner s = new Scanner(in);
        try {
            // s.skip("\\s*#.*"); // not working
            while (s.hasNextLine()) {
                final String line = s.nextLine();
                if (!line.matches("\\s*#.*")) {
                    testUNIQUE(line);
                }
            }
        } finally {
            s.close();
        }
    }
}

public class SolverTest {

    private CryptaSolvingTester t = new CryptaSolvingTester(false);

    public SolverTest() {
    }

    @BeforeClass
    public static void configureTestLoggers() {
        JULogUtil.configureTestLoggers();
    }

    @Before
    public void setDefaultConfig() {
        t.reset();
    }

    @Test
    public void testSendMoreMoney1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setHornerScheme(true);
        t.testUNIQUE("send+more=money");
    }

    @Test
    public void testSendMoreMoney2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setArithmeticBase(16);
        t.solver.limitSolution(100);
        t.testSAT("send+more=money");
    }

    @Test
    public void testSetBase1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        var test = "r='36'";
        t.config.setArithmeticBase(40);
        t.testUNIQUE(test);
    }

    @Test
    public void testSetBase2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        var test = "1AB52='109394'";
        t.config.setArithmeticBase(16);
        t.testUNIQUE(test);
    }

    @Test
    public void testSetBase1doubleticks() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        var test = "r=\"36\"";
        t.config.setArithmeticBase(40);
        t.testUNIQUE(test);
    }

    @Test
    public void testSetBase2doubleticks() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        var test = "1AB52=\"109394\"";
        t.config.setArithmeticBase(16);
        t.testUNIQUE(test);
    }

    @Test
    public void testSendMoreMoney3() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setAllowLeadingZeros(true);
        t.solver.limitSolution(100);
        t.testSAT("send+more=money");
    }

    @Test
    public void testSendMoreMoneyParenthesis()
            throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNIQUE(" -send -more= -money", " (-send) + (-more) = (-money)", "(-(-send)) + (-(-more)) = (-(-money))",
                "(-send) - more = -money", "(-(-send)) - (-more) = money", "(send+more=money)",
                "(((send+more)=(money)))");
    }

    @Test
    public void testBigCatLionSolutionLimit()
            throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.solver.limitSolution(5);
        t.testSAT("big + cat = lion", 5);
    }

    @Test
    public void testBigCatLion1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.solver.limitSolution(100);
        t.testSAT("big + cat = lion");
    }

    @Test
    public void testBigCatLion2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setArithmeticBase(16);
        t.config.setHornerScheme(true);
        t.solver.limitSolution(100);
        t.testSAT("big + cat = lion");
    }

    @Test
    public void testBigCatLion3() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setAllowLeadingZeros(true);
        t.solver.limitSolution(100);
        t.testSAT("big + cat = lion");
    }

    @Test
    public void testBigCatBig1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNSAT("big + cat = big");
    }

    @Test
    public void testBigCatBig2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNSAT("big / cat = big");
    }

    @Test
    public void testBigCatBig3() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setAllowLeadingZeros(true);
        t.testUNSAT("big * cat = big");
    }

    @Test
    public void testDonaldGeraldRobert1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testSAT("donald + gerald = robert");
    }

    @Test
    public void testDonaldGeraldRobert2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setArithmeticBase(2);
        t.testUNSAT("donald + gerald = robert");
    }

    @Test
    public void testDonaldGeraldRobert3() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setArithmeticBase(2);
        t.config.setHornerScheme(true);
        t.testUNSAT("donald + gerald = robert");
    }

    @Test
    @Ignore("It is a choco issue.")
    public void testEMC2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.solver.limitSolution(100);
        t.testSAT("nrgy = MC ^ 2");
    }

    @Test
    public void testBinEMC2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setArithmeticBase(2);
        t.testUNSAT("nrgy = MC ^ 2");
    }

    @Test
    public void testQuaEMC2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setArithmeticBase(4);
        t.solver.limitSolution(100);
        t.testSAT("nrgy = MC ^ 2");
    }

    @Test
    public void testModulo() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.solver.limitSolution(100);
        t.testSAT("B = BAC % AC");
    }

    @Test
    public void testGE() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setHornerScheme(true);
        t.solver.limitSolution(100);
        t.testSAT("BAC > B * A * C");
    }

    @Test
    public void testGQ() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.solver.limitSolution(100);
        t.testSAT("BAC >= B * A * C");
    }

    @Test
    public void testLE() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setHornerScheme(true);
        t.solver.limitSolution(100);
        t.testSAT("B * A / C < B + A * C");
    }

    @Test
    public void testLQ() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.solver.limitSolution(100);
        t.testSAT("B + A * C <= B * A / C");
    }

    @Test
    public void testNE1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNSAT("A != A");
    }

    @Test
    public void testNE2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testSAT("A != B");
    }

    @Test
    public void testSpeed() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.solver.limitSolution(100);
        t.testSAT("s = m/s + km / h");
    }

    @Test
    public void testBinSpeed() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setArithmeticBase(2);
        t.testUNSAT("s = m/s + km / h");
    }

    // From http://colin.barker.pagesperso-orange.fr/crypta.htm
    @Test
    public void testBarker1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNIQUE("oregon+georgia+indiana=arizona");
    }

    @Test
    public void testBarker2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNIQUE("carter+reagan+lincoln=clinton");
    }

    @Test
    public void testBarker3() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNIQUE("pear+plum+apple+grape+lemon=orange");
    }

    @Test
    public void testBarker5() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNIQUE("cinq*six=trente");
    }

    @Test
    public void testPavlis() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNIQUE(
                "SO+MANY+MORE+MEN+SEEM+TO+SAY+THAT+THEY+MAY+SOON+TRY+TO+STAY+AT+HOME+SO+AS+TO+SEE+OR+HEAR+THE+SAME+ONE+MAN+TRY+TO+MEET+THE+TEAM+ON+THE+MOON+AS+HE+HAS+AT+THE+OTHER+TEN=TESTS");
    }

    @Test
    public void testGraham() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setArithmeticBase(11);
        t.testUNIQUE("UNITED + STATES = AMERICA");
    }

    @Test
    public void testFaresMult() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setArithmeticBase(6);
        t.testUNIQUE("FARES = FEE * FEE");
    }

    @Test
    public void testFaresPow() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setArithmeticBase(6);
        t.testUNIQUE("FARES = FEE ^'2'");
    }

    @Test
    public void testTokyo10() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setArithmeticBase(10);
        t.testUNSAT("TOKYO  =  KYOTO * '3'");
    }

    @Test
    public void testTokyo9() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setArithmeticBase(9);
        t.testSAT("TOKYO  =  KYOTO * '3'");
    }

    @Test
    public void testPow1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testSAT("A = B ^ C", 2);
    }

    @Test
    public void testDivision1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        assertEquals("Div and Mult", t.testSAT("AB = C * BC"), t.testSAT("AB / BC = C"));
    }

    @Test
    public void testDivision2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        assertEquals("Div and Mult", t.testSAT("A = B * C"), t.testSAT("A / B = C"));
    }

    @Test
    public void testFloorDivision1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        assertEquals("Floor Division", 25, t.testSAT("A // B = C"));
    }

    @Test
    public void testFloorDivision2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        assertEquals("Floor Division", 176, t.testSAT("AC // B = D"));
    }

    // Start AND tests
    @Test
    public void testAndUnique1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNIQUE("aa+b=cd; a*a=a");
    }

    @Test
    public void testAndNotUnique1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testNotUNIQUE("aa+b=cd");
    }

    @Test
    public void testAndUNSAT1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNSAT("a*a=a; a*a=a+a; a+a!=a");
    }

    @Test
    public void testAndNotUnique2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNIQUE("a*a=b;b=a+a");
    }

    // ABC * DE = CFGH
    // + * -
    // JDHJ + DGC = JGKK
    // ------------------
    // JEDK + EBAH = FAGH
    @Test
    public void testCrossNumber1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNIQUE("ABC * DE = CFGH; " + "JDHJ + DGC = JGKK; " + "JEDK + EBAH = FAGH; " + "ABC + JDHJ = JEDK; "
                + "DE * DGC = EBAH; " + "CFGH-JGKK=FAGH");
    }

    @Test
    public void testCrossNumber2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNSAT("ABC * DE = CFGH; " + "JDHJ + DGC = JGKK; " + "JEDK + EBAH = FAGH; " + "ABC + JDHJ = JEDK; "
                + "DE * DGC = EBAH; " + "CFGH-JGKK=FAGA");
    }

    @Test
    public void testSendMoreMoneyList() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setHornerScheme(true);
        t.testUNIQUE("send+more=money; d+e>=y");
    }

    @Test
    public void testSendMoreMoneyList1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setHornerScheme(true);
        t.testUNIQUE("send+more=money; -send -more= -money");
    }

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
    public void testUnsatSendMoreMoneyList() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setHornerScheme(true);
        t.testUNSAT("send+more=money; s+e=n", "send+more=money;;; s+e=n", "send+more=money; s+e=n;",
                "send+more=money&& s+e=n");
    }

    // Long multiplication with integer
    /*
     * SEE * SO = MIMEO MIMEO = EMOO + 10*MESS SEE * O = EMOO SEE * S = MESS
     */
    @Test
    public void testUniqueLongMultiplications() throws CryptaModelException, CryptaSolverException {
        String[] cryptarithms = {"SEE * SO = MIMEO; MIMEO = EMOO + '10'*MESS;SEE * O = EMOO;SEE * S = MESS",
                "SEE * SO = MIMEO; MIMEO = EMOO + \"10\"*MESS;SEE * O = EMOO;SEE * S = MESS",
                "CUT * T = BUST; CUT * I = TNNT; TNNT * '10' + BUST = TENET; TENET = CUT * IT",
                "RED * S = ARCS; RED * A = RED; RED * '10' + ARCS = CDTS; CDTS = RED * AS",
                "HOW * E = HAIL; HOW * W = PAL; HAIL + PAL * '10' = LHAL; HOW * WE = LHAL",
                "SEE * SO = MIMEO&& MIMEO = EMOO + '10'*MESS&&SEE * O = EMOO&&SEE * S = MESS",
                "CUT * T = BUST&& CUT * I = TNNT&& TNNT * '10' + BUST = TENET&& TENET = CUT * IT",
                "RED * S = ARCS&& RED * A = RED&& RED * '10' + ARCS = CDTS&& CDTS = RED * AS",
                "HOW * E = HAIL&& HOW * W = PAL&& HAIL + PAL * '10' = LHAL&& HOW * WE = LHAL"

        };
        t.testUNIQUE(cryptarithms);
    }

    @Test
    public void testEvaluationLongMultiplicationFail3()
            throws CryptaParserException, CryptaSolverException, CryptaModelException {
        var cryptarithm = "RED * S = ARCS; RED * A = RED; RED * '10' + ARCS = CDTS; CDTS = RED * AS + '1'";
        t.testUNSAT(cryptarithm);
    }

    // LONG DIVISION
    /*
     * K M ----------- A K A / D A D D Y D Y N A ----------- A R M Y A R K A
     * --------- R A
     */
    @Test
    public void testEvaluationLongDivision1()
            throws CryptaParserException, CryptaSolverException, CryptaModelException {
        var cryptarithm = "AKA * K = DYNA; DADD - DYNA = ARM; AKA * M = ARKA; ARMY - ARKA = RA; AKA * KM + RA = DADDY";
        t.testUNIQUE(cryptarithm);
    }

    @Test
    public void testEvaluationLongDivisionFail1()
            throws CryptaParserException, CryptaSolverException, CryptaModelException {
        var cryptarithm = "AKA * K = DYNA; DADD + DYNA = ARM; AKA * M = ARKA; ARMY - ARKA = RA; AKA * KM + RA = DADDY";
        t.testUNSAT(cryptarithm);
    }

    // Test from issue 25
    // 9END+M08E=10NEY is wrong because M is already assigned 1.
    @Test
    public void testEvaluation1Issue25() throws CryptaParserException, CryptaSolverException, CryptaModelException {
        var cryptarithm = "9END+M08E=10NEY;1='1';0='0';9='9';8='8'";
        t.testUNSAT(cryptarithm);
    }

    @Test
    public void testEvaluation2Issue25() throws CryptaParserException, CryptaSolverException, CryptaModelException {
        var cryptarithm = "9END+M08E=10NEY;1=\"1\";0=\"0\";9=\"9\";8=\"8\"";
        t.testUNSAT(cryptarithm);
    }

    @Test
    public void testAndUnique1symbol() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNIQUE("aa+b=cd&& a*a=a");
    }

    @Test
    public void testAndUNSAT1symbol() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNSAT("a*a=a&& a*a=a+a&& a+a!=a");
    }

    @Test
    public void testAndNotUnique2symbol() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNIQUE("a*a=b&&b=a+a");
    }

    @Test
    public void testCrossNumber1symbol() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNIQUE("ABC * DE = CFGH&& " + "JDHJ + DGC = JGKK&& " + "JEDK + EBAH = FAGH&& " + "ABC + JDHJ = JEDK&& "
                + "DE * DGC = EBAH&& " + "CFGH-JGKK=FAGH");
    }

    @Test
    public void testCrossNumber2symbol() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNSAT("ABC * DE = CFGH&& " + "JDHJ + DGC = JGKK&& " + "JEDK + EBAH = FAGH&& " + "ABC + JDHJ = JEDK&& "
                + "DE * DGC = EBAH&& " + "CFGH-JGKK=FAGA");
    }

    @Test
    public void testSendMoreMoneyListsymbol()
            throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setHornerScheme(true);
        t.testUNIQUE("send+more=money&& d+e>=y");
    }

    @Test
    public void testSendMoreMoneyList1symbol()
            throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setHornerScheme(true);
        t.testUNIQUE("send+more=money&& -send -more= -money");
    }

    @Test
    public void testSendMoreMoneyList2symbol()
            throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setHornerScheme(true);
        t.testNotUNIQUE("send+more=money&& a+b=c");
    }

    @Test
    public void testSendMoreMoneyList3symbol()
            throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setHornerScheme(true);
        t.testSAT("send+more=money&& a+b=c");
    }

    // Long multiplication with integer

    @Test
    public void testEvaluationLongMultiplicationFail3symbol()
            throws CryptaParserException, CryptaSolverException, CryptaModelException {
        var cryptarithm = "RED * S = ARCS&& RED * A = RED&& RED * '10' + ARCS = CDTS&& CDTS = RED * AS + '1'";
        t.testUNSAT(cryptarithm);
    }

    // LONG DIVISION

    @Test
    public void testEvaluationLongDivision1symbol()
            throws CryptaParserException, CryptaSolverException, CryptaModelException {
        var cryptarithm = "AKA * K = DYNA&& DADD - DYNA = ARM&& AKA * M = ARKA&& ARMY - ARKA = RA&& AKA * KM + RA = DADDY";
        t.testUNIQUE(cryptarithm);
    }

    @Test
    public void testEvaluationLongDivisionFail1symbol()
            throws CryptaParserException, CryptaSolverException, CryptaModelException {
        var cryptarithm = "AKA * K = DYNA&& DADD + DYNA = ARM&& AKA * M = ARKA&& ARMY - ARKA = RA&& AKA * KM + RA = DADDY";
        t.testUNSAT(cryptarithm);
    }

    // Test from issue 25
    // 9END+M08E=10NEY is wrong because M is already assigned 1.
    @Test
    public void testEvaluation3Issue25() throws CryptaParserException, CryptaSolverException, CryptaModelException {
        var cryptarithm = "9END+M08E=10NEY&&1='1'&&0='0'&&9='9'&&8='8'";
        t.testUNSAT(cryptarithm);
    }

    @Test
    public void testEvaluation4Issue25() throws CryptaParserException, CryptaSolverException, CryptaModelException {
        // TODO : modify this line for issue 39 to accept W = 0
        var cryptarithm = "W='4'";
        t.testUNIQUE(cryptarithm);
    }

    @Test
    public void testSillke() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testResource("crosswords-sillke.db.txt");
    }

    @Test
    public void testAssignTalos1() throws CryptaParserException, CryptaSolverException, CryptaModelException {
        var cryptarithm = "alpos + les + otos = talos";
        t.config.setAssignments(Map.of("a", "1", "e", "3"));
        t.testUNIQUE(cryptarithm);
    }

    @Test
    public void testAssignTalos2() throws CryptaParserException, CryptaSolverException, CryptaModelException {
        var cryptarithm = "alpos + les + otos = talos";
        t.config.setAssignments(Map.of("a", "2", "e", "3"));
        t.testUNSAT(cryptarithm);
    }
}
