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

import org.chocosolver.solver.constraints.extension.Tuples;
import org.junit.Test;

import cryptator.gen.WordSumTuplesBuilder;

public class WSTuplesTest {

    private final int[] bases = {2, 4, 8, 10, 12, 16};

    protected void testTuples(final int expected, final int base, final int[] values) {
        WordSumTuplesBuilder builder = new WordSumTuplesBuilder(base, values);
        final Tuples tuples = builder.buildTuples();
        assertEquals(expected, tuples.nbTuples());
    }

    @Test
    public void testTuples1() {
        int[] values = {1, 2, 2};
        for (int b : bases) {
            testTuples(3, b, values);
        }
    }

    @Test
    public void testTuples2() {
        int[] values = {1, 2, 3};
        for (int base : bases) {
            testTuples(2, base, values);
        }
    }

    @Test
    public void testTuples3() {
        int[] values = {1, 2, 3, 4};
        for (int base : bases) {
            testTuples(8, base, values);
        }
    }

    @Test
    public void testTuples4() {
        int[] values = {1, 8, 9, 16};
        for (int base : bases) {
            testTuples(2, base, values);
        }
    }

    @Test
    public void testTuples5() {
        int[] values = {1, 2, 8, 9, 10, 16};
        for (int base : bases) {
            testTuples(18, base, values);
        }
    }

}
