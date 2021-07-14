/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.tree;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Stack;

import cryptator.specs.ICryptaTree;
import cryptator.specs.ITraversalEdgeConsumer;
import cryptator.specs.ITraversalNodeConsumer;


public final class TreeTraversals {

	private TreeTraversals() {}


	static class TraversalEdge {

		public final ICryptaTree node;

		public final ICryptaTree father;

		public final int numFather;

		public TraversalEdge(ICryptaTree node, ICryptaTree father, int numFather) {
			super();
			this.node = node;
			this.father = father;
			this.numFather = numFather;
		}

		public final ICryptaTree getNode() {
			return node;
		}

		public final ICryptaTree getFather() {
			return father;
		}

		public final int getNumFather() {
			return numFather;
		}


	}

	public static void preorderTraversal(ICryptaTree root, ITraversalNodeConsumer traversalConsumer) {
		final Stack<ICryptaTree> stack = new Stack<ICryptaTree>();
		int num = 1;
		stack.push(root);
		while(! stack.isEmpty()) {
			final ICryptaTree n = stack.pop();
			traversalConsumer.accept(n, num);
			if(n.isInternalNode()) {
				stack.push(n.getRightChild());
				stack.push(n.getLeftChild());
			}
			num++;
		}	
	}

	private static void pushChildren(Stack<TraversalEdge> stack, ICryptaTree n, int num) {
		if(n.isInternalNode()) {
			stack.push(new TraversalEdge(n.getRightChild(), n, num));
			stack.push(new TraversalEdge(n.getLeftChild(), n, num));
		}
	}

	public static void preorderTraversal(ICryptaTree root, ITraversalEdgeConsumer traversalConsumer) {
		final Stack<TraversalEdge> stack = new Stack<TraversalEdge>();
		int num = 1;
		pushChildren(stack, root, num);
		while(! stack.isEmpty()) {
			num++;
			final TraversalEdge e = stack.pop();
			final ICryptaTree n = e.getNode();
			traversalConsumer.accept(n, num, e.father, e.numFather);
			pushChildren(stack, n, num);
		}	
	}

	public static void postorderTraversal(ICryptaTree root, ITraversalNodeConsumer traversalNodeConsumer) {
		final Stack<ICryptaTree> stack = new Stack<ICryptaTree>();
		final ArrayList<ICryptaTree> order= new ArrayList<ICryptaTree>();
		stack.push(root);
		while(! stack.isEmpty()) {
			final ICryptaTree n = stack.pop();
			order.add(n);
			if(n.isInternalNode()) {
				stack.push(n.getLeftChild());
				stack.push(n.getRightChild());
			}
		}
		final ListIterator<ICryptaTree> iter = order.listIterator(order.size());
		int num = 1;
		while(iter.hasPrevious()) {
			traversalNodeConsumer.accept(iter.previous(), num++);
		}
	}


	public static void inorderTraversal(ICryptaTree root, ITraversalNodeConsumer traversalNodeConsumer) {

	}	
}
