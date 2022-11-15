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

public class CryptaNode implements ICryptaNode {

	private final CryptaOperator operator;

	private final ICryptaNode leftChild;

	private final ICryptaNode rightChild;

	public CryptaNode(String operator, ICryptaNode leftChild, ICryptaNode rightChild) {
		this(CryptaOperator.valueOfToken(operator), leftChild, rightChild);
	}

	public CryptaNode(CryptaOperator operator, ICryptaNode leftChild, ICryptaNode rightChild) {
		this.operator = operator;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}

	@Override
	public CryptaOperator getOperator() {
		return operator;
	}

	@Override
	public char[] getWord() {
		return operator.getToken().toCharArray();
	}

	@Override
	public ICryptaNode getLeftChild() {
		return leftChild;
	}

	@Override
	public ICryptaNode getRightChild() {
		return rightChild;
	}

	@Override
	public boolean isInternalNode() {
		return true;
	}
	
	@Override
	public boolean isConstant() {
		return leftChild.isConstant() && rightChild.isConstant();
	}

	@Override
	public String toString() {
		return operator.getToken();
	}

	public String write(){
		return operator.getToken();
	}
}
