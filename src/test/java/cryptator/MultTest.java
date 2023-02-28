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

import org.junit.Test;

import cryptator.gen.CryptaGenLongMult;
import cryptator.gen.CryptaGenMult;

public class MultTest {

    private void testMultModel(final int expectedSolutionCount, final String[] words) {
        testMultModel(expectedSolutionCount, words, false);
    }

    private void testMultModel(final int expectedSolutionCount, final String[] words, final boolean isDoublyTrue) {
        final CryptaGenMult m = new CryptaGenMult(words);
        m.buildModel();
        m.postPrecisionConstraints(10);
        m.postHeavyConstraints(10);
        if (isDoublyTrue) {
            m.postDoublyTrueConstraints(0);
        }
//        System.out.println(m.getModel());
//        Solution sol = new Solution(m.getModel());
//        m.getSolver().streamSolutions().forEach(s -> {
//            System.out.println(m);
//            // sol.record();
//            // System.out.println(sol);
//        });
//        m.getSolver().printStatistics();
        assertEquals(expectedSolutionCount, m.getSolver().streamSolutions().count());

    }

    private void testGenLongMultModel(final int expectedSolutionCount, final String[] words) {
        final CryptaGenLongMult m = new CryptaGenLongMult(words, 10);
        m.buildModel();
        m.postPrecisionConstraints(10);
        m.postHeavyConstraints(10);
//        System.out.println(m.getModel());
//        Solution sol = new Solution(m.getModel());
//        m.getSolver().streamSolutions().forEach(s -> {
//            System.out.println(m);
//            // sol.record();
//            // System.out.println(sol);
//        });
//        m.getSolver().printStatistics();
        assertEquals(expectedSolutionCount, m.getSolver().streamSolutions().count());

    }

    @Test
    public void testMult1() {
        final String[] words = new String[] {"a", "bb", "ccc"};
        testMultModel(1, words);
    }

    @Test
    public void testMult2() {
        final String[] words = new String[] {"a", "bbb", "ccc"};
        testMultModel(3, words);
    }

    @Test
    public void testMult3() {
        final String[] words = new String[] {"a", "bb", "ccc", "dddd"};
        testMultModel(5, words);
    }

    @Test
    public void testMult4() {
        final String[] words = new String[] {"a", "bb", "ccc", "dddd", "eeeee"};
        testMultModel(17, words);
    }

    @Test
    public void testMultDoublyTrue1() {
        final String[] words = new String[] {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight",
                "nine", "ten", "eleven", "twelve"};
        testMultModel(5, words, true);
    }

    @Test
    public void testLongMult1() {
        final String[] words = new String[] {"see", "so", "emoo", "mess", "mimeo"};
        testGenLongMultModel(22, words);
    }

    @Test
    public void testLongMult2() {
        final String[] words = new String[] {"who", "is", "hobs", "hawi", "mosis"};
        testGenLongMultModel(18, words);
    }

    @Test
    public void testLongMult3() {
        final String[] words = new String[] {"get", "by", "babe", "beare"};
        testGenLongMultModel(4, words);
    }

}
