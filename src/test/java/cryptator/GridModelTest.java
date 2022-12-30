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

import org.chocosolver.solver.Model;
import org.junit.Test;

import cryptator.gen.CryptaGridModel;

public class GridModelTest {

    private void testGridModel(int size, int words, int expectedSolutionCount) {
        CryptaGridModel m = new CryptaGridModel(new Model(), size, words);
        m.buildModel();
        assertEquals(expectedSolutionCount, m.getModel().getSolver().streamSolutions().count());
    }

    @Test
    public void testGridModel1() {
        testGridModel(1, 1, 1);
        testGridModel(1, 2, 2);

    }

    @Test
    public void testGridModel2() {
        testGridModel(2, 3, 0);
        testGridModel(2, 4, 12);
        testGridModel(2, 5, 60);
    }

    @Test
    public void testGridModel3() {
        testGridModel(3, 8, 0);
        testGridModel(3, 9, 60480);
        testGridModel(3, 10, 604800);
    }

}
