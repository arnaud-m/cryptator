/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.tree;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Stack;

import cryptator.specs.ICryptaSolution;
import cryptator.specs.ICryptaTree;
import cryptator.specs.ICryptarithmEvaluator;
import cryptator.specs.ITraversalEdgeConsumer;
import cryptator.specs.ITraversalNodeConsumer;

public final class TreeUtils {

	private TreeUtils() {}

	private static class TreeToDotty implements ITraversalEdgeConsumer {

		private final PrintWriter out;

		public TreeToDotty(OutputStream outstream) {
			super();
			this.out = new PrintWriter(outstream);
		}

		public void setUp() {
			out.write("graph G {\nnode [shape=plaintext];\n");
		}

		public void writeNode(int num, ICryptaTree node) {
			out.write(Integer.toString(num));
			out.write("[label=\"");
			out.write(node.getWord());
			out.write("\"];\n");
		}

		public void writeEdge(int i, int j) {
			out.write(Integer.toString(i));
			out.write("--");
			out.write(Integer.toString(j));
			out.write(";\n");
		}


		@Override
		public void accept(ICryptaTree node, int numNode, ICryptaTree father, int numFather) {
			writeNode(numNode, node);
			writeEdge(numFather, numNode);
		}

		public void tearDown() {
			out.write("}\n");
			out.flush();
		}


	}

	public static void toDotty(ICryptaTree root, OutputStream outstream) {
		final TreeToDotty toDotty = new TreeToDotty(outstream);
		toDotty.setUp();
		toDotty.writeNode(1, root);
		TreeTraversals.preorderTraversal(root, toDotty);
		toDotty.tearDown();

	}

	public static void writePreorder(ICryptaTree root, OutputStream outstream) {
		final PrintWriter out = new PrintWriter(outstream);
		TreeTraversals.preorderTraversal(root, (node, num) -> {
			out.write(node.getWord());
			out.write(" ");
		}
				);
		out.flush();
	}
	
	public static void writePostorder(ICryptaTree root, OutputStream outstream) {
		final PrintWriter out = new PrintWriter(outstream);
		TreeTraversals.postorderTraversal(root, (node, num) -> {
			out.write(node.getWord());
			out.write(" ");
		}
				);
		out.flush();
	}

	public static void toString(ICryptaTree root, OutputStream outstream) {

	}
	
	
	static class CheckerConsumer implements ITraversalNodeConsumer {

		private final ICryptaSolution solution;
		
		private final int base;
		
		private final Stack<Integer> stack = new Stack<Integer>();
		
		
		public CheckerConsumer(ICryptaSolution solution, int base) {
			super();
			this.solution = solution;
			this.base = base;
		}

		private final Integer getWordValue(ICryptaTree node) {
			int v = 0;
			for (char c : node.getWord()) {
				// TODO Handle missing value ?
				v = v * base + solution.getDigit(c);
			}
			return v;
		}
		
		@Override
		public void accept(ICryptaTree node, int numNode) {
			if(node.isLeaf()) {
				stack.push(getWordValue(node));
			} else {
				final int b = stack.pop();
				final int a = stack.pop();
				stack.push(node.getOperator().getFunction().applyAsInt(a, b));
			}
		}
		
		public int peek() {
			// TODO Check that the stack contains a single value
			return stack.peek();
		}
	}
	
	static class CryptaChecker implements ICryptarithmEvaluator {

		@Override
		public int evaluate(ICryptaTree cryptarithm, ICryptaSolution solution, int base) {
			CheckerConsumer checkerNodeConsumer = new CheckerConsumer(solution, base);
			TreeTraversals.postorderTraversal(cryptarithm, checkerNodeConsumer);
			return checkerNodeConsumer.peek();
		}		
	}
	
	public static ICryptarithmEvaluator makeCryptarithmChecker() {
		return new CryptaChecker();
	}

	// TODO Return a choco model : add mvn dependency
	public static Object model(ICryptaTree root) {
		return null;
	}

}
