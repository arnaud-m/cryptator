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
 * The Interface ICryptaNode defines a node of an abstract syntax binary tree
 * that represents a cryptarithm. The tree is not the parse tree built by the
 * ANTLR parser. Each node, even a leaf, is associated to an operator and to a
 * word.
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

    /**
     * Gets the right child if any.
     *
     * @return the right child
     */
    ICryptaNode getRightChild();

    /**
     * Checks if the node is an internal node of the tree.
     *
     * @return true, if it is internal node, otherwise it is a leaf
     */
    boolean isInternalNode();
    
    
    /**
     * Checks if the subtree is a constant of the tree.
     *
     * @return true, if it is constant, otherwise false.
     */
    boolean isConstant();

    /**
     * Checks if the node represents a word, i.e. it is a leaf and not constant. 
     *
     * @return true, if it is a word leaf, otherwise false.
     */
    default boolean isWord() {
    	return !isInternalNode() && !isConstant();
    }
    

    /**
     * Transform parsed node to string.
     * For example parse(inOrderPrint(parse("a+'8'=b"))) is the same as parse("a+'8'=b")
     *
     * @return the string representation of the node accepted by the parser
     */
    String write();
    
}
