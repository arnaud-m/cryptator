/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.specs;

import cryptator.CryptaOperator;


public interface ICryptaNode {

	CryptaOperator getOperator();
	
	char[] getWord();
	
	ICryptaNode getLeftChild();
	
	ICryptaNode getRightChild();
	
	boolean isLeaf();
	
	boolean isInternalNode();
	
}
