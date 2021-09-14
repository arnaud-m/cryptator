/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.util.HashMap;
import java.util.Map;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.expression.discrete.relational.ReExpression;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

public class CryptaGenListModel {

	private final String[] words;

	Model m;
	BoolVar[] leftW;
	BoolVar[] rightW;

	IntVar[] leftLen;
	IntVar[] rightLen;

	IntVar maxLenL;
	IntVar maxLenR;

	IntVar wordCountL;

	public final Map<Character, BoolVar> symbolsToVariables = new HashMap<>();

	public CryptaGenListModel(String[] words) {
		this.words = words;
	}


	private void buildWordVars() {
		int n = words.length;

		leftW = new BoolVar[n];
		rightW = new BoolVar[n];

		for (int i = 0; i < n; i++) {
			leftW[i] = m.boolVar("L_"+words[i]);
			rightW[i] = m.boolVar("R_"+words[i]);
			leftW[i].add(rightW[i]).le(1).post();
		}	
	}
	private void buildLenVars() {
		int n = words.length;
		leftLen = new IntVar[n];
		rightLen = new IntVar[n];
		for (int i = 0; i < words.length; i++) {
			leftLen[i] = leftW[i].mul(words[i].length()).intVar();
			rightLen[i] = rightW[i].mul(words[i].length()).intVar();
		}

		int maxLen = 100;
		maxLenL = m.intVar("maxLenL", 0, maxLen);
		maxLenR = m.intVar("maxLenR", 0, maxLen);
		m.max(maxLenL, leftLen).post();
		m.max(maxLenR, rightLen).post();
	}
	
	private void postLenConstraint() {
		maxLenR.ge(maxLenL).post();
		// TODO maxLenR.sub(maxLenL).gt(1).imp(wordCountL.ge(10)).post();	
	}
	
	
	private void buildSymbolVars(Map<Character, ReExpression> symbolsToExpressions) {
		for (Character c : symbolsToExpressions.keySet()) {
			BoolVar var = m.boolVar(String.valueOf(c));
			symbolsToVariables.put(c, var);
			symbolsToExpressions.get(c).eq(var).post();
		}
	}
	
	private void postWordCountConstraint() {
		wordCountL = m.intVar("wcL", 2, words.length);
		m.sum(leftW, "=", wordCountL).post();
		m.sum(rightW, "=", 1).post();
	}
	
	private void postDigitCountingConstraint() {
		IntVar[] symbols = symbolsToVariables.values().toArray(new IntVar[symbolsToVariables.size()]);
		m.sum(symbols, "<=", 10).post();
	}
	
	private Map<Character, ReExpression> buildExpressions() {
		final Map<Character, ReExpression> symbolsToExpressions = new HashMap<>();

		//IntVar z = m.intVar("maxLen", 0, 1000);
		for (int i = 0; i < words.length; i++) {
			for (char c : words[i].toCharArray()) {
				ReExpression expr = leftW[i].or(rightW[i]);
				if(! symbolsToExpressions.containsKey(c) ) {
					symbolsToExpressions.put(c, expr);
				} else {
					symbolsToExpressions.replace(
							c, 
							symbolsToExpressions.get(c).or(expr)
							);
				}

			}
		}
		return symbolsToExpressions;
	}
	public void modeling() {
		m = new Model();
		int n = words.length;
		buildWordVars();
		
		final Map<Character, ReExpression> symbolsToExpressions = buildExpressions();
		buildSymbolVars(symbolsToExpressions);
		postDigitCountingConstraint();
		
		postWordCountConstraint();
		
		buildLenVars();
		postLenConstraint();
		
		//wordCountL.eq(13).post();
		System.out.println(m);
		Solution sol = new Solution(m);
		int solutionCount = 0;
		//Solution sol = new Solution(m);
		while(m.getSolver().solve()) {
			solutionCount++;
			sol.record();
			//System.out.println(sol);
			for (IntVar v : leftW) {
				if(v.isInstantiatedTo(1)) System.out.print(v.getName().substring(2) + " + ");
			}
			System.out.print("= ");
			for (IntVar v : rightW) {
				if(v.isInstantiatedTo(1)) System.out.print(v.getName().substring(2) + " ");
			}
			System.out.println();
		}
		m.getSolver().printStatistics();
		System.out.println(solutionCount);

	}

	//	public void modeling() {
	//		Model m = new Model();
	//		BoolVar[] x = m.boolVarArray("x", words.length);
	//		//IntVar[] y = new IntVar[words.length];
	//		final Map<Character, ReExpression> symbolsToExpressions = new HashMap<>();
	//
	//		//IntVar z = m.intVar("maxLen", 0, 1000);
	//		for (int i = 0; i < words.length; i++) {
	//			for (char c : words[i].toCharArray()) {
	//				if(! symbolsToVariables.containsKey(c) ) {
	//					symbolsToVariables.put(c,  m.boolVar(String.valueOf(c)));
	//					symbolsToExpressions.put(c,  x[i]);
	//				} else {
	//					symbolsToExpressions.replace(
	//							c, 
	//							symbolsToExpressions.get(c).or(x[i])
	//							);
	//				}
	//
	//				BoolVar v = symbolsToVariables.get(c);
	//
	//				//v.or(v.not()).post();
	//				//x[i].le(v).decompose().post();
	//			}
	//						//y[i] = x[i].mul(words[i].length()).intVar();
	//		}
	//		for (Character c : symbolsToVariables.keySet()) {
	//			symbolsToExpressions.get(c).eq(symbolsToVariables.get(c)).post();
	//		}
	//
	//		//m.max(z, y).post();
	//
	//		IntVar[] symbols = symbolsToVariables.values().toArray(new IntVar[symbolsToVariables.size()]);
	//		m.sum(symbols, "<=", 10).post();
	//		System.out.println(m);
	//				Solution sol = new Solution(m, x);
	//				int n = 0;
	//		//Solution sol = new Solution(m);
	//				while(m.getSolver().solve()) {
	//					n++;
	//					sol.record();
	//					System.out.println(sol);
	//				}
	//				System.out.println(n);
	//
	//	}
	
	public static void main(String[] args) {
		CryptaGenListModel gen = new CryptaGenListModel(args);
		gen.modeling();
	}

}
