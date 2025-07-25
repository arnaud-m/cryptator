/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.tree;

public class CryptaConstant extends CryptaLeaf {

    public CryptaConstant(final String word) {
        super(word);
    }

    public CryptaConstant(final char[] word) {
        super(word);
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public String toGrammarString() {
        return "'" + new String(getWord()) + "'";
    }
}
