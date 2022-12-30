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
import java.util.OptionalInt;

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

    private void testGenerate(final int expectedSolCount, final OptionalInt expectedCandCount,
            final WordArray wordArray, final boolean lightModel, final boolean lightPropagation)
            throws CryptaModelException {
        final CryptagenConfig config = new CryptagenConfig();
        config.setLightModel(lightModel);
        config.setLightPropagation(lightPropagation);
        final CryptaListGenerator gen = new CryptaListGenerator(wordArray, config, Cryptagen.LOGGER);
        CryptaBiConsumer cons = new CryptaBiConsumer(Cryptagen.LOGGER);
        cons.withSolutionLog();
        cons.withSolutionCheck(config.getArithmeticBase());
        assertEquals(0, cons.getErrorCount());
        long actualCandCount = gen.generate(cons);
        if (expectedCandCount.isPresent()) {
            assertEquals(expectedCandCount.getAsInt(), actualCandCount);
        }
        assertEquals(expectedSolCount, cons.getSolutionCount());
    }

    private void testGenerate(final int expectedSolCount, final WordArray wordArray) throws CryptaModelException {
        testGenerate(expectedSolCount, OptionalInt.empty(), wordArray);
    }

    private void testGenerate(final int expectedSolCount, final OptionalInt expectedCandCount,
            final WordArray wordArray) throws CryptaModelException {
        testGenerate(expectedSolCount, expectedCandCount, wordArray, false, false);
        testGenerate(expectedSolCount, expectedCandCount, wordArray, false, true);
        testGenerate(expectedSolCount, expectedCandCount, wordArray, true, false);
        testGenerate(expectedSolCount, expectedCandCount, wordArray, true, true);
    }

    private void testHeavyGenerate(final int expectedSolCount, final OptionalInt expectedCandCount,
            final WordArray wordArray) throws CryptaModelException {
        testGenerate(expectedSolCount, expectedCandCount, wordArray, false, false);
        testGenerate(expectedSolCount, expectedCandCount, wordArray, true, false);
    }

    @Test
    public void testSendMoreMoney() throws CryptaModelException {
        final WordArray words = new WordArray(Arrays.asList("send", "more", "money"), null);
        testGenerate(1, OptionalInt.of(1), words);
    }

    @Test
    public void testSendMuchMoreMoney() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("send", "much", "more", "money"), null);
        testGenerate(1, OptionalInt.of(6), words, false, true);

    }

    @Test
    public void testPlanets1() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("venus", "earth", "uranus", "saturn"), null);
        testGenerate(2, words);
    }

    @Test
    public void testPlanets2() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("venus", "earth", "uranus", "saturn"), "planets");
        testGenerate(1, words);
    }

    @Test
    public void testAbcde() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("a", "bb", "ccc", "dddd", "eeeee"), null);
        testHeavyGenerate(0, OptionalInt.of(11), words);
    }

    @Test
    public void testAbcdef() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("a", "bb", "ccc", "dddd", "eeeee"), "ffffff");
        testHeavyGenerate(0, OptionalInt.of(15), words);
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
