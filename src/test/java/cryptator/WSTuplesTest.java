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

import org.chocosolver.solver.constraints.extension.Tuples;
import org.junit.Test;

import cryptator.gen.WordSumTuplesBuilder;

public class WSTuplesTest {

    private final int[] b = {2, 4, 8, 10, 12, 16};

    private final WordSumTuplesBuilder builder = new WordSumTuplesBuilder();

    public static int logFloor(double x, double b) {
        return (int) Math.floor(Math.log(x) / Math.log(b));
    }

    @Test
    public void testFloorLog() {
        // Powers of two to avoid numerical errors
        // The test fails with base 10, because of the floating point arithmetic.
        int[] b = {2, 4, 8, 16};
        int n = 1024;
        for (int element : b) {
            for (int x = 1; x < n; x++) {
                // System.out.println(x + " " + b[i] + " " + Math.log(x) / Math.log(b[i]));
                assertEquals(logFloor(x, element), WordSumTuplesBuilder.logFloor(x, element));
            }
        }
    }

    public void testMaxZ(int expected, int[] values, int k) {
        assertEquals(expected, WordSumTuplesBuilder.getMaxZ(values, k));
    }

    @Test
    public void testmaxZ() {
        int[] v1 = new int[] {0};
        testMaxZ(0, v1, 0);

        int[] v2 = new int[] {0, 3, 3, 2, 1};
        testMaxZ(0, v2, 0);
        testMaxZ(0, v2, 1);
        testMaxZ(0, v2, 2);
        testMaxZ(3, v2, 3);
        testMaxZ(6, v2, 4);
    }

    public void testTuples(int expected, int[] values, int base) {
        builder.setBase(base);
        final Tuples tuples = builder.buildTuples(values);
        // System.out.println(tuples);
        assertEquals(expected, tuples.nbTuples());

    }

    @Test
    public void testTuples1() {
        int[] values = {0, 1, 1};
        for (int element : b) {
            testTuples(4, values, element);
        }
    }

    @Test
    public void testTuples2() {
        int[] values = {0, 1, 1, 1};
        for (int element : b) {
            testTuples(11, values, element);
        }
    }

    @Test
    public void testTuples3() {
        int[] values = {0, 1, 1, 1, 1};
        testTuples(22, values, 2);
    }

    @Test
    public void testTuples4() {
        int[] values = {0, 4, 2, 2, 1};
        testTuples(107, values, 2);
    }

}
