/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen.pattern;

import java.util.Optional;
import java.util.stream.IntStream;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.nary.alldifferent.conditions.Condition;
import org.chocosolver.solver.expression.discrete.arithmetic.ArExpression;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;

import cryptator.specs.IChocoModel;

/**
 * The Class builds a generation model for long multiplication.
 *
 * It does not inherit nor use members because of the strength of the
 * constraints over the operands.
 */
public class CryptaLongMultModel implements IChocoModel {

    /** The word cardinalities, i.e. the number of distinct characters. */
    private final int[] cards;

    /** The word lengths, i.e. the number of characters. */
    private final int[] lengths;

    /** The model. */
    private final Model model;

    /** The multiplicand index. */
    private final IntVar multiplicand;

    /** The multiplier index. */
    private final IntVar multiplier;

    /** The product index. */
    private final IntVar product;

    /** The term indices. */
    private final IntVar[] terms;

    /** The term presences. */
    private final BoolVar[] presences;

    /** The multiplicand length. */
    private final IntVar mdLength;

    /** The multiplier length. */
    private final IntVar mrLength;

    /** The product length. */
    private final IntVar prLength;

    /** The term lengths. */
    private final IntVar[] termLengths;

    /** The multiplier cardinality, its number of distinct characters. */
    private final IntVar mrCard;

    /**
     * Instantiates a new model for the long multiplication.
     *
     * @param model   the model
     * @param lengths the word lengths
     * @param cards   the word cardinalities
     */
    public CryptaLongMultModel(final Model model, final int[] lengths, final int[] cards) {
        this.model = model;
        this.lengths = lengths;
        this.cards = cards;
        final int n = lengths.length;
        multiplicand = model.intVar("multiplicand", 0, n - 1);
        multiplier = model.intVar("multiplier", 0, n - 1);
        product = model.intVar("product", 0, n - 1);
        terms = model.intVarArray("terms", IntStream.of(lengths).max().orElse(0), 0, n);
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

    /**
     * Gets the multiplicand index.
     *
     * @return the multiplicand index
     */
    public int getMultiplicandIndex() {
        return multiplicand.getValue();
    }

    /**
     * Gets the multiplier index.
     *
     * @return the multiplier index
     */
    public int getMultiplierIndex() {
        return multiplier.getValue();
    }

    /**
     * Gets the product index.
     *
     * @return the product index
     */
    public int getProductIndex() {
        return product.getValue();
    }

    /**
     * Gets the product length.
     *
     * @return the product length
     */
    public IntVar getProductLength() {
        return prLength;
    }

    /**
     * Gets the product.
     *
     * @return the product
     */
    public IntVar getProduct() {
        return product;
    }

    /**
     * Gets the word indices.
     *
     * @return the word indices
     */
    public IntVar[] getWordIndices() {
        return ArrayUtils.append(ArrayUtils.toArray(multiplicand, multiplier, product), terms);
    }

    /**
     * Gets the term indices.
     *
     * @return the term indices
     */
    public int[] getTermIndices() {
        int n = mrCard.getValue();
        int[] indices = new int[n];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = terms[i].getValue();
        }
        return indices;
    }

    private static void ge1(final ArExpression e1, final ArExpression e2) {
        e1.ge(e2).post();
        e1.le(e2.add(1)).post();
    }

    /**
     * Post the product length constraints.
     */
    private void postPRLenConstraints() {
        ge1(prLength, mdLength.add(mrLength).sub(1));
        prLength.ge(termLengths[0].add(mrLength).sub(1)).post();
    }

    /**
     * Post the all different constraint on the word indices.
     */
    private void postAllDifferent() {
        final int n = lengths.length;
        model.allDifferentUnderCondition(ArrayUtils.concat(terms, product), new ExceptN(n), true).post();
        // the last term is inactive.
        terms[terms.length - 1].eq(n).post();
    }

    /**
     * Post the channeling between the term indices and lengths.
     */
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

    /**
     * Post the channeling constraint between the indices and lengths of the
     * operands of the short multiplication.
     */
    private void postMultLenChanneling() {
        model.element(mdLength, lengths, multiplicand).post();
        model.element(mrLength, lengths, multiplier).post();
        model.element(prLength, lengths, product).post();
    }

    /**
     * Post the constraint on the term lengths.
     */
    private void postTermLenConstraints() {
        // FIXME Can be equal to one if the multiplier digit is 0
        for (int i = 0; i < termLengths.length; i++) {
            ge1(termLengths[i], mdLength);
        }
    }

    /**
     * Post symmetry breaking constraint by ordering terms.
     */
    private void postTermOrderingConstraints() {
        for (int i = 0; i < terms.length; i++) {
            model.reifyXneC(terms[i], lengths.length, presences[i]);
        }
        model.decreasing(presences, 0).post();
    }

    /**
     * Post the constraint for counting the terms.
     */
    private void postTermCountConstraints() {
        model.element(mrCard, cards, multiplier).post();
        model.sum("presences", presences).eq(mrCard).post();
    }

    /**
     * Builds the model.
     */
    public void buildModel() {
        postAllDifferent();

        postPRLenConstraints();
        postTermLenConstraints();

        postMultLenChanneling();
        postTermLenChanneling();

        postTermCountConstraints();
        postTermOrderingConstraints();
    }

    private void toString(final StringBuilder b, final IntVar idx, final IntVar len) {
        toString(b, idx, len, Optional.empty());
    }

    private void toString(final StringBuilder b, final IntVar idx, final IntVar len, final Optional<IntVar> card) {
        b.append(idx.getValue()).append("(").append(len.getValue());
        if (card.isPresent()) {
            b.append("-").append(card.get().getValue());
        }
        b.append(") ");
    }

    /**
     * To string.
     *
     * @return the string
     */
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

    /**
     * The Class ExceptN for the all different constraint.
     */
    private static class ExceptN implements Condition {

        /** The dummy index. */
        private final int n;

        /**
         * Instantiates a new condition.
         *
         * @param n the dummy index
         */
        protected ExceptN(final int n) {
            super();
            this.n = n;
        }

        @Override
        public boolean holdOnVar(final IntVar x) {
            return !x.contains(n);
        }

        @Override
        public String toString() {
            return "_except_N";
        }

    }

}
