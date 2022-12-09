/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.tree;

import cryptator.CryptaOperator;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ITraversalEdgeConsumer;
import cryptator.specs.ITraversalNodeConsumer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.ListIterator;

public final class TreeTraversals {

    private TreeTraversals() {
    }

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

    private static void pushChildren(final Deque<TraversalEdge> stack, final ICryptaNode n, final int num) {
        if (n.isInternalNode()) {
            stack.push(new TraversalEdge(n.getRightChild(), n, num));
            stack.push(new TraversalEdge(n.getLeftChild(), n, num));
        }
    }

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

    private static void buildDecoratedTree(final DecoratedTree decoratedNode){
        if (decoratedNode.node.isInternalNode()){
            final CryptaOperator op = decoratedNode.node.getOperator();
            buildDecoratedTree(new DecoratedTree(op, decoratedNode, true));
            buildDecoratedTree(new DecoratedTree(op, decoratedNode,false));
        }
    }

    public static void inorderTraversal(final ICryptaNode root, final ITraversalNodeConsumer traversalNodeConsumer) {
        // https://www.geeksforgeeks.org/inorder-tree-traversal-without-recursion/

        DecoratedTree decoratedNode = new DecoratedTree(root);
        buildDecoratedTree(decoratedNode);

        Deque<DecoratedTree> s = new ArrayDeque<>();
        DecoratedTree curr = decoratedNode;
        while ((curr != null) || !s.isEmpty()) {
            while (curr != null) {
                s.push(curr);
                curr = curr.left;
            }
            curr = s.pop();
            traversalNodeConsumer.accept(curr.node, curr.getParenthesisToPut());
            curr = curr.right;
        }
    }

    private static class TraversalEdge {

        private final ICryptaNode node;

        private final ICryptaNode father;

        private final int numFather;

        TraversalEdge(final ICryptaNode node, final ICryptaNode father, final int numFather) {
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

    private static final class DecoratedTree{
        // The current node of the ICryptaNode tree
        public final ICryptaNode node;
        // True if the current operator OP has less priority than the father operator one
        // or if the current node is on the right of its father and its father's operator
        // is not commutative with the same priority of OP
        public final boolean putParenthesis;
        // The children of the Decorated tree
        public DecoratedTree left, right;

        // The number of left/right parenthesis to put when printing in inorder mode
        public int leftPar, rightPar;
        private DecoratedTree(CryptaOperator op, DecoratedTree father, boolean isLeft) {
            if (isLeft) {
                father.left = this;
                leftPar = father.leftPar + (father.putParenthesis ? 1 : 0);
                node = father.node.getLeftChild();
            }
            else {
                father.right = this;
                rightPar = father.rightPar + (father.putParenthesis ? 1 : 0);
                node = father.node.getRightChild();
            }
            this.putParenthesis = node.getOperator().getPriority() < op.getPriority() ||
                    (node.getOperator().getPriority() == op.getPriority() && !isLeft && !op.isCommutative());
        }

        private DecoratedTree(ICryptaNode node){
            this.node = node;
            this.putParenthesis = false;
        }

        public int getParenthesisToPut(){
            return node.isInternalNode() ? 0 : -leftPar + rightPar;
        }
    }
}
