/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.tree;

public class CryptaConstant extends CryptaLeaf {
    public CryptaConstant() {
        this(new char[0]);
    }

    public CryptaConstant(String word) {
        this(word.toCharArray());
    }

    public CryptaConstant(char[] word) {
        super(word, true);
    }


    // TODO : convert to base of cryptarithm
    public int getConstant() {
        return Integer.parseInt(new String(getWord()));
    }
}
