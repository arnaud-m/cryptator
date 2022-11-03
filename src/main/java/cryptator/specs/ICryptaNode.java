/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.specs;

import cryptator.CryptaOperator;

/**
 * The Interface ICryptaNode defines a node of an abstract syntax binary tree that represents a cryptarithm.
 * The tree is not the parse tree built by the ANTLR parser.
 * Each node, even a leaf, is associated to an operator and to a word.
 *
 */
public interface ICryptaNode {

	/**
	 * Gets the binary operator associated to the node.
	 *
	 * @return the operator
	 */
	CryptaOperator getOperator();

	/**
	 * Gets the word associated to the node.
	 *
	 * @return the word
	 */
	char[] getWord();

	/**
	 * Gets the left child if any.
	 *
	 * @return the left child
	 */
	ICryptaNode getLeftChild();
	//FIXME Use java.util.Optional ? 

	/**
	 * Gets the right child if any.
	 *
	 * @return the right child
	 */
	ICryptaNode getRightChild();
	//FIXME Use java.util.Optional ? 

	/**
	 * Checks if the node is a leaf of the tree.
	 *
	 * @return true, if it is leaf
	 */
	boolean isLeaf();

	/**
	 * Checks if the node is an internal node of the tree.
	 *
	 * @return true, if it is internal node
	 */
	boolean isInternalNode();

	boolean isConstant();

}
