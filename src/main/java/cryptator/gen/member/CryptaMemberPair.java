/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen.member;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import cryptator.gen.AbstractCryptaGenModel;
import cryptator.gen.GenerateUtil;
import cryptator.specs.ICryptaGenSolver;
import cryptator.specs.ICryptaNode;

public class CryptaMemberPair implements ICryptaGenSolver {

    protected final CryptaMemberLen left;

    protected final AbstractCryptaGenModel right;

    protected CryptaMemberPair(final CryptaMemberLen left, final CryptaMemberLen right) {
        super();
        this.left = left;
        this.right = right;
    }

    public CryptaMemberPair(final Model model, final String[] words, final String prefix, final boolean useMemberLen) {
        super();
        left = buildLeftMember(model, words, prefix, useMemberLen);
        right = new CryptaMemberElt(model, words, prefix + "R_");
    }

    public CryptaMemberPair(final IntVar index, final String[] words, final String prefix, final boolean useMemberLen) {
        super();
        left = buildLeftMember(index.getModel(), words, prefix, useMemberLen);
        right = new CryptaMemberElt(index, words, prefix + "R_");
    }

    private static final CryptaMemberLen buildLeftMember(final Model model, final String[] words, final String prefix,
            final boolean useMemberLen) {
        return useMemberLen ? new CryptaMemberLen(model, words, prefix + "L_")
                : new CryptaMemberCard(model, words, prefix + "L_");
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

    protected void postSymBreakLengthConstraint() {
        left.getMaxLength().le(right.getMaxLength()).post();
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
        left.postLentghSumConstraints(right.getMaxLength(), base);
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
