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
import cryptator.specs.ICryptaTree;

public class CryptaNode implements ICryptaTree {
	
	private final CryptaOperator operator;
	
	private final ICryptaTree leftChild;
	
	private final ICryptaTree rightChild;
	
	public CryptaNode(CryptaOperator operator, ICryptaTree leftChild, ICryptaTree rightChild) {
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
	public ICryptaTree getLeftChild() {
		return leftChild;
	}

	@Override
	public ICryptaTree getRightChild() {
		return rightChild;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public boolean isInternalNode() {
		return true;
	}
	
	

}
