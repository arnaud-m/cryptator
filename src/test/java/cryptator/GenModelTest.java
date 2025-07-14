/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import static org.junit.Assert.assertEquals;

import org.chocosolver.solver.Model;
import org.junit.Before;
import org.junit.Test;

import cryptator.gen.AbstractCryptaListModel;
import cryptator.gen.member.CryptaMemberElt;
import cryptator.gen.member.CryptaMemberLen;
import cryptator.specs.ICryptaGenModel;
import cryptator.specs.ICryptaNode;

class MockWordListModel extends AbstractCryptaListModel {

    public MockWordListModel(final Model model, final String[] words) {
        super(model, words);
    }

    @Override
    protected void postWordConstraints() {
        // Nothing to do
    }

    @Override
    protected void postMaxLengthConstraints() {
        maxLength.eq(0).decompose().post();
    }

    @Override
    public void postFixedRightMemberConstraints() {
        // Nothing to do.
    }

    @Override
    public void postDoublyTrueConstraints(final int lowerBound) {
        // Nothing to do.
    }

    @Override
    public void postHeavyConstraints(final int base) {
        // Nothing to do.
    }

    @Override
    public ICryptaNode recordCryptarithm() {
        return null;
    }

}

public class GenModelTest {

    private final String[] words = new String[] {"a", "b", "ba", "bb", "baa", "bab", "bba", "bbb", "baaa"};

    private ICryptaGenModel[] models;

    @Before
    public void buildGenModels() {
        models = new ICryptaGenModel[] {new CryptaMemberLen(new Model(), words, ""),
                new MockWordListModel(new Model(), words)};
        for (ICryptaGenModel m : models) {
            m.buildModel();
        }
    }

    private void postMaxWordCountConstraint(final ICryptaGenModel model, final int maxWordCount) {
        model.getWordCount().le(maxWordCount).post();
    }

    private void testGenModels(final int expectedSolutionCount, final int maxWordCount) {
        for (ICryptaGenModel m : models) {
            postMaxWordCountConstraint(m, maxWordCount);
            testGenModel(m, expectedSolutionCount);
        }
    }

    private void testGenModel(final ICryptaGenModel model, final int expectedSolutionCount) {
        assertEquals(expectedSolutionCount, model.getSolver().streamSolutions().count());
    }

    @Test
    public void testGenModels1() {
        testGenModels(10, 1);
    }

    @Test
    public void testGenModels2() {
        testGenModels(46, 2);
    }

    @Test
    public void testGenModels3() {
        testGenModels(130, 3);
    }

    @Test
    public void testWordListModel() {
        MockWordListModel m = new MockWordListModel(new Model(), words);
        m.buildModel();
        m.postMaxSymbolCountConstraint(1);
        postMaxWordCountConstraint(m, 2);
        testGenModel(m, 8);
    }

    @Test
    public void testMemberElt() {
        CryptaMemberElt m = new CryptaMemberElt(new Model(), words, "");
        m.buildModel();
        testGenModel(m, 9);
    }

}
