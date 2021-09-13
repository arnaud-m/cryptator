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

import java.util.concurrent.atomic.AtomicInteger;

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


final class CryptaSolvingTester {

	public CryptaConfig config = new CryptaConfig();

	public final CryptaParserWrapper parser = new CryptaParserWrapper();

	public final ICryptaSolver solver;
	
	public final ICryptaEvaluation eval = new CryptaEvaluation();

	
	public CryptaSolvingTester(boolean useBignum) {
		super();
		this.solver = new CryptaSolver(useBignum);
	}

	public void reset() {
		config = new CryptaConfig();
		solver.limitSolution(0);
		solver.limitTime(0);
	}

	public int testSolve(String cryptarithm, boolean hasSolution) throws CryptaModelException, CryptaSolverException {
		final AtomicInteger solutionCount = new AtomicInteger();
		final ICryptaNode node = parser.parse(cryptarithm);
		assertEquals(
				cryptarithm, 
				hasSolution,
				solver.solve(node, config, (s) -> {
					//System.out.println(s);
					solutionCount.incrementAndGet();
					try {
						assertEquals(1, eval.evaluate(node, s, config.getArithmeticBase()));
					} catch (CryptaEvaluationException e) {
						e.printStackTrace();
						fail();
					}
				} )
				);
		return solutionCount.get();	
	}

	public int testSAT(String cryptarithm) throws CryptaModelException, CryptaSolverException {
		return testSolve(cryptarithm, true);
	}

	public void testSAT(String cryptarithm, int solutionCount) throws CryptaModelException, CryptaSolverException {
		assertEquals("solution count " + cryptarithm, solutionCount, testSolve(cryptarithm, true));
	}

	public void testUNSAT(String cryptarithm) throws CryptaModelException, CryptaSolverException {
		assertEquals("solution count " + cryptarithm, 0, testSolve(cryptarithm, false));
	}

	public void testUNIQUE(String cryptarithm) throws CryptaModelException, CryptaSolverException {
		testSAT(cryptarithm, 1);
	}
}

public class SolverTest {

	public CryptaSolvingTester t = new CryptaSolvingTester(false);


	public SolverTest() {}

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
		t.config.useHornerScheme(true);
		t.testUNIQUE("send+more=money");
	}

	@Test
	public void testSendMoreMoney2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.config.setArithmeticBase(16);
		t.solver.limitSolution(100);
		t.testSAT("send+more=money");
	}

	@Test
	public void testSendMoreMoney3() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.config.allowLeadingZeros(true);
		t.solver.limitSolution(100);
		t.testSAT("send+more=money");;
	}

	@Test
	public void testSendMoreMoney4() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.testUNIQUE(" -send -more= -money");
	}

	@Test
	public void testSendMoreMoney5() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.testUNIQUE(" (-send) + (-more) = (-money)");
	}

	@Test
	public void testSendMoreMoney6() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.testUNIQUE("(-(-send)) + (-(-more)) = (-(-money))");
	}

	@Test
	public void testSendMoreMoney7() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.testUNIQUE("(-send) - more = -money");
	}

	@Test
	public void testSendMoreMoney8() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.testUNIQUE("(-(-send)) - (-more) = money");
	}

	@Test
	public void testSendMoreMoney9() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.testUNIQUE("(send+more=money)");
	}

	@Test
	public void testSendMoreMoney10() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.testUNIQUE("(((send+more)=(money)))");
	}

	@Test
	public void testBigCatLionSolutionLimit() throws CryptaParserException, CryptaModelException, CryptaSolverException {
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
		t.config.useHornerScheme(true);
		t.solver.limitSolution(100);
		t.testSAT("big + cat = lion");
	}

	@Test
	public void testBigCatLion3() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.config.allowLeadingZeros(true);
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
		t.config.allowLeadingZeros(true);
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
		t.config.useHornerScheme(true);
		t.testUNSAT("donald + gerald = robert");
	}

	@Test
	public void testDonaldGeraldRobert4() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.config.setArithmeticBase(2);
		t.config.allowLeadingZeros();
		t.config.setRelaxMinDigitOccurence(1);
		t.testUNSAT("donald + gerald = robert");
	}

	@Test
	public void testDonaldGeraldRobert5() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.config.setArithmeticBase(2);
		t.config.allowLeadingZeros();
		t.config.setRelaxMinDigitOccurence(1);
		t.config.setRelaxMaxDigitOccurence(1);
		t.testUNSAT("donald + gerald = robert");
	}

	@Test
	@Ignore
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
		t.config.useHornerScheme(true);
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
		t.config.useHornerScheme(true);
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
	public void testBarker4() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.testUNIQUE("copper*neon=iron*silver");
	}

	@Test
	public void testBarker5() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.testUNIQUE("cinq*six=trente");
	}

	@Test
	@Ignore // Hypothesis: integer overflow
	public void testBarker6() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.testUNIQUE("iron*radium=neon*sodium");
	}

	@Test
	public void testPavlis() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.testUNIQUE("SO+MANY+MORE+MEN+SEEM+TO+SAY+THAT+THEY+MAY+SOON+TRY+TO+STAY+AT+HOME+SO+AS+TO+SEE+OR+HEAR+THE+SAME+ONE+MAN+TRY+TO+MEET+THE+TEAM+ON+THE+MOON+AS+HE+HAS+AT+THE+OTHER+TEN=TESTS");
	}

	@Test
	@Ignore
	public void testPrinterError1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		// https://mathworld.wolfram.com/PrintersErrors.html
		t.testUNIQUE("2^5*9^2 = 2592");
	}

	@Test
	@Ignore
	public void testPrinterError2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		// https://mathworld.wolfram.com/PrintersErrors.html
		t.testUNIQUE("3^4*425 = 34425");
	}

	@Test
	public void testGraham() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		// https://mathworld.wolfram.com/PrintersErrors.html
		t.config.setArithmeticBase(11);
		t.testUNIQUE("UNITED + STATES = AMERICA");
	}

	//FIXME division operator confusion
	@Test
	@Ignore
	public void testDivision1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		assertEquals("Div and Mult",
				t.testSAT("ABC = AB * BC"),			
				t.testSAT("ABC / BC = AB")
				);
	}


	@Test
	@Ignore
	public void testDivision2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		assertEquals("Div and Mult",
				t.testSAT("A = B * C"),
				t.testSAT("A / B = C")				
				);
	}

	@Test
	public void testPow1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.testSAT("A = B ^ C", 2);
	}

	@Test
	@Ignore
	public void testMrazik() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		assertEquals("Div and Mult",
				t.testSAT("A * (4*TH + OF*JULY) = HAPPY"),
				t.testSAT("A = HAPPY / (4*TH + OF*JULY)")
				);
	}	

}
