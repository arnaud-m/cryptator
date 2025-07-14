/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;

import cryptator.gen.member.CryptaMemberPair;
import cryptator.specs.ICryptaNode;

public class CryptaGenAdd extends AbstractCryptaListModel {

    private final CryptaMemberPair addition;

    public CryptaGenAdd(final String[] words, final boolean isRightUnique) {
        super(new Model("Generate-Addition"), words);
        addition = new CryptaMemberPair(model, words, "", isRightUnique);
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
        addition.postFixedRightMemberConstraints();
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
    public final ICryptaNode recordCryptarithm() {
        return addition.recordCryptarithm();
    }

    @Override
    public String toString() {
        return GenerateUtil.recordString(addition.getLeft(), " + ") + " = "
                + GenerateUtil.recordString(addition.getRight(), " + ");
    }

}
