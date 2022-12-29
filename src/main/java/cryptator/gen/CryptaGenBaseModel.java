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

/**
 * Base class for generation model.
 */
public abstract class CryptaGenBaseModel extends AbstractCryptaGenModel {

    protected CryptaGenBaseModel(final Model model, final String[] words, final String prefix,
            final boolean boundedDomain) {
        super(model, words, prefix, boundedDomain);

    }

    @Override
    protected void postWordConstraints() {
        // Do nothing
    }

    @Override
    protected void postMaxLengthConstraints() {
        maxLength.eq(0).decompose().post();
    }

}
