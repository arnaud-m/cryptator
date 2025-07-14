/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import static cryptator.solver.AdaptiveSolver.computeThreshold;
import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import cryptator.parser.CryptaParserException;
import cryptator.solver.AdaptiveSolver;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolverException;

public class AdaptiveTest {

    private final CryptaSolvingTester t = new CryptaSolvingTester(new AdaptiveSolver(false));

    public AdaptiveTest() {
    }

    @BeforeClass
    public static void configureTestLoggers() {
        JULogUtil.configureTestLoggers();
    }

    @Test
    public void testThreshold() {
        assertEquals(25, computeThreshold(2));
        assertEquals(9, computeThreshold(8));
        assertEquals(8, computeThreshold(10));
        assertEquals(7, computeThreshold(16));
    }

    @Test
    public void testAdaptive1() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNIQUE("ARISTOCRATIC + PRESCRIPTION + PROSOPOPOEIA + PROTECTORATE = TRANSOCEANIC");
    }

    @Test
    public void testAdaptive2() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNIQUE("NARRAGANSETT + NONOGENARIAN + OSTEOPOROSIS + PROPORTIONATE + TRANSPOSITION = RETROGRESSION");
    }

    @Test
    public void testAdaptive3() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNIQUE("PERSPIRATION + POSTPROCESSOR + PRACTITIONER + PROSCRIPTION + PROSOPOPOEIA"
                + " + RECONNAISSANCE + TRANSOCEANIC + TRANSPOSITION = CONCESSIONAIRE");
    }

    @Test
    public void testAdaptive4() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.testUNIQUE("SEND+MORE=MONEY");
    }

    @Test
    public void testAdaptive5() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setArithmeticBase(2);
        t.testUNIQUE("aaaaa + ab = abbbba");
        t.config.setArithmeticBase(10);
    }

    @Test
    public void testAdaptive6() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        t.config.setArithmeticBase(2);
        t.testUNIQUE("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa + a = abbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
        t.config.setArithmeticBase(10);
    }

}
