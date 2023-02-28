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

import java.util.Arrays;
import java.util.OptionalInt;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import cryptator.cmd.CryptaBiConsumer;
import cryptator.cmd.WordArray;
import cryptator.config.CryptagenConfig;
import cryptator.gen.CryptaListGenerator;
import cryptator.solver.CryptaModelException;

public class GenerateTest {

    final CryptagenConfig config = new CryptagenConfig();

    @BeforeClass
    public static void configureTestLoggers() {
        JULogUtil.configureTestLoggers();
    }

    private void configure(final int gridSize, final boolean lightModel, final boolean lightPropagation) {
        config.setGridSize(gridSize);
        config.setLightModel(lightModel);
        config.setLightPropagation(lightPropagation);
    }

    private void testGenerate(final int expectedSolCount, final OptionalInt expectedCandCount,
            final WordArray wordArray) throws CryptaModelException {
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

    private void testGenerate(final int expectedSolCount, final WordArray wordArray, final int gridSize)
            throws CryptaModelException {
        testGenerate(expectedSolCount, OptionalInt.empty(), wordArray, 0);
    }

    private void testGenerate(final int expectedSolCount, final OptionalInt expectedCandCount,
            final WordArray wordArray, final int gridSize) throws CryptaModelException {
        configure(gridSize, false, false);
        testGenerate(expectedSolCount, expectedCandCount, wordArray);
        configure(gridSize, false, true);
        testGenerate(expectedSolCount, expectedCandCount, wordArray);
        configure(gridSize, true, false);
        testGenerate(expectedSolCount, expectedCandCount, wordArray);
        configure(gridSize, false, true);
        testGenerate(expectedSolCount, expectedCandCount, wordArray);
    }

    private void testHeavyGenerate(final int expectedSolCount, final OptionalInt expectedCandCount,
            final WordArray wordArray, final int gridSize) throws CryptaModelException {
        configure(gridSize, false, false);
        testGenerate(expectedSolCount, expectedCandCount, wordArray);
        configure(gridSize, true, false);
        testGenerate(expectedSolCount, expectedCandCount, wordArray);
    }

    private void testMultGenerate(final int expectedSolCount, final WordArray wordArray) throws CryptaModelException {
        JULogUtil.configureSilentLoggers();
        config.setMultModel(true);
        configure(0, false, false);
        testGenerate(expectedSolCount, OptionalInt.empty(), wordArray);
        configure(0, false, true);
        testGenerate(expectedSolCount, OptionalInt.empty(), wordArray);
        config.setMultModel(false);
    }

    private void testLongMultGenerate(final int expectedSolCount, final WordArray wordArray)
            throws CryptaModelException {
        JULogUtil.configureSilentLoggers();
        config.setLongMultModel(true);
        configure(0, false, false);
        testGenerate(expectedSolCount, OptionalInt.empty(), wordArray);
        configure(0, false, true);
        testGenerate(expectedSolCount, OptionalInt.empty(), wordArray);
        config.setLongMultModel(false);
    }

    @Test
    public void testSendMoreMoney() throws CryptaModelException {
        final WordArray words = new WordArray(Arrays.asList("send", "more", "money"), null);
        testGenerate(1, OptionalInt.of(1), words, 0);
    }

    @Test
    public void testSendMuchMoreMoney() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("send", "much", "more", "money"), null);
        configure(0, false, true);
        testGenerate(1, OptionalInt.of(6), words);

    }

    @Test
    public void testPlanets1() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("venus", "earth", "uranus", "saturn"), null);
        testGenerate(2, words, 0);
    }

    @Test
    public void testPlanets2() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("venus", "earth", "uranus", "saturn"), "planets");
        testGenerate(1, words, 0);
    }

    @Test
    public void testAbcde() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("a", "bb", "ccc", "dddd", "eeeee"), null);
        testHeavyGenerate(0, OptionalInt.of(11), words, 0);
    }

    @Test
    public void testAbcdef() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("a", "bb", "ccc", "dddd", "eeeee"), "ffffff");
        testHeavyGenerate(0, OptionalInt.of(15), words, 0);
    }

    @Test
    public void testDoublyTrue1() throws CryptaModelException {
        testGenerate(0, new WordArray("FR", "fr", 0, 10), 0);
    }

    @Test
    public void testDoublyTrue2() throws CryptaModelException {
        testGenerate(1, new WordArray("FR", "fr", 30, 30), 0);
    }

    @Test
    public void testDoublyTrue3() throws CryptaModelException {
        testGenerate(3, new WordArray("IT", "it", 20, 30), 0);
    }

    @Test
    public void testCrossword1() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("AB", "C", "AC", "AD", "E", "FB", "FD", "G", "EC"), null);
        testHeavyGenerate(0, OptionalInt.empty(), words, 3);
    }

    @Test
    public void testCrossword2() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("A", "B", "CD", "CE", "F", "CF", "CA", "CC", "DE"), null);
        testHeavyGenerate(2, OptionalInt.empty(), words, 3);
    }

    @Ignore("Take too long")
    @Test
    public void testCrossword3() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("HJ", "AD", "DF", "BG", "EC", "DF", "AC", "GE", "HEK"), null);
        testHeavyGenerate(2, OptionalInt.empty(), words, 3);
    }

    @Test
    public void testMult1() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("mad", "man", "asylum"), null);
        testMultGenerate(0, words);
    }

    @Test
    public void testMult2() throws CryptaModelException {
        config.setArithmeticBase(9);
        WordArray words = new WordArray(Arrays.asList("alfred", "e", "neuman"), null);
        testMultGenerate(2, words);
        config.setArithmeticBase(10);
    }

    @Test
    public void testMult3() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("nora", "l", "aron"), null);
        testMultGenerate(2, words);
    }

    @Test
    public void testMult4() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("ba", "cba", "dcba"), null);
        testMultGenerate(1, words);
    }

    @Test
    public void testMult5() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("north", "south", "east", "west"), null);
        testMultGenerate(2, words);
    }

    @Test
    public void testLongMult1() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("who", "is", "hobs", "hawi", "mosis"), null);
        testLongMultGenerate(1, words);
    }

    @Test
    public void testLongMult3() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("get", "by", "babe", "beare"), null);
        testLongMultGenerate(1, words);
    }

}
