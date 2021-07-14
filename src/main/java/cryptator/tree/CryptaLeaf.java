/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.tree;

import cryptator.CryptaOperator;
import cryptator.specs.ICryptaNode;

public class CryptaLeaf implements ICryptaNode {

	private final char[] word;
	
	public CryptaLeaf(char[] word) {
		this.word = word;
	}
	
	public CryptaLeaf(String word) {
		this(word.toCharArray());
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
	public boolean isLeaf() {
		return true;
	}

	@Override
	public boolean isInternalNode() {
		return false;
	}

}
