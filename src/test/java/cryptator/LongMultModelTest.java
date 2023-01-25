/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.exception.InvalidSolutionException;
import org.junit.Assert;
import org.junit.Test;

import cryptator.gen.CryptaLongMultModel;

public class LongMultModelTest {

    public void testLongMultModel(int expectedSolutionCount, int[] lengths) {
        testLongMultModel(expectedSolutionCount, lengths, lengths);
    }

    public void testLongMultModel(int expectedSolutionCount, int[] lengths, int[] cards) {
        CryptaLongMultModel m = new CryptaLongMultModel(new Model(), lengths, cards);
        m.buildModel();
        Assert.assertEquals(expectedSolutionCount, m.getSolver().streamSolutions().count());
//        m.getSolver().streamSolutions().forEach(s -> {
//            System.out.println(s);
//            System.out.println(m);
//        });
//        m.getSolver().printStatistics();
    }

    @Test
    public void testLongMultModel1() {
        testLongMultModel(8, new int[] {2, 2, 3});
    }

    @Test
    public void testLongMultModel2() {
        testLongMultModel(16, new int[] {2, 3, 3, 4});
    }

    public void testLongMultModel3() {
        testLongMultModel(20, new int[] {2, 2, 3, 5});
    }

    // Choco exception fixed in the main branch.
    // Waiting for the next choco release ...
    @Test(expected = InvalidSolutionException.class)
    public void testLongMultModel4() {
        testLongMultModel(18, new int[] {2, 3, 3, 5});
    }

    @Test
    public void testLongMultModel5() {
        testLongMultModel(2, new int[] {2, 3, 3, 6});
    }

    @Test
    public void testLongMultModel6() {
        testLongMultModel(2, new int[] {2, 3, 4, 6});
    }

    // Choco exception fixed in the main branch.
    // Waiting for the next choco release ...
    @Test(expected = InvalidSolutionException.class)
    public void testLongMultModel7() {
        testLongMultModel(-1, new int[] {4, 3, 3, 4, 7});
    }

    @Test
    public void testLongMultModel8() {
        testLongMultModel(13, new int[] {2, 3, 3, 4}, new int[] {1, 2, 2, 1});
    }

}
