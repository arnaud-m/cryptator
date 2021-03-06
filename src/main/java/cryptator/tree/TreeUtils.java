/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.tree;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import cryptator.CryptaOperator;
import cryptator.specs.ICryptaNode;


public final class TreeUtils {

	public static final String ZERO = "_0";
	
	private TreeUtils() {}

	private static void writeWord(ICryptaNode node, PrintWriter out) {
		char[] w = node.getWord();
		if(w.length > 0) out.write(node.getWord());
		else out.write(ZERO);
		out.write(" ");		
	}
	
	public static void writePreorder(ICryptaNode root, OutputStream outstream) {
		final PrintWriter out = new PrintWriter(outstream);
		TreeTraversals.preorderTraversal(root, (node, num) -> writeWord(node, out));
		out.flush();
	}


	public static void printPostorder(ICryptaNode root) {
		writePostorder(root, System.out);
		System.out.println();
	}

	public static void writePostorder(ICryptaNode root, OutputStream outstream) {
		final PrintWriter out = new PrintWriter(outstream);
		TreeTraversals.postorderTraversal(root, (node, num) -> writeWord(node, out));
		out.flush();
	}

	public static void printInorder(ICryptaNode root) {
		writeInorder(root, System.out);
		System.out.println();
	}

	public static void writeInorder(ICryptaNode root, OutputStream outstream) {
		final PrintWriter out = new PrintWriter(outstream);
		TreeTraversals.inorderTraversal(root, (node, num) -> writeWord(node, out));
		out.flush();
	}

	public static String writeInorder(ICryptaNode root) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		TreeUtils.writeInorder(root, out);
		return out.toString();
	}
	
	public static CryptaFeatures computeFeatures(ICryptaNode cryptarithm) {
		final CryptaFeatures feat = new CryptaFeatures();
		TreeTraversals.preorderTraversal(cryptarithm, feat);
		return feat;
	}
	
	public static CryptaOperatorDetection computeUnsupportedBignumOperator(ICryptaNode cryptarithm) {
		final CryptaOperatorDetection detect = new CryptaOperatorDetection(CryptaOperator.ID, CryptaOperator.ADD, CryptaOperator.EQ);
		TreeTraversals.preorderTraversal(cryptarithm, detect);
		return detect;
	}
	
	public static char[] computeSymbols(ICryptaNode cryptarithm) {
		final CryptaSymbols sym = new CryptaSymbols();
		TreeTraversals.preorderTraversal(cryptarithm, sym);
		return sym.getSymbols();
	}
	
}
