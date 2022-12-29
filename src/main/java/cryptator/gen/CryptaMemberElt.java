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
import org.chocosolver.solver.variables.IntVar;

public class CryptaMemberElt extends AbstractCryptaGenModel {

    protected final IntVar index;

    public CryptaMemberElt(final Model m, final String[] words, final String prefix) {
        super(m, words, prefix, true);
        index = m.intVar(prefix + "idx", 0, words.length - 1);
    }

    public final IntVar getIndex() {
        return index;
    }

    @Override
    protected void postWordConstraints() {
        model.boolsIntChanneling(vwords, index, 0).post();
    }

    @Override
    protected void postWordCountConstraint() {
        super.postWordCountConstraint();
        postWordCountConstraints(1);
    }

    @Override
    protected void postMaxLengthConstraints() {
        model.element(maxLength, getLengths(words), index).post();
    }

}
