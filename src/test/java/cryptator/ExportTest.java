/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import cryptator.parser.CryptaParserException;
import cryptator.parser.CryptaParserWrapper;
import cryptator.solver.CryptaSolutionException;
import cryptator.solver.CryptaSolutionMap;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.tree.CryptaEvaluationException;
import cryptator.tree.GraphvizExport;

public class ExportTest {
	
	public ExportTest() {}
	
	public final CryptaParserWrapper parser = new CryptaParserWrapper();


	@Test
	public void testExport1() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("SEND+MORE=MONEY");
		assertNotNull(GraphvizExport.exportToGraphviz(cryptarithm));
	}
	
	@Test
	public void testExport2() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("SEND+MORE=MONEY");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("O = 0 M = 1 Y = 2 E = 5 N = 6 D = 7 R = 8 S = 9");
		assertNotNull(GraphvizExport.exportToGraphviz(cryptarithm, solution));
	}
	
	@Test
	public void testExport3() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("SEND+MUCH+MORE=MONEY");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("O = 0 M = 1 Y = 2 E = 5 N = 6 D = 7 R = 8 S = 9");
		assertNotNull(GraphvizExport.exportToGraphviz(cryptarithm, solution));
	}
	
}
