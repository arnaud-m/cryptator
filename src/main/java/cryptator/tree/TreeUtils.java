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
import java.util.HashMap;

import cryptator.solver.CryptaSolution;
import cryptator.specs.ICryptaEvaluation;
import cryptator.specs.ICryptaNode;

public final class TreeUtils {

	private TreeUtils() {}

	public static String writePreorder(ICryptaNode root, OutputStream outstream) {
		final String[] s = {""};
		final PrintWriter out = new PrintWriter(outstream);

		TreeTraversals.preorderTraversal(root, (node, num) -> {
			out.write(node.getWord());
			out.write(" ");
			s[0] = s[0] + String.valueOf(node.getWord()) + " ";
		}
				);
		out.flush();
		return s[0];
	}

	
	public static void printPostorder(ICryptaNode root) {
		if(root!=null) {
			writePostorder(root, System.out);
		}
		System.out.println();
	}
	
	public static String writePostorder(ICryptaNode root, OutputStream outstream) {
		final String[] s = {""};
		final PrintWriter out = new PrintWriter(outstream);
		TreeTraversals.postorderTraversal(root, (node, num) -> {
			out.write(node.getWord());
			out.write(" ");
			s[0] = s[0] + String.valueOf(node.getWord()) + " ";
		}
				);
		out.flush();
		return s[0];

	}

	public static void printInorder(ICryptaNode root) {
		if(root!=null) {
			writeInorder(root, System.out);
		}
		System.out.println();
	}

	public static String writeInorder(ICryptaNode root, OutputStream outstream) {
		final String[] s = {""};
		final PrintWriter out = new PrintWriter(outstream);
		TreeTraversals.inorderTraversal(root, (node, num) -> {
			out.write(node.getWord());
			out.write(" ");
			s[0] = s[0] + String.valueOf(node.getWord()) + " ";
		}
				);
		out.flush();
		return s[0];
	}

	public static HashMap<Character, Integer> explorationRecursive(int n, int[] elements, HashMap<Character,
			Integer> res, HashMap<Character, Integer> map, ICryptaNode cryptarithm) {

		if(n == 1){
			return checkArray(elements, map, cryptarithm);
		} else if (res==null){
			for(int i = 0; i < n-1; i++) {
				HashMap<Character, Integer> tmp = explorationRecursive(n - 1, elements, res, map, cryptarithm);
				res=res!=null? res: tmp;
				if(n % 2 == 0) {
					swap(elements, i, n-1);
				} else {
					swap(elements, 0, n-1);
				}
			}
			HashMap<Character, Integer> tmp = explorationRecursive(n - 1, elements, res, map, cryptarithm);
			res=res!=null? res: tmp;
		}
		return res;
	}

	private static void swap(int[] input, int a, int b) {
		int tmp = input[a];
		input[a] = input[b];
		input[b] = tmp;
	}

	private static HashMap<Character, Integer> checkArray(int[] input, HashMap<Character,
			Integer> map, ICryptaNode cryptarithm) {
		int i=0;
		for (Character key : map.keySet()) {
			map.replace(key, input[i]);
			i++;
		}
		ICryptaEvaluation chk = new CryptaEvaluation();
		int v = chk.evaluate(cryptarithm, new CryptaSolution(map), 10);
		return v == 1? map: null;
	}

	public static HashMap<Character, Integer> explorationRecursive2(int[] elements,
			HashMap<Character, Integer> map, ICryptaNode cryptarithm) {

		for (int i=elements.length-1; i>=0; i--){
			for(int j=0; j<10; j++) {
				elements[i] += 1;
				if (elements[i] == 9 && i-1>=0) {
					elements[i] = 0;
					elements[i - 1] += 1;
				}
				boolean stop = false;
				for (int nb=0; nb<elements.length; nb++) {
					if (elements[nb] == elements[i] && nb != i) {
						stop = true;
						break;
					}
				}

				if(!stop) {
					HashMap<Character, Integer> res = checkArray(elements, map, cryptarithm);
					if (res != null) {
						return res;
					}
				}
			}
		}

		return null;
	}


	// TODO Return a choco model : add mvn dependency
	public static Object model(ICryptaNode root) {
		return null;
	}

}
