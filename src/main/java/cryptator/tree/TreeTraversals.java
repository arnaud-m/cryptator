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

    private static boolean shouldParanthesize(final ICryptaNode node, final CryptaOperator op){
        return node.getOperator().getPriority() < op.getPriority();
    }

    private static void buildDecoratedTree(final ICryptaNode node, final DecoratedTree decoratedNode){
        if (node.isInternalNode()){
            final CryptaOperator op = node.getOperator();
            final ICryptaNode l = node.getLeftChild();
            final ICryptaNode r = node.getRightChild();
            decoratedNode.left = new DecoratedTree(l, shouldParanthesize(l, op), decoratedNode, true);
            decoratedNode.right = new DecoratedTree(r, shouldParanthesize(r, op), decoratedNode,false);
//            decoratedNode.left.leftPar = decoratedNode.leftPar + (decoratedNode.putParenthesis ? 1 : 0);
//            decoratedNode.right.rightPar = decoratedNode.rightPar + (decoratedNode.putParenthesis ? 1 : 0);
            buildDecoratedTree(l, decoratedNode.left);
            buildDecoratedTree(r, decoratedNode.right);
        }
    }

    public static void inorderTraversal(final ICryptaNode root, final ITraversalNodeConsumer traversalNodeConsumer) {
        // https://www.geeksforgeeks.org/inorder-tree-traversal-without-recursion/
        int num = 1;

        DecoratedTree decoratedNode = new DecoratedTree(root, false, null, false);
        buildDecoratedTree(root, decoratedNode);

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
        public final ICryptaNode node;
        public final boolean putParenthesis;

        public final DecoratedTree father;
        public DecoratedTree left, right;

        public int leftPar;
        public int rightPar;
        private DecoratedTree(ICryptaNode n, boolean putPar, DecoratedTree father, boolean isLeft) {
            this.node = n;
            this.putParenthesis = putPar;
            this.father = father;
            if (father != null) {
                if (isLeft)
                    leftPar += father.leftPar + (father.putParenthesis ? 1 : 0);
                else
                    rightPar += father.rightPar + (father.putParenthesis ? 1 : 0);
            }
        }

        public int getParenthesisToPut(){
            return node.isInternalNode() ? 0 : -leftPar + rightPar;
        }
    }

}
