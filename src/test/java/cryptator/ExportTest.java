/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

import cryptator.parser.CryptaParserException;
import cryptator.parser.CryptaParserWrapper;
import cryptator.solver.CryptaSolutionException;
import cryptator.solver.CryptaSolutionMap;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.tree.CryptaEvaluationException;
import cryptator.tree.GraphizExporter;

public class ExportTest {
	
	static final class NullOutputStream extends OutputStream {

		public final static NullOutputStream INSTANCE = new NullOutputStream();
		
		@Override
		public void write(int b) throws IOException {
			// DO Nothing
		}
	}
	
	private final OutputStream outstream = NullOutputStream.INSTANCE;
	
	public ExportTest() {}
	
	public final CryptaParserWrapper parser = new CryptaParserWrapper();

	public final GraphizExporter export = new GraphizExporter();

	@Test
	public void testExport1() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("SEND+MORE=MONEY");
		export.print(cryptarithm, outstream);
	}
	
	@Test
	public void testExport2() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("SEND+MORE=MONEY");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("O = 0 M = 1 Y = 2 E = 5 N = 6 D = 7 R = 8 S = 9");
		export.print(cryptarithm, solution, outstream);
	}
	
	@Test
	public void testExport3() throws CryptaParserException, CryptaSolutionException, CryptaEvaluationException {
		final ICryptaNode cryptarithm = parser.parse("SEND+MUCH+MORE=MONEY");
		final ICryptaSolution solution = CryptaSolutionMap.parseSolution("O = 0 M = 1 Y = 2 E = 5 N = 6 D = 7 R = 8 S = 9");
		export.print(cryptarithm, solution, outstream);
	}
	
}
