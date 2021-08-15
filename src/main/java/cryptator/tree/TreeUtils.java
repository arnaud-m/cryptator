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

	public final static String ZERO = "_0";
	
	private TreeUtils() {}

	private static void writeWord(ICryptaNode node, PrintWriter out) {
		char[] w = node.getWord();
		if(w.length > 0) out.write(node.getWord());
		else out.write(ZERO);
		out.write(" ");		
	}
	
	public static void writePreorder(ICryptaNode root, OutputStream outstream) {
		final PrintWriter out = new PrintWriter(outstream);
		TreeTraversals.preorderTraversal(root, (node, num) -> {
			writeWord(node, out);
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
			writeWord(node, out);
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
			writeWord(node, out);
		}
				);
		out.flush();
	}



}
