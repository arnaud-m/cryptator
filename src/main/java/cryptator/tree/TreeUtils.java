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
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaEvaluation;
import cryptator.specs.ITraversalEdgeConsumer;
import cryptator.specs.ITraversalNodeConsumer;

public final class TreeUtils {

	private TreeUtils() {}

	public static void writePreorder(ICryptaNode root, OutputStream outstream) {
		final PrintWriter out = new PrintWriter(outstream);
		TreeTraversals.preorderTraversal(root, (node, num) -> {
			out.write(node.getWord());
			out.write(" ");
		}
				);
		out.flush();
	}
	
	public static void printPostorder(ICryptaNode root) {
		writePostorder(root, System.out);
		System.out.println();
	}
	
	public static void writePostorder(ICryptaNode root, OutputStream outstream) {
		final PrintWriter out = new PrintWriter(outstream);
		TreeTraversals.postorderTraversal(root, (node, num) -> {
			out.write(node.getWord());
			out.write(" ");
		}
				);
		out.flush();
	}
	

	// TODO Return a choco model : add mvn dependency
	public static Object model(ICryptaNode root) {
		return null;
	}

}
