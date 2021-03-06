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

import java.math.BigInteger;

import org.junit.Test;

import cryptator.parser.CryptaParserException;
import cryptator.parser.CryptaParserWrapper;
import cryptator.solver.CryptaSolutionException;
import cryptator.solver.CryptaSolutionMap;
import cryptator.specs.ICryptaEvaluation;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.tree.CryptaEvaluation;
import cryptator.tree.CryptaEvaluationException;
import cryptator.tree.CryptaLeaf;
import cryptator.tree.CryptaNode;

public class EvaluationTest {

	public final CryptaParserWrapper parser = new CryptaParserWrapper();

	public final ICryptaEvaluation eval = new CryptaEvaluation();
	
	public EvaluationTest() {}
	
	@Test
	public void testSolutionParser1() throws CryptaSolutionException {
		final String solution = "A=  1 B    2 C   =3 D=4 E  =  5";
		final ICryptaSolution s = CryptaSolutionMap.parseSolution(solution);
		assertEquals(5, s.size());
		assertTrue(s.hasDigit('A'));
		assertEquals(1, s.getDigit('A'));
		assertTrue(s.hasDigit('B'));
		assertEquals(2, s.getDigit('B'));
		assertTrue(s.hasDigit('C'));
		assertEquals(3, s.getDigit('C'));
		assertTrue(s.hasDigit('D'));
		assertEquals(4, s.getDigit('D'));
		assertTrue(s.hasDigit('E'));
		assertEquals(5, s.getDigit('E'));	
		
		assertFalse(s.hasDigit('F'));
		assertFalse(s.hasDigit('G'));
	}

	@Test(expected=CryptaSolutionException.class)
	public void testSolutionException() throws CryptaSolutionException {
		final String solution = "A  1 B =   2 C  3  D=4 E   =  5";
		final ICryptaSolution s = CryptaSolutionMap.parseSolution(solution);
		s.getDigit('F');
	}
	
	@Test(expected = CryptaSolutionException.class)
	public void testInvalidSolutionParser1() throws CryptaSolutionException {
		CryptaSolutionMap.parseSolution("AB 1");		
	}
	
	@Test(expected = CryptaSolutionException.class)
	public void testInvalidSolutionParser2() throws CryptaSolutionException {
		CryptaSolutionMap.parseSolution("A B");		
	}
	
	@Test(expected = CryptaSolutionException.class)
	public void testInvalidSolutionParser3() throws CryptaSolutionException {
		CryptaSolutionMap.parseSolution("A 1 B");		
	}
	
	private void assertTrueEval(ICryptaNode cryptarithm, ICryptaSolution solution, int base) throws CryptaEvaluationException {
		assertEquals(BigInteger.ONE, eval.evaluate(cryptarithm, solution, base));
	}
	
	private void assertFalseEval(ICryptaNode cryptarithm, ICryptaSolution solution, int base) throws CryptaEvaluationException {
		assertEquals(BigInteger.ZERO, eval.evaluate(cryptarithm, solution, base));
	}
	
	
	@Test
	public void testEvaluation1() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("SEND+MORE=MONEY");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("O = 0 M = 1 Y = 2 E = 5 N = 6 D = 7 R = 8 S = 9");
		assertTrueEval(cryptarithm, solution, 10);
	}
	
	@Test
	public void testBinaryEval1() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("BAB + BA = BBB");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("A 0 B 1");
		assertTrueEval(cryptarithm, solution, 2);
	}
	
	@Test
	public void testBinaryEval2() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("BAB + BA = BBB");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("A 1 B 1");
		assertFalseEval(cryptarithm, solution, 2);
	}
	
	@Test
	public void testBinaryEval3() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("BBB + B = BAAA");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("A 0 B 1");
		assertTrueEval(cryptarithm, solution, 2);
	}
	
	
	
	@Test
	public void testEvaluation2() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("SEND+MORE=MONEY");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("O = 0 M = 1 Y = 2 E = 5 N = 6 D = 7 R = 8 S = 0");
		assertFalseEval(cryptarithm, solution, 10);
	}

	@Test
	public void testEvaluation3() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("BIG+CAT=LION");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("B=4 I=2 G=3 C=8 A=5 T=6 L=1 O=7 N=9");
		assertTrueEval(cryptarithm, solution, 10);
	}

	@Test
	public void testEvaluation4() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("3 * MOT = TOM - 1");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("M=2 O=4 T=7 3=3 1=1");
		assertTrueEval(cryptarithm, solution, 10);
	}

	@Test
	public void testEvaluation5() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("TO+GO=OUT");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("T=2 O=1 G=8 U=0");
		assertTrueEval(cryptarithm, solution, 10);
	}

	@Test
	public void testEvaluation6() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("SO+MANY+MORE+MEN+SEEM+TO+SAY+THAT+\n" +
				"THEY+MAY+SOON+TRY+TO+STAY+AT+HOME+\n" +
				"SO+AS+TO+SEE+OR+HEAR+THE+SAME+ONE+\n" +
				"MAN+TRY+TO+MEET+THE+TEAM+ON+THE+\n" +
				"MOON+AS+HE+HAS+AT+THE+OTHER+TEN\n" +
				"=TESTS");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("S=3 O=1 M=2 A=7 N=6 Y=4 R=8 E=0 T=9 H=5");
		assertTrueEval(cryptarithm, solution, 10);
	}
	
	@Test
	public void testEvaluation7() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("A%B=C");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("A=9 B=3 C=0");
		assertTrueEval(cryptarithm, solution, 10);
	}

	@Test
	public void testEvaluation8() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("AB+BA=CBC");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("A=9 B=2 C=1");
		assertTrueEval(cryptarithm, solution, 10);
	}
	

	@Test
	public void testEvaluation9() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("nrgy = MC ^ 2");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("r=0 2=2 C=2 g=1 y=0 M=3 n=3");
		assertTrueEval(cryptarithm, solution, 4);
	}

	
	public void testPartialSolution(String cryptarithm, String partialSolution) throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode node = parser.parse(cryptarithm);
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution(partialSolution);
		eval.evaluate(node, solution, 10);
	}

	
	@Test(expected=CryptaEvaluationException.class)
	public void testEvalPartialSolution() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		testPartialSolution("SEND+MORE=MONEY", "O = 0 M = 1 Y = 2 E = 5 N = 6 D = 7 R = 8");
	}

	@Test(expected=CryptaEvaluationException.class)
	public void testEvalPartialSolution2() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		testPartialSolution("AB+BA=CBC", "A=9 B=2");
	}
	
	@Test(expected=CryptaEvaluationException.class)
	public void testEvalWithInvalidDigit1() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		testPartialSolution("SEND+MORE=MONEY", "O = 0 M = 1 Y = 2 E = 5 N = 6 D = 7 R = 8 S = 10");
	}

	@Test(expected=CryptaEvaluationException.class)
	public void testEvalWithInvalidDigit2() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		testPartialSolution("SEND+MORE=MONEY", "O = 0 M = 1 Y = 2 E = 5 N = 6 D = 7 R = 8 S = -9");
	}

	@Test(expected=CryptaSolutionException.class)
	public void testEvalWithInvalidDigit3() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		testPartialSolution("AB+BA=CBC", "A=9 B=2 C=a");
	}
	
	@Test
	public void testBignum1() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = new CryptaNode("+", 
				new CryptaLeaf("AAAAAAAAAAAAAAAAAAAAAAAAA".toCharArray()),
				new CryptaLeaf("BBBBBBBBBBBBBBBBBBBBBBBBB".toCharArray())
				);
		
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("A=1 B=2 C=3");
		assertEquals(new BigInteger("3333333333333333333333333"), eval.evaluate(cryptarithm, solution, 10));
	}
	
	@Test
	public void testBignum2() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("AAAAAAAAAAAAAAAAAAAAAAAAA + BBBBBBBBBBBBBBBBBBBBBBBBB = CCCCCCCCCCCCCCCCCCCCCCCCC");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("A=1 B=2 C=3");
		assertTrueEval(cryptarithm, solution, 10);
	}
	
	@Test
	public void testBignum3() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("AAAAAAAAAAAAAAAAAAAAAAAAD + A + BBBBBBBBBBBBBBBBBBBBBBDDD + BBB = CCCCCCCCCCCCCCCCCCCCCCCDD + CC");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("A=1 B=2 C=3 D=0");
		assertTrueEval(cryptarithm, solution, 10);
	}

}
