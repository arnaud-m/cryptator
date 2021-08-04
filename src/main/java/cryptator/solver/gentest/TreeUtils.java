/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver.gentest;

import java.util.ArrayList;
import java.util.HashMap;

import cryptator.specs.ICryptaEvaluation;
import cryptator.specs.ICryptaNode;
import cryptator.tree.CryptaEvaluation;
import cryptator.tree.CryptaEvaluationException;



public final class TreeUtils {

	private TreeUtils() {}
	


	public static ArrayList<Integer> makeArray (int n) {
		ArrayList<Integer> tab=new ArrayList<>();
		for (int i=0; i<n; i++){
			tab.add(i);
		}
		return tab;
	}



	//checker

	private static HashMap<Character, Variable> checkArray(ArrayList<Integer> input, HashMap<Character, Variable> map, ICryptaNode cryptarithm, int max) {
		int i=0;
		for (Variable var : map.values()) {
			if(var.setValue(input.get(i)%max)){
				i++;
			}
			else{
				return null;
			}
		}
		ICryptaEvaluation chk = new CryptaEvaluation();
		int v;
		try {
			v = chk.evaluate(cryptarithm, new CryptaSolution(map), 10);
		} catch (CryptaEvaluationException e) {
			e.printStackTrace();
			v = -1;
		}
		return v == 1? map: null;
	}

	public static HashMap<Character, Variable> explorationRecursive(ArrayList<Integer> elements,
														   HashMap<Character, Variable> map, ICryptaNode cryptarithm, int max, int nbRep) throws Exception {
		if(map.size()>max*nbRep){
			throw new Exception("to much different letter");
		}
		for(int i = (int) Math.pow(max,elements.size()); i>0; i--) {
			elements.set(elements.size()-1, elements.get(elements.size()-1)+1);
			for(int j=elements.size()-1; j>=0;j--) {
				if (elements.get(j) == max && j - 1 >=0) {
					elements.set(j, 0);
					elements.set(j-1, elements.get(j-1)+1);
				}
			}
			if(elements.get(0)==max){
				break;
			}
			int rep = 0;
			for (int k=0; k<elements.size(); k++) {
				for(int l=0; l<elements.size(); l++) {
					if (elements.get(k).equals(elements.get(l)) && k != l) {
						rep +=1;
					}
				}
			}

			if(rep<=nbRep) {
				HashMap<Character, Variable> res = checkArray(elements, map, cryptarithm, max);
				if (res != null) {
					return res;
				}
			}
		}

		return null;
	}


	public static ArrayList<Integer> nextCombination(ArrayList<Integer> combination, int length, int max) {
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
	public static HashMap<Character, Variable> heapPermutation(ArrayList<Integer> a, int size, int n, HashMap<Character, Variable> map, ICryptaNode cryptarithm, int max) {
		// if size becomes 1 then prints the obtained
		// permutation
		HashMap<Character, Variable> m = null;
		if (size == 1) {
			m=checkArray(a, map, cryptarithm, max);
			return m;
		}

		for (int i = 0; i < size; i++) {
			if(m==null) {
				m = heapPermutation(a, size - 1, n, map, cryptarithm, max);
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

	public static HashMap<Character, Variable> findSolCrypta(ArrayList<Integer> comb, int size, int max, HashMap<Character, Variable> map, ICryptaNode cryptarithm, int nbRep) throws Exception {
		if(map.size()>max*nbRep){
			throw new Exception("to much different letter");
		}
		while (comb!=null) {
			HashMap<Character, Variable> m=checkArray(comb, map, cryptarithm, max);
			if(m!=null){

				return m;
			}
			ArrayList<Integer> perm=new ArrayList<>(comb);
			m=heapPermutation(perm, size, size, map, cryptarithm, max);
			if(m!=null){
				return m;
			}
			comb=nextCombination(comb, size, max*nbRep);
		}
		return null;
	}



		public static String arrayVarToString(HashMap<Character, Variable> map) {
		StringBuilder sb=new StringBuilder();
		if(map!=null) {
			for (Variable var : map.values()) {
				sb.append(var.getName());
				sb.append("=");
				sb.append(var.getValue());
				sb.append(" ");
			}
		}
		return sb.toString();
	}


}
