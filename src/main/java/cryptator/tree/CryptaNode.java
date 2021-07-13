package cryptator.tree;

import cryptator.CryptaOperator;
import cryptator.specs.ICryptaTree;

public class CryptaNode implements ICryptaTree {
	
	private final static char[] EMPTY_WORD = new char[0];
	
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
		return operator.getOperator().toCharArray();
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
