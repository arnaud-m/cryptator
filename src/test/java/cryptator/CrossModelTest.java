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

import org.junit.Ignore;
import org.junit.Test;

import cryptator.gen.CryptaGenCrossword;

public class CrossModelTest {

    private void testCrosswordModel(int size, String[] words, int expectedSolutionCount) {
        final CryptaGenCrossword m = new CryptaGenCrossword(size, words);
        m.buildModel();
        // System.out.println(m.getModel());
        assertEquals(expectedSolutionCount, m.getModel().getSolver().streamSolutions().count());
    }

    private void testHeavyCrosswordModel(int size, String[] words, int expectedSolutionCount) {
        final CryptaGenCrossword m = new CryptaGenCrossword(size, words);
        m.buildModel();
        m.postHeavyConstraints(10);
        // System.out.println(m.getModel());
        assertEquals(expectedSolutionCount, m.getModel().getSolver().streamSolutions().count());
        // m.getSolver().streamSolutions().forEach(s -> System.out.println(m));
    }

    @Test
    public void testCrosswordN3W9() {
        final String[] words = new String[] {"a", "b", "c", "d", "e", "f", "h", "i", "j"};
        testCrosswordModel(3, words, 60480);
    }

    @Ignore("Take too long")
    @Test
    public void testCrosswordN3W10() {
        final String[] words = new String[] {"a", "b", "c", "d", "e", "f", "h", "i", "j", "k"};
        testCrosswordModel(3, words, 604800);
    }

    @Test
    public void testHeavyCrosswordN3W10() {
        final String[] words = new String[] {"a", "b", "cc", "ddd", "eeee", "fffff", "gggggg", "hhhhhh", "iiiiiii"};
        testHeavyCrosswordModel(3, words, 4);
    }

    @Test
    public void testHeavyCrosswordN3W9() {
        final String[] words = new String[] {"A", "B", "F", "CA", "CC", "CD", "CE", "CF", "DE"};
        testHeavyCrosswordModel(3, words, 5040);
    }

}
