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

import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaPrinter;
import cryptator.specs.ITraversalEdgeConsumer;

public class GraphizExporter implements ICryptaPrinter {

	public GraphizExporter() {}

	@Override
	public void print(ICryptaNode node, OutputStream outstream) {
		final GraphvizConsumer toDotty = new GraphvizConsumer(outstream);
		toDotty.setUp();
		toDotty.writeNode(1, node);
		TreeTraversals.preorderTraversal(node, toDotty);
		toDotty.tearDown();
	}

	private static class GraphvizConsumer implements ITraversalEdgeConsumer {

		private final PrintWriter out;

		public GraphvizConsumer(OutputStream outstream) {
			super();
			this.out = new PrintWriter(outstream);
		}

		public void setUp() {
			out.write("graph G {\nnode [shape=plaintext];\n");
		}

		public void writeNode(int num, ICryptaNode node) {
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
		public void accept(ICryptaNode node, int numNode, ICryptaNode father, int numFather) {
			writeNode(numNode, node);
			writeEdge(numFather, numNode);
		}

		public void tearDown() {
			out.write("}\n");
			out.flush();
		}
	}

}
