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

import cryptator.solver.CryptaSolutionException;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaPrinter;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ICryptaSolutionPrinter;
import cryptator.specs.ITraversalEdgeConsumer;

@Deprecated
public class GraphizExporter implements ICryptaPrinter, ICryptaSolutionPrinter {

	public GraphizExporter() {}

	private void print(ICryptaNode node, GraphvizConsumer toDotty) {
		toDotty.setUp();
		toDotty.writeNode(1, node);
		TreeTraversals.preorderTraversal(node, toDotty);
		toDotty.tearDown();
	}

	@Override
	public void print(ICryptaNode node, OutputStream outstream) {
		print(node, new GraphvizConsumer(outstream));
	}


	@Override
	public void print(ICryptaNode node, ICryptaSolution solution, OutputStream outstream) {
		print(node, new GraphvizSolutionConsumer(outstream, solution));	
	}


	private static class GraphvizConsumer implements ITraversalEdgeConsumer {

		protected final PrintWriter out;

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

	private static class GraphvizSolutionConsumer extends GraphvizConsumer {

		private final ICryptaSolution solution;

		public GraphvizSolutionConsumer(OutputStream outstream, ICryptaSolution solution) {
			super(outstream);
			this.solution = solution;
		}

		private void writeRecordLabel(char[] a) {
			if (a == null)
				return ;
			int iMax = a.length - 1;
			if (iMax == -1)
				return ;

			for (int i = 0; ; i++) {
				out.write('{');
				out.write(a[i]);
				out.write('|');
				try {
					final int digit = solution.getDigit(a[i]);
					out.write(String.valueOf(digit));
				} catch (CryptaSolutionException e) {
					// Nothing to write
				}	
				out.write('}');

				if (i == iMax)
					return ;
				out.write("|");
			}		
		}


		@Override
		public void writeNode(int num, ICryptaNode node) {
			if(node.isInternalNode()) super.writeNode(num, node);
			else {
				out.write(Integer.toString(num));
				out.write("[shape=record, label=\"");
				writeRecordLabel(node.getWord());
				out.write("\"];\n");
			}
		}

	}


}
