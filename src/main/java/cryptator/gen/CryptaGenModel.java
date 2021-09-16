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
import java.util.function.BiConsumer;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import cryptator.CryptaBiConsumer;
import cryptator.CryptaOperator;
import cryptator.Cryptator;
import cryptator.JULogUtil;
import cryptator.solver.CryptaModelException;
import cryptator.specs.ICryptaNode;
import cryptator.tree.CryptaLeaf;
import cryptator.tree.CryptaNode;
import cryptator.tree.TreeUtils;


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
				if(! symbolsToVariables.containsKey(c) ) {
					symbolsToVariables.put(c, model.boolVar(String.valueOf(c)));
				} 
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
				if(! map.containsKey(c) ) {
					map.put(c, new ArrayList<>());
				}
				final Collection<BoolVar> list = map.get(c);
				list.add(left.words[i]);
				list.add(right.words[i]);
			}
		}
		return map;
	}

	private void buildCoreModel() {
		postLeftOrRightMemberConstraints();
		postSymbolPresenceConstraint(buildSymbolWordLists());
		postDigitCountConstraint();
	}
	
	public void postMemberCardConstraints() {
		left.wordCount.ge(2).post();
		right.wordCount.eq(1).post();
	}
	
	public void postMemberMaxLenConstraint() {
		right.maxLength.ge(left.maxLength).post();
		//TODO maxLenR.sub(maxLenL).gt(1).imp(wordCountL.ge(10)).post();	
	}
	
	public void postMaxDigitCountConstraint(int max) {
		digitCount.le(max).post();		
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
	

	public static void main(String[] args) throws CryptaModelException {
		JULogUtil.configureLoggers();
		CryptaWordListGenerator gen = new CryptaWordListGenerator(args);
		CryptaBiConsumer cons = new CryptaBiConsumer(Cryptator.LOGGER);
		cons.withSolutionLog();
		gen.generate(cons);
		
	}

}
