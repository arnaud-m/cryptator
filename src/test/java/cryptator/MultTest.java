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

import cryptator.gen.CryptaGenMult;

public class MultTest {

    private void testMultModel(String[] words, int expectedSolutionCount) {
        testMultModel(false, words, expectedSolutionCount);
    }

    private void testMultModel(boolean isDoublyTrue, String[] words, int expectedSolutionCount) {
        final CryptaGenMult m = new CryptaGenMult(words);
        m.buildModel();
        m.postMinLeftCountConstraints(10);
        if (isDoublyTrue)
            m.postDoublyTrueConstraint(0);
        // System.out.println(m.getModel());
        // assertEquals(expectedSolutionCount,
        // m.getModel().getSolver().streamSolutions().count());
        // Solution sol = new Solution(m.getModel());
        m.getSolver().streamSolutions().forEach(s -> {
            // System.out.println(m);
            // sol.record();
            // System.out.println(sol);
        });
        // m.getSolver().printStatistics();
        assertEquals(expectedSolutionCount, m.getSolver().getSolutionCount());
        // m.getModel().getSolver().streamSolutions().count());
    }

    @Test
    public void testMult1() {
        final String[] words = new String[] {"a", "bb", "ccc"};
        testMultModel(words, 1);
    }

    @Test
    public void testMult2() {
        final String[] words = new String[] {"a", "bbb", "ccc"};
        testMultModel(words, 4);
    }

    @Test
    public void testMult3() {
        final String[] words = new String[] {"a", "bb", "ccc", "dddd"};
        testMultModel(words, 5);
    }

    @Test
    public void testMult4() {
        final String[] words = new String[] {"a", "bb", "ccc", "dddd", "eeeee"};
        testMultModel(words, 17);
    }

    @Test
    public void testMultDoublyTrue1() {
        final String[] words = new String[] {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight",
                "nine", "ten", "eleven", "twelve"};
        testMultModel(true, words, 5);
    }

}
