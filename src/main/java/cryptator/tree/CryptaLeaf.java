/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.tree;

import cryptator.CryptaOperator;
import cryptator.specs.ICryptaNode;

public class CryptaLeaf implements ICryptaNode {

    private final char[] word;

    public CryptaLeaf(final String word) {
        this(word.toCharArray());
    }

    public CryptaLeaf(final char[] word) {
        this.word = word;
    }

    @Override
    public CryptaOperator getOperator() {
        return CryptaOperator.ID;
    }

    @Override
    public char[] getWord() {
        return word;
    }

    @Override
    public ICryptaNode getLeftChild() {
        return null;
    }

    @Override
    public ICryptaNode getRightChild() {
        return null;
    }

    @Override
    public boolean isInternalNode() {
        return false;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public String toString() {
        return new String(word);
    }

    @Override
    public String toGrammarString() {
        return new String(getWord());
    }
}
