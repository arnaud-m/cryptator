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
 * The Interface ITraversalEdgeConsumer visits edges when traversing the
 * cryptarithm tree.
 */
public interface ITraversalEdgeConsumer {

    /**
     * Handle the edge (father -> node) during a traversal of the cryptarithm tree.
     * The positions of the nodes in the traversal ordering are also given as
     * parameters.
     *
     * @param node      the visited node in the tree
     * @param numNode   the position of the node in the traversal ordering starting
     *                  at 0.
     * @param father    the father node in the tree
     * @param numFather the position of the father in the traversal ordering
     *                  starting at 0.
     */
    void accept(ICryptaNode node, int numNode, ICryptaNode father, int numFather);
}
