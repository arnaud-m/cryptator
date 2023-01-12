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

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import cryptator.parser.CryptaParserException;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolver;
import cryptator.solver.CryptaSolverException;

public class Failing {

    private final CryptaSolvingTester t = new CryptaSolvingTester(new CryptaSolver(false));

    public Failing() {
    }

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
    @Ignore("Too long (around 1 min).")
    public void testMrazik() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        // Take around 1 minute
        assertEquals("Div and Mult", t.testSAT("A * (4*TH + OF*JULY) = HAPPY"),
                t.testSAT("A = HAPPY / (4*TH + OF*JULY)"));
    }

    @Test
    public void testExpression1() {
        Model m = new Model();
        IntVar x = m.intVar(IntVar.MIN_INT_BOUND, IntVar.MAX_INT_BOUND);
        IntVar y = m.intVar(IntVar.MIN_INT_BOUND, IntVar.MAX_INT_BOUND);
        IntVar xy = x.mul(y).mul(101).mul(101).intVar();
        assertTrue(true);
    }

    @Test
    public void testExpression2() {
        Model m = new Model();
        IntVar x = m.intVar(0, 10);
        IntVar y = m.intVar(0, 10);
        IntVar z = m.intVar(0, 100);

        x.pow(y).eq(z).post();
        assertTrue(true);
    }

    @Test
    public void testPrinterError2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        // https://mathworld.wolfram.com/PrintersErrors.html
        t.testUNIQUE("3^4*425 = 34425");
    }

    @Test
    public void testBarker4() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNIQUE("copper*neon=iron*silver");
    }

}
