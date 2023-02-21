/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen.member;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.extension.Tuples;
import org.chocosolver.solver.variables.IntVar;

import cryptator.gen.AbstractCryptaGenModel;
import cryptator.gen.WordSumTuplesBuilder;

public class CryptaMemberCard extends CryptaMemberLen {

    protected final IntVar[] cardLengths;

    public CryptaMemberCard(final Model m, final String[] words, final String prefix) {
        super(m, words, prefix);
        cardLengths = m.intVarArray(prefix + "cardLen", getMaxLength(words) + 1, 0, words.length);
    }

    public final IntVar[] getCardLength() {
        return cardLengths;
    }

    @Override
    protected void postWordCountConstraint() {
        super.postWordCountConstraint();
        // Remove card of empty/unused words
        IntVar[] vars = Arrays.copyOfRange(cardLengths, 1, cardLengths.length);
        model.sum(vars, "=", wordCount).post();
    }

    private void postGlobalCardLengthConstraint() {
        int[] values = IntStream.range(0, cardLengths.length).toArray();
        model.globalCardinality(lengths, values, cardLengths, true).post();
    }

    @Override
    public void postLentghSumConstraints(final IntVar sumLength, final int base) {

        int[] maxCardLengths = CryptaMemberCard.getLengthCounts(words);
        final int maxCard = IntStream.of(maxCardLengths).max().orElse(0);

        IntVar[] cardLengthsWoZero = Arrays.copyOf(cardLengths, cardLengths.length);
        cardLengthsWoZero[0] = model.intVar(0);

        IntVar x = model.intVar("Xk", 0, maxCard, false);
        model.element(x, cardLengthsWoZero, maxLength, 0).post();

        IntVar y = model.intVar("Yk", 0, maxCard, false);
        model.element(y, cardLengthsWoZero, maxLength.sub(1).intVar(), 0).post();

        IntVar z = model.intVar("Zk", 0, words.length, false);
        z.eq(wordCount.sub(x).sub(y)).decompose().post();

        WordSumTuplesBuilder builder = new WordSumTuplesBuilder(base);
        Tuples tuples = builder.buildTuples(maxCardLengths);

        model.table(new IntVar[] {maxLength, x, y, z, sumLength}, tuples).post();
    }

    @Override
    public void buildModel() {
        super.buildModel();
        postGlobalCardLengthConstraint();
    }

    private static int[] getLengthCounts(final String[] words) {
        final int n = AbstractCryptaGenModel.getMaxLength(words);
        int[] v = new int[n + 1];
        for (String w : words) {
            v[w.length()]++;
        }
        return v;
    }

}
