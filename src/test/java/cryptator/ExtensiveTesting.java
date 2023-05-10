/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import org.junit.BeforeClass;
import org.junit.Test;

import cryptator.parser.CryptaParserException;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolverException;

public class ExtensiveTesting {

    public ExtensiveTesting() {
    }

    @BeforeClass
    public static void configureTestLoggers() {
        JULogUtil.configureTestLoggers();
    }

    @Test
    public void testBarker() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        (new CryptaSolvingTester(false)).testResource("cryptarithms-barker.db.txt");
    }

    @Test
    public void testBarker4() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        (new CryptaSolvingTester(true)).testUNIQUE("copper*neon=iron*silver");
    }

    @Test
    public void testBarker6() throws CryptaParserException, CryptaModelException, CryptaSolverException {
        (new CryptaSolvingTester(true)).testUNIQUE("iron*radium=neon*sodium");
    }
}
