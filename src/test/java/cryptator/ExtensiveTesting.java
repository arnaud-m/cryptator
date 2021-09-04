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

import java.io.InputStream;
import java.util.Scanner;

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

public class ExtensiveTesting {

	public CryptaConfig config = new CryptaConfig();

	public final CryptaParserWrapper parser = new CryptaParserWrapper();

	public final ICryptaSolver solver= new CryptaSolver();

	public final ICryptaEvaluation eval = new CryptaEvaluation();

	public ExtensiveTesting() {}

	@BeforeClass
	public static void configureTestLoggers() {
		JULogUtil.configureTestLoggers();
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
	public void testAll() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		final InputStream in = getClass().getClassLoader().getResourceAsStream("cryptarithms-barker.txt");
		final Scanner s = new Scanner(in);
		try {
			// s.skip("\\s*#.*"); // not working
			while(s.hasNextLine()) {
				final String line = s.nextLine();
				if(! line.matches("\\s*#.*")) {
					testCryptarithm(line);
				}
			}
		} finally {
			s.close();
		}
	}
}
