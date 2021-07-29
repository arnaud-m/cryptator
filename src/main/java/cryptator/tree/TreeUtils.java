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

import cryptator.solver.CryptaSolution;
import cryptator.solver.Variable;
import cryptator.specs.ICryptaEvaluation;
import cryptator.specs.ICryptaNode;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.expression.discrete.arithmetic.ArExpression;
import org.chocosolver.solver.variables.IntVar;



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

	public static String writeInorder(ICryptaNode root, OutputStream outstream) {
		final String[] s = {""};
		final PrintWriter out = new PrintWriter(outstream);
		TreeTraversals.inorderTraversal(root, (node, num) -> {
			out.write(node.getWord());
			out.write(" ");
			// FIXME The number is invalid
			out.write(String.valueOf(num));
			out.write(" ");
			// TODO What is the purpose of s ? 
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
					addVar(map[0], new Variable(String.valueOf(word[0]), 0, 1, 9));
					for (int i=1; i<word.length;i++) {
						addVar(map[0], new Variable(String.valueOf(word[i]), 0, 0, 9));
					}
					StringBuilder s = new StringBuilder();
					for (char c: word){
						s.append(c);
					}

					addVar(map[0], new Variable(s.toString(), 0, 1, (int) Math.pow(10,word.length)-1));
				}
			}
		);
		return map[0];
	}

	public static ArrayList<IntVar> contraintWordPostorder(ICryptaNode root, ArrayList<IntVar> map, Model model) {
		TreeTraversals.postorderTraversal(root, (node, num) -> {
					if (node.isLeaf()) {
						char[] word = node.getWord();
						IntVar[] vars= new IntVar[word.length];
						int[] coeffs= new int[word.length];

						for(int i=0; i<word.length; i++){
							vars[i]=getVar(map, String.valueOf(word[i]));
							coeffs[i]= (int) Math.pow(10, word.length-i-1);
						}

						StringBuilder s = new StringBuilder();
						for (char c: word){
							s.append(c);
						}
						IntVar v=getVar(map, s.toString());
						model.scalar(vars, coeffs, "=", v).post();
					}
				}
		);
		return map;
	}


	public static Model contraint(ICryptaNode root, Model model) {
		ArrayList<Variable> mapV = mapPostorder(root);

		ArrayList<IntVar> map = new ArrayList<>();
		for (Variable v: mapV){
			map.add(model.intVar(v.getName(), v.getValMin(), v.getValMax(), false));
		}


		contraintWordPostorder(root, map, model);


		ArrayList<IntVar> allDif = new ArrayList<>();
		for(IntVar var: map){
			if(var.getLB()!=var.getUB()){
				allDif.add(var);
			}
		}

		IntVar[] array = allDif.toArray(new IntVar[0]);
		model.allDifferent(array).post();


		comparator(root, map, model);
		return model;
	}

	public static void comparator(ICryptaNode root, ArrayList<IntVar> map, Model model) {
		switch (root.getOperator()){
			case EQ:
				calcul(root.getLeftChild(), map, model).eq(calcul(root.getRightChild(), map, model)).decompose().post();
				break;
			case NEQ:
				calcul(root.getLeftChild(), map, model).ne(calcul(root.getRightChild(), map, model)).decompose().post();
				break;
			case LT:
				calcul(root.getLeftChild(), map, model).lt(calcul(root.getRightChild(), map, model)).decompose().post();
				break;
			case GT:
				calcul(root.getLeftChild(), map, model).gt(calcul(root.getRightChild(), map, model)).decompose().post();
				break;
			case LEQ:
				calcul(root.getLeftChild(), map, model).le(calcul(root.getRightChild(), map, model)).decompose().post();
				break;
			case GEQ:
				calcul(root.getLeftChild(), map, model).ge(calcul(root.getRightChild(), map, model)).decompose().post();
				break;
		}

	}


	public static ArExpression calcul(ICryptaNode root, ArrayList<IntVar> map, Model model) {
		switch (root.getOperator()){
			case ADD:
				return calcul(root.getLeftChild(), map, model).add(calcul(root.getRightChild(), map, model));

			case SUB:
				return calcul(root.getLeftChild(), map, model).sub(calcul(root.getRightChild(), map, model));

			case MUL:
				return calcul(root.getLeftChild(), map, model).mul(calcul(root.getRightChild(), map, model));

			case DIV:
				return calcul(root.getLeftChild(), map, model).div(calcul(root.getRightChild(), map, model));

			case MOD:
				return calcul(root.getLeftChild(), map, model).mod(calcul(root.getRightChild(), map, model));

			case POW:
				return calcul(root.getLeftChild(), map, model).pow(calcul(root.getRightChild(), map, model));

			default:
				IntVar v=getVar(map, String.valueOf(root.getWord()));
				return v!=null? v: model.intVar("0", 0, 0, false);
		}
	}

	private static IntVar getVar(ArrayList<IntVar> map, String s) {
		for (IntVar var: map){
			if(var.getName().equals(s)){
				return var;
			}
		}
		return null;
	}

	public static void addVar(ArrayList<Variable> map, Variable var) {
		for (Variable v: map){
			if (v.getName().equals(var.getName())){
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

	private static ArrayList<Variable> checkArray(ArrayList<Integer> input, ArrayList<Variable> map, ICryptaNode cryptarithm, int max) {
		int i=0;
		for (Variable var : map) {
			if(var.setValue(input.get(i)%max)){
				i++;
			}
			else{
				return null;
			}
		}
		ICryptaEvaluation chk = new CryptaEvaluation();
		int v = chk.evaluate(cryptarithm, new CryptaSolution(map), 10);
		return v == 1? map: null;
	}

	public static ArrayList<Variable> explorationRecursive(ArrayList<Integer> elements,
														   ArrayList<Variable> map, ICryptaNode cryptarithm, int max, int nbRep) throws Exception {
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
				ArrayList<Variable> res = checkArray(elements, map, cryptarithm, max);
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
	public static ArrayList<Variable> heapPermutation(ArrayList<Integer> a, int size, int n, ArrayList<Variable> map, ICryptaNode cryptarithm, int max) {
		// if size becomes 1 then prints the obtained
		// permutation
		ArrayList<Variable> m = null;
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

	public static ArrayList<Variable> findSolCrypta(ArrayList<Integer> comb, int size, int max, ArrayList<Variable> map, ICryptaNode cryptarithm, int nbRep) throws Exception {
		if(map.size()>max*nbRep){
			throw new Exception("to much different letter");
		}
		while (comb!=null) {
			ArrayList<Variable> m=checkArray(comb, map, cryptarithm, max);
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
