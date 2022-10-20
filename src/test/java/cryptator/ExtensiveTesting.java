/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.io.InputStream;
import java.util.Scanner;

import org.junit.BeforeClass;
import org.junit.Test;

import cryptator.parser.CryptaParserException;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolver;
import cryptator.solver.CryptaSolverException;

public class ExtensiveTesting {

	public final CryptaSolvingTester t = new CryptaSolvingTester(new CryptaSolver(false));
	
	public ExtensiveTesting() {}

	@BeforeClass
	public static void configureTestLoggers() {
		JULogUtil.configureTestLoggers();
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
					t.testUNIQUE(line);
				}
			}
		} finally {
			s.close();
		}
	}
	
}
