/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import cryptator.cmd.CryptaBiConsumer;
import cryptator.cmd.WordArray;
import cryptator.config.CryptagenConfig;
import cryptator.gen.CryptaListGenerator;
import cryptator.solver.CryptaModelException;

public class GenerateTest {

    @BeforeClass
    public static void configureTestLoggers() {
        JULogUtil.configureTestLoggers();
    }

    public long testGenerate(final WordArray wordArray, final boolean lightModel, final boolean lightPropagation)
            throws CryptaModelException {
        final CryptagenConfig config = new CryptagenConfig();
        config.setLightModel(lightModel);
        config.setLightPropagation(lightPropagation);
        final CryptaListGenerator gen = new CryptaListGenerator(wordArray, config, Cryptagen.LOGGER);
        CryptaBiConsumer cons = new CryptaBiConsumer(Cryptagen.LOGGER);
        cons.withSolutionCheck(config.getArithmeticBase());
        gen.generate(cons);
        assertEquals(0, cons.getErrorCount());
        return cons.getSolutionCount();
    }

    public void testGenerate(final int expectedSolCount, final WordArray wordArray) throws CryptaModelException {
        assertEquals(expectedSolCount, testGenerate(wordArray, false, false));
        assertEquals(expectedSolCount, testGenerate(wordArray, false, true));
        assertEquals(expectedSolCount, testGenerate(wordArray, true, false));
        assertEquals(expectedSolCount, testGenerate(wordArray, true, true));
    }

    public void testGenerate(final int expectedSolCount, final String rightMember, final String... words)
            throws CryptaModelException {
        testGenerate(expectedSolCount, new WordArray(Arrays.asList(words), rightMember));
    }

    @Test
    public void testSendMoreMoney() throws CryptaModelException {
        testGenerate(1, null, "send", "more", "money");
    }

    @Test
    public void testPlanets1() throws CryptaModelException {
        testGenerate(2, null, "venus", "earth", "uranus", "saturn");
    }

    @Test
    public void testPlanets2() throws CryptaModelException {
        testGenerate(1, "planets", "venus", "earth", "uranus", "saturn");
    }

    @Test
    public void testDoublyTrue1() throws CryptaModelException {
        testGenerate(0, new WordArray("FR", "fr", 0, 10));
    }

    @Test
    public void testDoublyTrue2() throws CryptaModelException {
        testGenerate(1, new WordArray("FR", "fr", 30, 30));
    }

    @Test
    public void testDoublyTrue3() throws CryptaModelException {
        testGenerate(3, new WordArray("IT", "it", 20, 30));
    }

}
