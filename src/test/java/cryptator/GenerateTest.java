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
import cryptator.config.CryptagenConfig.RightMemberType;
import cryptator.gen.CryptaListGenerator;
import cryptator.solver.CryptaModelException;

public class GenerateTest {

    final CryptagenConfig config = new CryptagenConfig();

    @BeforeClass
    public static void configureTestLoggers() {
        JULogUtil.configureTestLoggers();
    }

    private void configure(final int gridSize, final boolean lightPropagation) {
        config.setGridSize(gridSize);
        config.setLightModel(lightPropagation);
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
        configure(gridSize, false);
        testGenerate(expectedSolCount, expectedCandCount, wordArray);
        configure(gridSize, true);
        testGenerate(expectedSolCount, expectedCandCount, wordArray);
    }

    private void testHeavyGenerate(final int expectedSolCount, final OptionalInt expectedCandCount,
            final WordArray wordArray, final int gridSize) throws CryptaModelException {
        configure(gridSize, false);
        testGenerate(expectedSolCount, expectedCandCount, wordArray);
    }

    private void testMultGenerateLH(final int expectedSolCount, final WordArray wordArray) throws CryptaModelException {
        JULogUtil.configureSilentLoggers();
        config.setRightMemberType(RightMemberType.FREE);
        config.setMultModel(true);
        configure(0, false);
        testGenerate(expectedSolCount, OptionalInt.empty(), wordArray);
        configure(0, true);
        testGenerate(expectedSolCount, OptionalInt.empty(), wordArray);
        config.setMultModel(false);
        config.setRightMemberType(RightMemberType.UNIQUE);
    }

    private void testMultGenerate(final int expectedSolCount, final WordArray wordArray) throws CryptaModelException {
        config.setUseBigNum(false);
        testMultGenerateLH(expectedSolCount, wordArray);
        config.setUseBigNum(true);
        testMultGenerateLH(expectedSolCount, wordArray);
        config.setUseBigNum(false);
    }

    private void testLongMultGenerateLH(final int expectedSolCount, final WordArray wordArray)
            throws CryptaModelException {
        JULogUtil.configureSilentLoggers();
        config.setLongMultModel(true);
        configure(0, false);
        testGenerate(expectedSolCount, OptionalInt.empty(), wordArray);
        // No heavy model
        // configure(0, true);
        // testGenerate(expectedSolCount, OptionalInt.empty(), wordArray);
        config.setLongMultModel(false);
    }

    private void testLongMultGenerate(final int expectedSolCount, final WordArray wordArray)
            throws CryptaModelException {
        config.setUseBigNum(false);
        testLongMultGenerateLH(expectedSolCount, wordArray);
        config.setUseBigNum(true);
        testLongMultGenerateLH(expectedSolCount, wordArray);
        config.setUseBigNum(false);
    }

    @Test
    public void testSendMoreMoney() throws CryptaModelException {
        final WordArray words = new WordArray(Arrays.asList("send", "more", "money"), null);
        testGenerate(1, OptionalInt.of(1), words, 0);
    }

    @Test
    public void testSendMuchMoreMoney() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("send", "much", "more", "money"), null);
        configure(0, true);
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
        testMultGenerateLH(2, words);
        // Takes too long with the bignum model
    }

    @Test
    public void testBignumMult1() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("1002527", "1002553", "1005086451431"), null);
        config.setUseBigNum(true);
        testMultGenerate(1, words);
        config.setUseBigNum(true);
    }

    @Test
    public void testBignumMult2() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("1000721", "1000541", "1001262390061"), null);
        config.setUseBigNum(true);
        testMultGenerate(1, words);
        config.setUseBigNum(true);
    }

    @Ignore("Takes too long.")
    @Test
    public void testBignumMult3() throws CryptaModelException {
        WordArray words = new WordArray(
                Arrays.asList("1002527", "1002553", "1005086451431", "1000721", "1000541", "1001262390061"), null);
        config.setUseBigNum(true);
        testMultGenerate(4, words);
        config.setUseBigNum(true);
    }

    @Test
    public void testLongMult1() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("who", "is", "hobs", "hawi", "mosis"), null);
        testLongMultGenerate(1, words);
    }

    @Test
    public void testLongMult2() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("get", "by", "babe", "beare"), null);
        testLongMultGenerate(1, words);
    }

    @Test
    public void testBignumLongMult1() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("8467", "999999983491", "8466999860218297", "7999999867928",
                "3999999933964", "5999999900946", "6999999884437"), null);
        config.setUseBigNum(true);
        testLongMultGenerate(1, words);
        config.setUseBigNum(false);
    }

    @Test
    public void testBignumLongMult2() throws CryptaModelException {
        WordArray words = new WordArray(
                Arrays.asList("8467", "999999983491", "8466999860218297", "25401", "33868", "67736", "76203"), null);
        config.setUseBigNum(true);
        testLongMultGenerate(1, words);
        config.setUseBigNum(false);
    }

}
