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

public class EvaluationTest {

	public final CryptaParserWrapper parser = new CryptaParserWrapper();

	public final ICryptaEvaluation eval = new CryptaEvaluation();
	
	public EvaluationTest() {}
	
	@Test
	public void testSolutionParser1() throws CryptaSolutionException {
		final String solution = "A=  1 B    2 C   =3 D=4 E  =  5";
		final ICryptaSolution s = CryptaSolutionMap.parseSolution(solution);
		assertEquals(5, s.size());
		assertEquals(1, s.getDigit('A'));
		assertEquals(2, s.getDigit('B'));
		assertEquals(3, s.getDigit('C'));
		assertEquals(4, s.getDigit('D'));
		assertEquals(5, s.getDigit('E'));		
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
	
	@Test
	public void testEvaluation1() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("SEND+MORE=MONEY");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("O = 0 M = 1 Y = 2 E = 5 N = 6 D = 7 R = 8 S = 9");
		assertEquals(1, eval.evaluate(cryptarithm, solution, 10));
	}
	
	@Test
	public void testBinaryEval1() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("BAB + BA = BBB");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("A 0 B 1");
		assertEquals(1, eval.evaluate(cryptarithm, solution, 2));
	}
	
	@Test
	public void testBinaryEval2() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("BAB + BA = BBB");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("A 1 B 1");
		assertEquals(0, eval.evaluate(cryptarithm, solution, 2));
	}
	
	@Test
	public void testBinaryEval3() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("BBB + B = BAAA");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("A 0 B 1");
		assertEquals(1, eval.evaluate(cryptarithm, solution, 2));
	}
	
	
	
	@Test
	public void testEvaluation2() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("SEND+MORE=MONEY");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("O = 0 M = 1 Y = 2 E = 5 N = 6 D = 7 R = 8 S = 0");
		assertEquals(0, eval.evaluate(cryptarithm, solution, 10));
	}
	
	@Test(expected=CryptaEvaluationException.class)
	public void testEvalPartialSolution() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("SEND+MORE=MONEY");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("O = 0 M = 1 Y = 2 E = 5 N = 6 D = 7 R = 8");
		eval.evaluate(cryptarithm, solution, 10);
	}
	
	@Test(expected=CryptaEvaluationException.class)
	public void testEvalWithInvalidDigit1() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("SEND+MORE=MONEY");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("O = 0 M = 1 Y = 2 E = 5 N = 6 D = 7 R = 8 S = 10");
		eval.evaluate(cryptarithm, solution, 10);
	}
	
	@Test(expected=CryptaEvaluationException.class)
	public void testEvalWithInvalidDigit2() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("SEND+MORE=MONEY");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("O = 0 M = 1 Y = 2 E = 5 N = 6 D = 7 R = 8 S = -9");
		eval.evaluate(cryptarithm, solution, 10);
	}
	
	

}
