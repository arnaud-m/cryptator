/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;

import cryptator.specs.ICryptaGenSolver;
import cryptator.specs.ICryptaNode;

class CryptaMemberPair implements ICryptaGenSolver {

    protected final CryptaMemberLen left;

    protected final AbstractCryptaGenModel right;

    protected CryptaMemberPair(CryptaMemberLen left, CryptaMemberLen right) {
        super();
        this.left = left;
        this.right = right;
    }

    public CryptaMemberPair(final Model model, final String[] words, final String prefix, boolean useMemberLen) {
        super();
        left = buildLeftMember(model, words, prefix, useMemberLen);
        right = new CryptaMemberElt(model, words, prefix + "R_");
    }

    public CryptaMemberPair(final IntVar index, final String[] words, final String prefix, boolean useMemberLen) {
        super();
        left = buildLeftMember(index.getModel(), words, prefix, useMemberLen);
        right = new CryptaMemberElt(index, words, prefix + "R_");
    }

    private static final CryptaMemberLen buildLeftMember(final Model model, final String[] words, final String prefix,
            boolean useMemberLen) {
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

    protected final void postDisjunctionConstraints(final BoolVar[] v) {
        final BoolVar[] l = getLeft().getWordVars();
        final BoolVar[] r = getRight().getWordVars();
        for (int i = 0; i < v.length; i++) {
            l[i].add(r[i]).eq(v[i]).post();
        }
    }

    protected void postMaxLengthConstraint(final IntVar maxLen) {
        getModel().max(maxLen, getLeft().getMaxLength(), getRight().getMaxLength()).post();

    }

    public final void postHeavyConstraints(final int base) {
        left.postLentghSumConstraints(right.getMaxLength(), base);
    }

    @Override
    public ICryptaNode recordCryptarithm() {
        return GenerateUtil.recordAddition(left, right);
    }

}

public class CryptaGenAdd extends AbstractCryptaListModel implements ICryptaGenSolver {

    private final CryptaMemberPair addition;

    public CryptaGenAdd(final String[] words, final boolean useMemberLen) {
        super(new Model("Generate-Addition"), words);
        addition = new CryptaMemberPair(model, words, "", useMemberLen);
    }

    @Override
    public void buildModel() {
        super.buildModel();
        addition.buildModel();
    }

    @Override
    protected void postMaxLengthConstraints() {
        addition.postMaxLengthConstraint(maxLength);
    }

    @Override
    protected void postWordConstraints() {
        addition.postDisjunctionConstraints(vwords);
    }

    public void postHeavyConstraints(final int base) {
        addition.postHeavyConstraints(base);
    }

    public void postFixedRightMemberConstraints() {
        final BoolVar[] vars = addition.getRight().getWordVars();
        vars[vars.length - 1].eq(1).post();
    }

    public void postDoublyTrueConstraints(final int lb) {
        final int n = getN();
        final IntVar sum = model.intVar("SUM", lb, n - 1);

        final IntVar[] lvars = new IntVar[n + 1];
        System.arraycopy(addition.getLeft().getWordVars(), 0, lvars, 0, n);
        lvars[n] = sum;

        final IntVar[] rvars = new IntVar[n + 1];
        System.arraycopy(addition.getRight().getWordVars(), 0, rvars, 0, n);
        rvars[n] = sum;

        final int[] coeffs = ArrayUtils.array(0, n);
        coeffs[n] = -1;

        model.scalar(lvars, coeffs, "=", 0).post();
        model.scalar(rvars, coeffs, "=", 0).post();
    }

    @Override
    public void postPrecisionConstraints(int base) {
        // Nothing to do.
    }

    @Override
    public final ICryptaNode recordCryptarithm() {
        return addition.recordCryptarithm();
    }

    @Override
    public String toString() {
        return GenerateUtil.recordString(addition.getLeft(), " + ") + " = "
                + GenerateUtil.recordString(addition.getRight(), " + ");
    }

}
