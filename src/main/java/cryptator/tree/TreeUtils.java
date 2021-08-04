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

	public static void printInorder(ICryptaNode root) {
		writeInorder(root, System.out);
		System.out.println();
	}

	public static void writeInorder(ICryptaNode root, OutputStream outstream) {
		final PrintWriter out = new PrintWriter(outstream);
		TreeTraversals.inorderTraversal(root, (node, num) -> {
			out.write(node.getWord());
			out.write(" ");
			//			out.write(String.valueOf(num));
			//			out.write(" ");
		}
				);
		out.flush();
	}



}
