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
import org.chocosolver.solver.variables.IntVar;

import cryptator.gen.AbstractCryptaGenModel;

public class CryptaMemberLen extends AbstractCryptaGenModel {

    public final IntVar[] lengths;

    public CryptaMemberLen(final Model m, final String[] words, final String prefix) {
        super(m, words, prefix);
        lengths = buildLengths(m, words, prefix);
    }

    private static IntVar[] buildLengths(final Model m, final String[] words, final String prefix) {
        final int n = words.length;
        IntVar[] vars = new IntVar[n];
        for (int i = 0; i < n; i++) {
            vars[i] = m.intVar(prefix + "len_" + words[i], new int[] {0, words[i].length()});
        }
        return vars;
    }

    @Override
    protected void postWordConstraints() {
        // Nothing to do
    }

    @Override
    protected void postMaxLengthConstraints() {
        for (int i = 0; i < words.length; i++) {
            model.reifyXeqC(lengths[i], words[i].length(), vwords[i]);
        }
        model.max(maxLength, lengths).post();
    }

}
