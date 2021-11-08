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

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import cryptator.parser.CryptaParserException;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolver;
import cryptator.solver.CryptaSolverException;

public class Failing {

	public final CryptaSolvingTester t = new CryptaSolvingTester(new CryptaSolver(true));

	public Failing() {}

	@BeforeClass
	public static void configureTestLoggers() {
		JULogUtil.configureTestLoggers();
	}

	@Test
	// FIXME integer overflow ?
	public void testBarker6() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		t.testUNIQUE("iron*radium=neon*sodium");
	}

	@Test
	// FIXME integer overflow ?
	public void testPrinterError1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		// https://mathworld.wolfram.com/PrintersErrors.html
		t.testUNIQUE("2^5*9^2 = 2592");
	}


	@Test
	//FIXME confusion between division operator (integer/real)
	public void testDivision1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		assertEquals("Div and Mult",
				t.testSAT("AB = C * BC"),			
				t.testSAT("AB / BC = C")
				);
	}


	@Test
	//FIXME confusion between division operator (integer/real)
	public void testDivision2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		assertEquals("Div and Mult",
				t.testSAT("A = B * C"),
				t.testSAT("A / B = C")				
				);
	}

	@Test
	//FIXME confusion between division operator (integer/real)
	public void testMrazik() throws CryptaParserException, CryptaModelException, CryptaSolverException {
		assertEquals("Div and Mult",
				t.testSAT("A * (4*TH + OF*JULY) = HAPPY"),
				t.testSAT("A = HAPPY / (4*TH + OF*JULY)")
				);
	}	

}
