/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.specs;

/**
 * The Interface ITraversalNodeConsumer visits nodes when traversing the
 * cryptarithm tree.
 */
public interface ITraversalNodeConsumer {

    /**
     * Handle the node during a traversal of the cryptarithm tree. The positions of
     * the node in the traversal ordering is also given as parameter.
     *
     * @param node    the visited node in the tree
     * @param numNode the position in the traversal ordering starting at 0.
     */
    void accept(ICryptaNode node, int numNode);
}
