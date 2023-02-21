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
import org.chocosolver.solver.expression.discrete.arithmetic.ArExpression;
import org.chocosolver.solver.variables.IntVar;

import cryptator.solver.AdaptiveSolver;
import cryptator.specs.ICryptaNode;

class CryptaMemberMult extends CryptaMemberPair {

    private final IntVar sumL;

    private final IntVar sumR;

    public CryptaMemberMult(Model model, String[] words, String prefix) {
        super(new CryptaMemberLen(model, words, prefix + "L_"), new CryptaMemberLen(model, words, prefix + "R_"));
        final int ub = AbstractCryptaGenModel.getSumLength(words);
        this.sumL = model.intVar("L_sumLength", 0, ub);
        this.sumR = model.intVar("R_sumLength", 0, ub);

    }

    @Override
    public void buildModel() {
        super.buildModel();
        left.getWordCount().ge(1).post();
        right.getWordCount().ge(1).post();
    }

    @Override
    public ICryptaNode recordCryptarithm() {
        return GenerateUtil.recordMultiplication(left, right);
    }

    @Override
    protected void postSymBreakLengthConstraint() {
        getModel().lexLess(left.getWordVars(), right.getWordVars()).post();
    }

    public void postMultPrecisionConstraints(int base) {
        getModel().sum(left.lengths, "=", sumL).post();
        getModel().sum(((CryptaMemberLen) right).lengths, "=", sumR).post();
        final int thresh = AdaptiveSolver.computeThreshold(base) + 1;
        sumL.le(thresh).post();
        sumR.le(thresh).post();
    }

    public void postMultHeavyConstraints(int base) {
        final ArExpression minL = sumL.sub(left.getWordCount()).add(1);
        final ArExpression minR = sumR.sub(right.getWordCount()).add(1);
        minL.le(sumR).post();
        minR.le(sumL).post();
    }

}

public class CryptaGenMult extends AbstractCryptaListModel {

    private final CryptaMemberMult multiplication;

    public CryptaGenMult(final String[] words) {
        super(new Model("Generate-Multiplication"), words);
        multiplication = new CryptaMemberMult(model, words, "");
    }

    @Override
    public void buildModel() {
        super.buildModel();
        multiplication.buildModel();
    }

    @Override
    protected void postMaxLengthConstraints() {
        multiplication.postMaxLengthConstraint(maxLength);
    }

    @Override
    protected void postWordConstraints() {
        multiplication.postDisjunctionConstraints(vwords);
    }

    public void postHeavyConstraints(final int base) {
        multiplication.postMultHeavyConstraints(base);
    }

    public void postFixedRightMemberConstraints() {
        multiplication.postFixedRightMemberConstraints();
    }

    private int getDoublyCoeff(int i) {
        double coeff = Math.log(i) / Math.log((getN() - 1));
        return (int) Math.round(1000 * coeff);

    }

    public void postDoublyTrueConstraints(final int lb) {
        final int n = getN();
        final IntVar sumL = model.intVar("L_SUMLOG", getDoublyCoeff(lb), getDoublyCoeff(n - 1));
        final IntVar sumR = model.intVar("R_SUMLOG", getDoublyCoeff(lb), getDoublyCoeff(n - 1));

        final IntVar[] lvars = new IntVar[n + 1];
        System.arraycopy(multiplication.getLeft().getWordVars(), 0, lvars, 0, n);
        lvars[n] = sumL;

        final IntVar[] rvars = new IntVar[n + 1];
        System.arraycopy(multiplication.getRight().getWordVars(), 0, rvars, 0, n);
        rvars[n] = sumR;

        final int[] coeffs = new int[n + 1];
        coeffs[0] = IntVar.MIN_INT_BOUND;
        for (int i = 1; i < n; i++) {
            coeffs[i] = getDoublyCoeff(i);
        }
        coeffs[n] = -1;

        model.scalar(lvars, coeffs, "=", 0).post();
        model.scalar(rvars, coeffs, "=", 0).post();

        sumL.sub(sumR).abs().mul(2).le(wordCount).post();
    }

    @Override
    public void postPrecisionConstraints(int base) {
        final int thresh = AdaptiveSolver.computeThreshold(base);
        getMaxLength().le(thresh).post();
        multiplication.postMultPrecisionConstraints(base);
    }

    @Override
    public final ICryptaNode recordCryptarithm() {
        return multiplication.recordCryptarithm();
    }

    @Override
    public String toString() {
        return GenerateUtil.recordString(multiplication.getLeft(), " * ") + " = "
                + GenerateUtil.recordString(multiplication.getRight(), " * ");
    }

}
