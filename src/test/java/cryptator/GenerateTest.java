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

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import cryptator.cmd.CryptaBiConsumer;
import cryptator.cmd.WordArray;
import cryptator.config.CryptaCmdConfig.SolverType;
import cryptator.config.CryptagenConfig;
import cryptator.config.CryptagenConfig.GenerateType;
import cryptator.config.CryptagenConfig.RightMemberType;
import cryptator.gen.CryptaListGenerator;
import cryptator.solver.CryptaModelException;

public class GenerateTest {

    final CryptagenConfig config = new CryptagenConfig();

    @BeforeClass
    public static void configureTestLoggers() {
        JULogUtil.configureSilentLoggers();
    }

    @Before
    public void configureDefault() {
//        JULogUtil.configureTestLoggers();
        config.setArithmeticBase(10);
        config.setGridSize(3);
        config.setLightModel(false);
        config.setSolverType(SolverType.SCALAR);
        config.setRightMemberType(RightMemberType.UNIQUE);
        config.setGenerateType(GenerateType.ADD);
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
        testGenerate(expectedSolCount, OptionalInt.empty(), wordArray);
    }

    private void testGenerateLH(final int expectedSolCount, final OptionalInt expectedCandCount,
            final WordArray wordArray) throws CryptaModelException {
        config.setLightModel(false);
        testGenerate(expectedSolCount, expectedCandCount, wordArray);
        config.setLightModel(true);
        testGenerate(expectedSolCount, expectedCandCount, wordArray);
    }

    private void testGenerateLH(final int expectedSolCount, final WordArray wordArray) throws CryptaModelException {
        testGenerateLH(expectedSolCount, OptionalInt.empty(), wordArray);
    }

    private void testGenerateBLH(final int expectedSolCount, final OptionalInt expectedCandCount,
            final WordArray wordArray) throws CryptaModelException {
        config.setSolverType(SolverType.SCALAR);
        testGenerateLH(expectedSolCount, expectedCandCount, wordArray);
        config.setSolverType(SolverType.BIGNUM);
        testGenerateLH(expectedSolCount, expectedCandCount, wordArray);
    }

    private void testGenerateBLH(final int expectedSolCount, final WordArray wordArray) throws CryptaModelException {
        testGenerateBLH(expectedSolCount, OptionalInt.empty(), wordArray);
    }

    private void testMultGenerateBLH(final int expectedSolCount, final WordArray wordArray)
            throws CryptaModelException {
        config.setRightMemberType(RightMemberType.FREE);
        config.setGenerateType(GenerateType.MUL);
        testGenerateBLH(expectedSolCount, OptionalInt.empty(), wordArray);
    }

    @Test
    public void testSendMoreMoney() throws CryptaModelException {
        final WordArray words = new WordArray(Arrays.asList("send", "more", "money"), null);
        testGenerate(1, OptionalInt.of(1), words);
    }

    @Test
    public void testSendMuchMoreMoney() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("send", "much", "more", "money"), null);
        config.setLightModel(true);
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
        config.setRightMemberType(RightMemberType.FIXED);
        testGenerate(1, words, 0);
    }

    @Test
    public void testAbcde() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("a", "bb", "ccc", "dddd", "eeeee"), null);
        testGenerate(0, OptionalInt.of(11), words);
    }

    @Test
    public void testAbcdef() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("a", "bb", "ccc", "dddd", "eeeee"), "ffffff");
        config.setRightMemberType(RightMemberType.FIXED);
        testGenerate(0, OptionalInt.of(15), words);
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
        config.setGenerateType(GenerateType.CROSS);
        testGenerate(0, OptionalInt.empty(), words);
    }

    @Test
    public void testCrossword2() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("A", "B", "CD", "CE", "F", "CF", "CA", "CC", "DE"), null);
        config.setGenerateType(GenerateType.CROSS);
        testGenerate(2, OptionalInt.empty(), words);
    }

    @Ignore("Take too long")
    @Test
    public void testCrossword3() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("HJ", "AD", "DF", "BG", "EC", "DF", "AC", "GE", "HEK"), null);
        config.setGenerateType(GenerateType.CROSS);
        testGenerate(2, OptionalInt.empty(), words);
    }

    @Test
    public void testMult1() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("mad", "man", "asylum"), null);
        testMultGenerateBLH(0, words);
    }

    @Test
    public void testMult2() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("alfred", "e", "neuman"), null);
        config.setArithmeticBase(9);
        testMultGenerateBLH(2, words);
    }

    @Test
    public void testMult3() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("nora", "l", "aron"), null);
        testMultGenerateBLH(2, words);
    }

    @Test
    public void testMult4() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("ba", "cba", "dcba"), null);
        testMultGenerateBLH(1, words);
    }

    @Test
    public void testMult5() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("north", "south", "east", "west"), null);
        config.setRightMemberType(RightMemberType.FREE);
        config.setGenerateType(GenerateType.MUL);
        testGenerateLH(2, words);
        // Takes too long with the bignum model
    }

    @Test
    public void testBignumMult1() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("1002527", "1002553", "1005086451431"), null);
        config.setRightMemberType(RightMemberType.FREE);
        config.setGenerateType(GenerateType.MUL);
        config.setSolverType(SolverType.BIGNUM);
        testGenerateLH(1, words);
    }

    @Test
    public void testBignumMult2() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("1000721", "1000541", "1001262390061"), null);
        config.setRightMemberType(RightMemberType.FREE);
        config.setGenerateType(GenerateType.MUL);
        config.setSolverType(SolverType.BIGNUM);
        testGenerateLH(1, words);
    }

    @Ignore("Takes too long.")
    @Test
    public void testBignumMult3() throws CryptaModelException {
        WordArray words = new WordArray(
                Arrays.asList("1002527", "1002553", "1005086451431", "1000721", "1000541", "1001262390061"), null);
        config.setRightMemberType(RightMemberType.FREE);
        config.setGenerateType(GenerateType.MUL);
        config.setSolverType(SolverType.BIGNUM);
        testGenerateLH(1, words);
    }

    @Test
    public void testLongMult1() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("who", "is", "hobs", "hawi", "mosis"), null);
        config.setGenerateType(GenerateType.LMUL);
        testGenerateBLH(1, words);
    }

    @Test
    public void testLongMult2() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("get", "by", "babe", "beare"), null);
        config.setGenerateType(GenerateType.LMUL);
        testGenerateBLH(1, words);
    }

    @Test
    public void testBignumLongMult1() throws CryptaModelException {
        WordArray words = new WordArray(Arrays.asList("8467", "999999983491", "8466999860218297", "7999999867928",
                "3999999933964", "5999999900946", "6999999884437"), null);
        config.setGenerateType(GenerateType.LMUL);
        config.setSolverType(SolverType.BIGNUM);
        testGenerateLH(1, words);
    }

    @Test
    public void testBignumLongMult2() throws CryptaModelException {
        WordArray words = new WordArray(
                Arrays.asList("8467", "999999983491", "8466999860218297", "25401", "33868", "67736", "76203"), null);
        config.setGenerateType(GenerateType.LMUL);
        config.setSolverType(SolverType.BIGNUM);
        testGenerateLH(1, words);
    }

}
