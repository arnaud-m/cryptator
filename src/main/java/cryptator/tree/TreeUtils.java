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
import java.util.ArrayList;
import java.util.Arrays;

import cryptator.solver.CryptaSolution;
import cryptator.solver.Variable;
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

	public static ArrayList<Variable> mapPostorder(ICryptaNode root) {
		final ArrayList<Variable>[] map = new ArrayList[]{new ArrayList<>()};
		TreeTraversals.postorderTraversal(root, (node, num) -> {
				if (node.isLeaf()) {
					char[] word = node.getWord();
					addVar(map[0], new Variable(word[0], 0, 1, 9));
					for (int i=1; i<word.length;i++) {
						addVar(map[0], new Variable(word[i], 0, 0, 9));
					}
				}
			}
		);
		return map[0];
	}

	public static void addVar(ArrayList<Variable> map, Variable var) {
		for (Variable v: map){
			if (v.getName()==var.getName()){
				v.setValMax(Math.min(v.getValMax(), var.getValMax()));
				v.setValMin(Math.max(v.getValMin(), var.getValMin()));
				return;
			}
		}
		map.add(var);
	}

		public static ArrayList<Integer> makeArray (int n) {
		ArrayList<Integer> tab=new ArrayList<>();
		for (int i=0; i<n; i++){
			tab.add(i);
		}
		return tab;
	}



	//checker

	private static ArrayList<Variable> checkArray(ArrayList<Integer> input, ArrayList<Variable> map, ICryptaNode cryptarithm) {
		int i=0;
		for (Variable var : map) {
			var.setValue(input.get(i));
			i++;
		}

		ICryptaEvaluation chk = new CryptaEvaluation();
		int v = chk.evaluate(cryptarithm, new CryptaSolution(map), 10);
		return v == 1? map: null;
	}

	public static ArrayList<Variable> explorationRecursive(ArrayList<Integer> elements,
														   ArrayList<Variable> map, ICryptaNode cryptarithm) {
			System.out.println(Math.pow(10,elements.size()));
			for(int i=0; i<Math.pow(10,elements.size()); i++) {
				elements.set(0, elements.get(0)+1);
				for(int j=0; j<elements.size()-1;j++) {
					if (elements.get(j) == 10 && j + 1 < elements.size()) {
						elements.set(j, 0);
						elements.set(j+1, elements.get(j+1)+1);

					}
				}

				boolean stop = false;
				for (int k=0; k<elements.size(); k++) {
					for(int l=0; l<elements.size(); l++) {
						if (elements.get(k).equals(elements.get(l)) && k != l) {
							stop = true;
							break;
						}
					}
				}

				if(!stop) {
					ArrayList<Variable> res = checkArray(elements, map, cryptarithm);
					if (res != null) {
						return res;
					}
				}
			}

		return null;
	}


	public static ArrayList<Integer> nextCombination(ArrayList<Integer> combination, int length, int max, ArrayList<Variable> map, ICryptaNode cryptarithm) {
		int last = length - 1;

		while (last >= 0 && combination.get(last) == max - length + last) {
			last--;
		}

		if (last < 0) return null;

		combination.set(last, combination.get(last)+1);

		for (int i = last + 1; i < length; i++) {
			combination.set(i, combination.get(last) - last + i);
		}

		return combination;
	}

	// Generating permutation using Heap Algorithm
	public static ArrayList<Variable> heapPermutation(ArrayList<Integer> a, int size, int n, ArrayList<Variable> map, ICryptaNode cryptarithm) {
		// if size becomes 1 then prints the obtained
		// permutation
		ArrayList<Variable> m = null;
		if (size == 1) {
			m=checkArray(a, map, cryptarithm);
			return m;
		}

		for (int i = 0; i < size; i++) {
			if(m==null) {
				m = heapPermutation(a, size - 1, n, map, cryptarithm);
			}

			// if size is odd, swap 0th i.e (first) and
			// (size-1)th i.e (last) element
			if (size % 2 == 1) {
				int temp = a.get(0);
				a.set(0, a.get(size - 1));
				a.set(size - 1, temp);
			}

			// If size is even, swap ith
			// and (size-1)th i.e last element
			else {
				int temp = a.get(i);
				a.set(i, a.get(size - 1));
				a.set(size - 1, temp);
			}
		}
		return m;
	}

	public static ArrayList<Variable> findSolCrypta(ArrayList<Integer> comb, int size, int max, ArrayList<Variable> map, ICryptaNode cryptarithm) {
		while (comb!=null) {
			ArrayList<Variable> m=checkArray(comb, map, cryptarithm);
			if(m!=null){
				return m;
			}
			ArrayList<Integer> perm=new ArrayList<>(comb);
			m=heapPermutation(perm, size, size, map, cryptarithm);
			if(m!=null){
				return m;
			}
			comb=nextCombination(comb, size, max, map, cryptarithm);
		}
		return null;
	}



		public static String arrayVarToString(ArrayList<Variable> map) {
		StringBuilder sb=new StringBuilder();
		if(map!=null) {
			for (Variable var : map) {
				sb.append(var.getName());
				sb.append("=");
				sb.append(var.getValue());
				sb.append(" ");
			}
		}
		return sb.toString();
	}


	// TODO Return a choco model : add mvn dependency
	public static Object model(ICryptaNode root) {
		return null;
	}

}
