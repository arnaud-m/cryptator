/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;

import cryptator.CryptaOperator;
import cryptator.specs.ICryptaGenModel;
import cryptator.specs.ICryptaGenSolver;
import cryptator.specs.ICryptaNode;
import cryptator.tree.CryptaNode;

public class CryptaGenModel extends WordListModel implements ICryptaGenSolver {

    private final CryptaMemberLen left;

    private final ICryptaGenModel right;

    public CryptaGenModel(final String[] words, final boolean lenOrCardModel) {
        super(new Model("Generate"), words);
        left = lenOrCardModel ? new CryptaMemberLen(model, words, "L_") : new CryptaMemberCard(model, words, "L_");
        right = new CryptaMemberElt(model, words, "R_");
    }

    @Override
    public void buildModel() {
        super.buildModel();
        left.buildModel();
        right.buildModel();
        postLeftOrRightConstraints();
        postSymBreakLengthLenConstraint();
    }

    @Override
    protected void postMaxLengthConstraints() {
        model.max(maxLength, left.getMaxLength(), right.getMaxLength()).post();
    }

    private void postLeftOrRightConstraints() {
        final BoolVar[] l = left.getWordVars();
        final BoolVar[] r = right.getWordVars();
        for (int i = 0; i < vwords.length; i++) {
            l[i].add(r[i]).eq(vwords[i]).post();
        }
    }

    private void postSymBreakLengthLenConstraint() {
        left.getMaxLength().le(right.getMaxLength()).post();

    }

    public void postLeftCountConstraints(final int min, final int max) {
        final int min2 = Math.max(min, 2);
        left.getWordCount().ge(min2).post();
        if (max >= min2) {
            left.getWordCount().le(max).post();
        }

    }

    public void postMinLeftCountConstraints(final int base) {
        left.postLentghSumConstraints(right.getMaxLength(), base);
    }

    public void postFixedRightMemberConstraint() {
        final BoolVar[] vars = right.getWordVars();
        vars[vars.length - 1].eq(1).post();
    }

    public void postDoublyTrueConstraint(final int lb) {
        final int n = getN();
        final IntVar sum = model.intVar("SUM", lb, n - 1);

        final IntVar[] lvars = new IntVar[n + 1];
        System.arraycopy(left.getWordVars(), 0, lvars, 0, n);
        lvars[n] = sum;

        final IntVar[] rvars = new IntVar[n + 1];
        System.arraycopy(right.getWordVars(), 0, rvars, 0, n);
        rvars[n] = sum;

        final int[] coeffs = ArrayUtils.array(0, n);
        coeffs[n] = -1;

        model.scalar(lvars, coeffs, "=", 0).post();
        model.scalar(rvars, coeffs, "=", 0).post();
    }

    @Override
    public final ICryptaNode recordCryptarithm() {
        final ICryptaNode l = GenerateUtil.recordAddition(left);
        final ICryptaNode r = GenerateUtil.recordAddition(right);
        return (r == null) || (l == null) ? null : new CryptaNode(CryptaOperator.EQ, l, r);
    }

    @Override
    public String toString() {
        return left.toString() + " = " + right.toString();
    }

}
