/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.ListIterator;

import cryptator.specs.ICryptaNode;
import cryptator.specs.ITraversalEdgeConsumer;
import cryptator.specs.ITraversalNodeConsumer;

/**
 * The Class TreeTraversals provides static functions for common tree
 * traversals.
 */
public final class TreeTraversals {

    /**
     * Does nothing.
     */
    private TreeTraversals() {
    }

    /**
     * Iterative preorder traversal of the tree that feeds a consumer.
     *
     * The consumer accepts a node represented by a pair (node, number).
     *
     * @param root              the root of the tree
     * @param traversalConsumer the traversal consumer of pairs (node, number)
     */
    public static void preorderTraversal(final ICryptaNode root, final ITraversalNodeConsumer traversalConsumer) {
        final Deque<ICryptaNode> stack = new ArrayDeque<>();
        int num = 1;
        stack.push(root);
        while (!stack.isEmpty()) {
            final ICryptaNode n = stack.pop();
            traversalConsumer.accept(n, num);
            if (n.isInternalNode()) {
                stack.push(n.getRightChild());
                stack.push(n.getLeftChild());
            }
            num++;
        }
    }

    /**
     * Push children of a father node on the stack.
     *
     * @param stack the stack
     * @param n     the father node
     * @param num   the number of the father node
     */
    private static void pushChildren(final Deque<TraversalEdge> stack, final ICryptaNode n, final int num) {
        if (n.isInternalNode()) {
            stack.push(new TraversalEdge(n.getRightChild(), n, num));
            stack.push(new TraversalEdge(n.getLeftChild(), n, num));
        }
    }

    /**
     * Iterative preorder traversal of the tree that feeds a consumer.
     *
     * The consumer accepts an edge represented by two pairs (node, number)).
     *
     * The pairs stand for the father and the child of the edge.
     *
     * @param root              the root of the tree
     * @param traversalConsumer the traversal consumer of pairs of pair.
     */
    public static void preorderTraversal(final ICryptaNode root, final ITraversalEdgeConsumer traversalConsumer) {
        final Deque<TraversalEdge> stack = new ArrayDeque<>();
        int num = 1;
        pushChildren(stack, root, num);
        while (!stack.isEmpty()) {
            num++;
            final TraversalEdge e = stack.pop();
            final ICryptaNode n = e.getNode();
            traversalConsumer.accept(n, num, e.getFather(), e.getNumFather());
            pushChildren(stack, n, num);
        }
    }

    /**
     * Iterative postorder traversal of the tree that feeds a consumer.
     *
     * The consumer accepts a node represented by a pair (node, number).
     *
     * @param root                  the root of the tree
     * @param traversalNodeConsumer the traversal consumer of pairs (node, number)
     */
    public static void postorderTraversal(final ICryptaNode root, final ITraversalNodeConsumer traversalNodeConsumer) {
        final Deque<ICryptaNode> stack = new ArrayDeque<>();
        final ArrayList<ICryptaNode> order = new ArrayList<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            final ICryptaNode n = stack.pop();
            order.add(n);
            if (n.isInternalNode()) {
                stack.push(n.getLeftChild());
                stack.push(n.getRightChild());
            }
        }
        final ListIterator<ICryptaNode> iter = order.listIterator(order.size());
        int num = 1;
        while (iter.hasPrevious()) {
            traversalNodeConsumer.accept(iter.previous(), num++);
        }
    }

    /**
     * Iterative inorder traversal of the tree that feeds a consumer.
     *
     * The consumer accepts a node represented by a pair (node, number).
     *
     * @param root                  the root of the tree
     * @param traversalNodeConsumer the consumer of pairs (node, number)
     */
    public static void inorderTraversal(final ICryptaNode root, final ITraversalNodeConsumer traversalNodeConsumer) {
        // https://www.geeksforgeeks.org/inorder-tree-traversal-without-recursion/
        int num = 1;
        Deque<ICryptaNode> s = new ArrayDeque<>();
        ICryptaNode curr = root;

        while ((curr != null) || !s.isEmpty()) {
            while (curr != null) {
                s.push(curr);
                curr = curr.getLeftChild();
            }
            curr = s.pop();
            traversalNodeConsumer.accept(curr, num++);
            curr = curr.getRightChild();
        }
    }

    /**
     * The Class TraversalEdge.
     */
    private static class TraversalEdge {

        /** The node. */
        private final ICryptaNode node;

        /** The father. */
        private final ICryptaNode father;

        /** The num father. */
        private final int numFather;

        /**
         * Instantiates a new traversal edge.
         *
         * @param node      the node
         * @param father    the father
         * @param numFather the num father
         */
        TraversalEdge(final ICryptaNode node, final ICryptaNode father, final int numFather) {
            super();
            this.node = node;
            this.father = father;
            this.numFather = numFather;
        }

        /**
         * Gets the node.
         *
         * @return the node
         */
        public final ICryptaNode getNode() {
            return node;
        }

        /**
         * Gets the father.
         *
         * @return the father
         */
        public final ICryptaNode getFather() {
            return father;
        }

        /**
         * Gets the num father.
         *
         * @return the num father
         */
        public final int getNumFather() {
            return numFather;
        }

    }
}
