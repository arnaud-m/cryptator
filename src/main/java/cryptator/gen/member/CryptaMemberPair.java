/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen.member;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;

import cryptator.gen.AbstractCryptaGenModel;
import cryptator.gen.GenerateUtil;
import cryptator.gen.WordSumTuplesBuilder;
import cryptator.specs.ICryptaGenSolver;
import cryptator.specs.ICryptaNode;

public class CryptaMemberPair implements ICryptaGenSolver {

    protected final CryptaMemberLen left;

    protected final AbstractCryptaGenModel right;

    public CryptaMemberPair(final Model model, final String[] words, final String prefix, final boolean isRightUnique) {
        super();
        left = new CryptaMemberLen(model, words, prefix + "L_");
        right = isRightUnique ? new CryptaMemberElt(model, words, prefix + "R_")
                : new CryptaMemberLen(model, words, prefix + "R_");
    }

    public CryptaMemberPair(final IntVar index, final String[] words, final String prefix) {
        super();
        left = new CryptaMemberLen(index.getModel(), words, prefix + "L_");
        right = new CryptaMemberElt(index, words, prefix + "R_");
    }

    @Override
    public final Model getModel() {
        return left.getModel();
    }

    public final CryptaMemberLen getLeft() {
        return left;
    }

    public final AbstractCryptaGenModel getRight() {
        return right;
    }

    public void buildModel() {
        left.buildModel();
        right.buildModel();
        postSymBreakLengthConstraint();
    }

    private void postSymBreakLengthConstraint() {
        if (right instanceof CryptaMemberLen) {
            getModel().lexLess(left.getWordVars(), right.getWordVars()).post();
        } else {
            left.getMaxLength().le(right.getMaxLength()).post();
        }
    }

    public final void postDisjunctionConstraints(final BoolVar[] v) {
        final BoolVar[] l = getLeft().getWordVars();
        final BoolVar[] r = getRight().getWordVars();
        for (int i = 0; i < v.length; i++) {
            l[i].add(r[i]).eq(v[i]).post();
        }
    }

    public void postMaxLengthConstraint(final IntVar maxLen) {
        getModel().max(maxLen, getLeft().getMaxLength(), getRight().getMaxLength()).post();
    }

    public final void postHeavyConstraints(final int base) {
        int[] lengths = AbstractCryptaGenModel.getLengths(left.getWords());
        WordSumTuplesBuilder builder = new WordSumTuplesBuilder(base, lengths);
        IntVar[] vars = ArrayUtils.toArray(left.getMaxLength(), left.getWordCount(), right.getMaxLength(),
                right.getWordCount());
        getModel().table(vars, builder.buildTuples()).post();
    }

    public void postFixedRightMemberConstraints() {
        final BoolVar[] vars = right.getWordVars();
        vars[vars.length - 1].eq(1).post();
        right.getWordCount().eq(1).post();
    }

    @Override
    public ICryptaNode recordCryptarithm() {
        return GenerateUtil.recordAddition(left, right);
    }

}
