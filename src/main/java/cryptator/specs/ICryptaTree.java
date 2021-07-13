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

// Rename ICryptaNode ?
public interface ICryptaTree {

	CryptaOperator getOperator();
	
	char[] getWord();
	
	ICryptaTree getLeftChild();
	
	ICryptaTree getRightChild();
	
	boolean isLeaf();
	
	boolean isInternalNode();
	
}
