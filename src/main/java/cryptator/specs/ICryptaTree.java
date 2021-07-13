package cryptator.specs;

import cryptator.CryptaOperator;

// Rename ICryptaNode ?
public interface ICryptaTree {

	CryptaOperator getOperator();
	
	char[] getWord();
	
	ICryptaTree getLeftChild();
	
	ICryptaTree getRightChild();
	
	boolean isLeaf();
	
	boolean isInternalNode();
	
}
