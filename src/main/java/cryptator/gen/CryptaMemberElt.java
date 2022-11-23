/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen;

import java.util.Arrays;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class CryptaMemberElt extends CryptaGenBaseModel {

    protected final IntVar index;

    public CryptaMemberElt(final Model m, final String[] words, final String prefix) {
        super(m, words, prefix, true);
        index = m.intVar(prefix + "idx", 0, words.length - 1);
    }

    @Override
    protected void postWordCountConstraint() {
        super.postWordCountConstraint();
        model.boolsIntChanneling(getWordVars(), index, 0).post();
        wordCount.eq(1).decompose().post();

    }

    @Override
    protected void postMaxLengthConstraints() {
        int[] lengths = Arrays.stream(words).mapToInt(String::length).toArray();
        model.element(maxLength, lengths, index).post();
    }

}
