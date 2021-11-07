/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;

import cryptator.CryptaOperator;
import cryptator.specs.ICryptaNode;
import cryptator.tree.CryptaLeaf;
import cryptator.tree.CryptaNode;

public class CryptaGenModel {

	private final String[] words;

	private final Model model;

	private final CryptaEqnMember left;

	private final CryptaEqnMember right;

	private final Map<Character, BoolVar> symbolsToVariables;
	private final IntVar digitCount;

	public CryptaGenModel(String[] words) {
		this.words = words;
		model = new Model("Generate");		
		left = new CryptaEqnMember(model, words, "L_");
		right = new CryptaEqnMember(model, words, "R_");
		symbolsToVariables = buildSymbolVars(words, model);
		digitCount = model.intVar("digitCount", 0, symbolsToVariables.size());
		buildCoreModel();
	}

	private static Map<Character, BoolVar> buildSymbolVars(String[] words, Model model) {
		final Map<Character, BoolVar> symbolsToVariables = new HashMap<>();
		for (int i = 0; i < words.length; i++) {
			for (char c : words[i].toCharArray()) {
				symbolsToVariables.computeIfAbsent(c, s -> model.boolVar(String.valueOf(s)));
			}
		}
		return symbolsToVariables;
	}

	
	public final String[] getWords() {
		return words;
	}

	public final Model getModel() {
		return model;
	}

	public final CryptaEqnMember getLeft() {
		return left;
	}

	public final CryptaEqnMember getRight() {
		return right;
	}

	private void postSymbolPresenceConstraint(Map<Character, List<BoolVar>> symbolsToExpressions) {
		for (Character c : symbolsToExpressions.keySet()) {
			final Collection<BoolVar> varsL = symbolsToExpressions.get(c);
			final BoolVar[] vars = varsL.toArray(new BoolVar[varsL.size()]);
			model.max(symbolsToVariables.get(c), vars).post();
		}
	}

	private void postLeftOrRightMemberConstraints() {
		for (int i = 0; i < words.length; i++) {
			left.words[i].add(right.words[i]).le(1).post();
		}
	}

	private void postDigitCountConstraint() {
		final IntVar[] symbols = symbolsToVariables.values().toArray(new IntVar[symbolsToVariables.size()]);
		model.sum(symbols, "=", digitCount).post();	
	}

	private Map<Character, List<BoolVar>> buildSymbolWordLists() {
		final Map<Character, List<BoolVar>> map = new HashMap<>();
		for (int i = 0; i < words.length; i++) {
			for (char c : words[i].toCharArray()) {
				final Collection<BoolVar> list = map.computeIfAbsent(c, s -> new ArrayList<>());
				list.add(left.words[i]);
				list.add(right.words[i]);
			}
		}
		return map;
	}
	
	private void postMemberMaxLenConstraint() {
		right.maxLength.ge(left.maxLength).post();
			
	}

	private void buildCoreModel() {
		postLeftOrRightMemberConstraints();
		postSymbolPresenceConstraint(buildSymbolWordLists());
		postDigitCountConstraint();
		postMemberMaxLenConstraint();
	}
	
	public void postMemberCardConstraints(int min, int max) {
		if(min > 1) left.wordCount.ge(min).post();
		else left.wordCount.ge(2).post();
		if(max >= min) left.wordCount.le(max).post();
		
		right.wordCount.eq(1).post();
	}
	
	public void postLeftMinCardConstraints(int base) {
		IntVar diff = right.maxLength.sub(left.maxLength).intVar();
		final int n = words.length;
		int prod = base;
		int i = 2;
		while(prod <= n) {
			diff.ge(i).imp(left.wordCount.ge(prod)).post();
			prod *= base;
			i++;
		}
		diff.lt(i).post();
	}
	
	public void postMaxDigitCountConstraint(int max) {
		digitCount.le(max).post();		
	}
	
	public void postRigtMemberConstraint() {
		BoolVar[] vars = right.getWords();
		vars[vars.length - 1].eq(1).post();
	}
	
	public void postDoublyTrueConstraint(int lb) {
		final int n = words.length;
		final IntVar sum = model.intVar("SUM", lb, n - 1);
		
		final IntVar[] lvars = new IntVar[n+1];
		System.arraycopy(left.getWords(), 0, lvars, 0, n);
		lvars[n] = sum; 
		
		final IntVar[] rvars = new IntVar[n+1];
		System.arraycopy(right.getWords(), 0, rvars, 0, n);
		rvars[n] = sum; 
		
		final int[] coeffs = ArrayUtils.array(0, n);
		coeffs[n] = -1;
		
		model.scalar(lvars, coeffs, "=", 0).post();
		model.scalar(rvars, coeffs, "=", 0).post();
	}

	private ICryptaNode recordMember(CryptaEqnMember member) {
		BoolVar[] vars = member.getWords();
		ICryptaNode node = null;
		for (int i = 0; i < vars.length; i++) {
			if(vars[i].isInstantiatedTo(1)) {
				final CryptaLeaf leaf = new CryptaLeaf(words[i]);
				node = node == null ? leaf : new CryptaNode(CryptaOperator.ADD, node, leaf);
			}
		}
		return node;
	}
	
	public final ICryptaNode recordCryptarithm() {
		return new CryptaNode(CryptaOperator.EQ, recordMember(left), recordMember(right));
	}
	

}
