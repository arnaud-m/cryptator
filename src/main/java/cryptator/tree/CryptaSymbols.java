/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.tree;

import cryptator.specs.ICryptaNode;
import cryptator.specs.ITraversalNodeConsumer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CryptaSymbols implements ITraversalNodeConsumer {

    private final Set<Character> letters;

    public CryptaSymbols() {
        this.letters = new HashSet<>();
    }

    @Override
    public void accept(ICryptaNode node, int numNode) {
        if (node.isWordLeaf()) {
            for (Character c : node.getWord()) {
                letters.add(c);
            }
        }
    }


    public char[] getSymbols() {
        char[] res = new char[letters.size()];
        int i = 0;
        for (Character character : letters) {
            res[i++] = character;
        }
        Arrays.sort(res);
        return res;
    }

}
