/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.tree;

import cryptator.CryptaOperator;
import cryptator.specs.ICryptaNode;

public class CryptaLeaf implements ICryptaNode {

	private final char[] word;

	public CryptaLeaf() {
		// TODO Remove old representation of '0' ; replace by constant ; update feature computation too.; graphivz export.
		this(new char[0]);
	}

	public CryptaLeaf(String word) {
		this(word.toCharArray());
	}

	public CryptaLeaf(char[] word) {
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
	public String write(){
		return new String(getWord());
	}
}
