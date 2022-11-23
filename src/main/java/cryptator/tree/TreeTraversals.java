/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
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

public final class TreeTraversals {

    private TreeTraversals() {
    }

    private static class TraversalEdge {

        public final ICryptaNode node;

        public final ICryptaNode father;

        public final int numFather;

        public TraversalEdge(ICryptaNode node, ICryptaNode father, int numFather) {
            super();
            this.node = node;
            this.father = father;
            this.numFather = numFather;
        }

        public final ICryptaNode getNode() {
            return node;
        }

        public final ICryptaNode getFather() {
            return father;
        }

        public final int getNumFather() {
            return numFather;
        }

    }

    public static void preorderTraversal(ICryptaNode root, ITraversalNodeConsumer traversalConsumer) {
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

    private static void pushChildren(Deque<TraversalEdge> stack, ICryptaNode n, int num) {
        if (n.isInternalNode()) {
            stack.push(new TraversalEdge(n.getRightChild(), n, num));
            stack.push(new TraversalEdge(n.getLeftChild(), n, num));
        }
    }

    public static void preorderTraversal(ICryptaNode root, ITraversalEdgeConsumer traversalConsumer) {
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

    public static void postorderTraversal(ICryptaNode root, ITraversalNodeConsumer traversalNodeConsumer) {
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

    public static void inorderTraversal(ICryptaNode root, ITraversalNodeConsumer traversalNodeConsumer) {
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

}
