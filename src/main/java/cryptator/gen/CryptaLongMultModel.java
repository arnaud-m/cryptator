/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen;

import java.util.Optional;
import java.util.stream.IntStream;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.nary.alldifferent.conditions.Condition;
import org.chocosolver.solver.expression.discrete.arithmetic.ArExpression;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;

import cryptator.specs.IChocoModel;

public class CryptaLongMultModel implements IChocoModel {

    private final int[] cards;

    private final int[] lengths;

    private final Model model;

    private final IntVar multiplicand;

    private final IntVar multiplier;

    private final IntVar product;

    private final IntVar[] terms;

    private final BoolVar[] presences;

    private final IntVar mdLength;

    private final IntVar mrLength;

    private final IntVar prLength;

    private final IntVar[] termLengths;

    private final IntVar mrCard;

    private int maxProductLength = 5;

    public CryptaLongMultModel(Model model, final int[] lengths, final int[] cards) {
        this.model = model;
        this.lengths = lengths;
        this.cards = cards;
        final int n = lengths.length;
        multiplicand = model.intVar("multiplicand", 0, n - 1);
        multiplier = model.intVar("multiplier", 0, n - 1);
        product = model.intVar("product", 0, n - 1);
        terms = model.intVarArray("terms", maxProductLength + 1, 0, n);
        presences = model.boolVarArray("presences", terms.length);

        final int maxLength = IntStream.of(lengths).max().orElse(0);
        mdLength = model.intVar("mdLen", 0, maxLength);
        mrLength = model.intVar("mrLen", 0, maxLength);
        prLength = model.intVar("prLen", 0, maxLength);
        termLengths = model.intVarArray("termLen", terms.length, 0, maxLength);
        mrCard = model.intVar("mrCard", 0, maxLength);
    }

    @Override
    public Model getModel() {
        return model;
    }

    public int getMultiplicandIndex() {
        return multiplicand.getValue();
    }

    public int getMultiplierIndex() {
        return multiplier.getValue();
    }

    public int getProductIndex() {
        return product.getValue();
    }

    public IntVar getProductLength() {
        return prLength;
    }

    public IntVar getProduct() {
        return product;
    }

    public IntVar[] getWordIndices() {
        return ArrayUtils.append(ArrayUtils.toArray(multiplicand, multiplier, product), terms);
    }

    public int[] getTermIndices() {
        int n = mrCard.getValue();
        int[] indices = new int[n];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = terms[i].getValue();
        }
        return indices;
    }

    private static void ge1(ArExpression e1, ArExpression e2) {
        e1.ge(e2).post();
        e1.le(e2.add(1)).post();
    }

    private void postPRLenConstraints() {
        ge1(prLength, mdLength.add(mrLength).sub(1));
        prLength.ge(termLengths[0].add(mrLength).sub(1)).post();
    }

    private void postAllDifferent() {
        final int n = lengths.length;
        model.allDifferentUnderCondition(ArrayUtils.concat(terms, product), new ExceptN(n), true).post();
        // the last term is inactive.
        terms[maxProductLength].eq(n).post();
    }

    private void postTermLenChanneling() {
        final int n = lengths.length;
        final IntVar[] vars = new IntVar[n + 1];
        for (int i = 0; i < n; i++) {
            vars[i] = model.intVar(lengths[i]);
        }
        vars[n] = mdLength;

        for (int i = 0; i < terms.length; i++) {
            model.element(termLengths[i], vars, terms[i], 0).post();
        }
    }

    private void postMultLenChanneling() {
        model.element(mdLength, lengths, multiplicand).post();
        model.element(mrLength, lengths, multiplier).post();
        model.element(prLength, lengths, product).post();
    }

    private void postTermLenConstraints() {
        for (int i = 0; i < termLengths.length; i++) {
            ge1(termLengths[i], mdLength);
        }
    }

    private void postTermOrderingConstraints() {
        for (int i = 0; i < terms.length; i++) {
            model.reifyXneC(terms[i], lengths.length, presences[i]);
        }
        model.decreasing(presences, 0).post();
    }

    private void postTermCountConstraints() {
        model.element(mrCard, cards, multiplier).post();
        model.sum("presences", presences).eq(mrCard).post();
    }

    public void buildModel() {
        postAllDifferent();

        postPRLenConstraints();
        postTermLenConstraints();

        postMultLenChanneling();
        postTermLenChanneling();

        postTermCountConstraints();
        postTermOrderingConstraints();
    }

    private void toString(StringBuilder b, IntVar idx, IntVar len) {
        toString(b, idx, len, Optional.empty());
    }

    private void toString(StringBuilder b, IntVar idx, IntVar len, Optional<IntVar> card) {
        b.append(idx.getValue()).append("(").append(len.getValue());
        if (card.isPresent())
            b.append("-").append(card.get().getValue());
        b.append(") ");
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        toString(b, multiplicand, mdLength);
        toString(b, multiplier, mrLength, Optional.of(mrCard));
        b.append("| ");
        int i = mrCard.getValue();
        while (i > 0) {
            i--;
            toString(b, terms[i], termLengths[i]);

        }
        b.append("| ");
        toString(b, product, prLength);
        return b.toString();
    }

    private static class ExceptN implements Condition {

        private final int n;

        protected ExceptN(int n) {
            super();
            this.n = n;
        }

        @Override
        public boolean holdOnVar(IntVar x) {
            return !x.contains(n);
        }

        @Override
        public String toString() {
            return "_except_N";
        }

    }

}
