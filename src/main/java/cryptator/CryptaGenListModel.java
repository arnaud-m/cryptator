/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;


class CryptaEqnMember {

	private final int offset;;
	public final BoolVar[] words;
	public final IntVar[] lengths;
	public final IntVar maxLength;
	public final IntVar wordCount;

	public CryptaEqnMember(Model m, String[] words, String prefix) {
		super();
		this.offset = prefix.length();
		this.words = new BoolVar[words.length];
		for (int i = 0; i < words.length; i++) {
			this.words[i] = m.boolVar(prefix + words[i]);
		}	

		lengths = new IntVar[words.length];
		int maxLen = 0;

		for (int i = 0; i < words.length; i++) {
			if(maxLen < words[i].length()) maxLen = words[i].length();
			lengths[i] = this.words[i].mul(words[i].length()).intVar();
		}

		maxLength = m.intVar(prefix + "maxLen", 0, maxLen);
		m.max(maxLength, lengths).post();

		wordCount = m.intVar(prefix + "wordCount", 0, words.length);
		m.sum(this.words, "=", wordCount).post();

	}

	public final BoolVar[] getWords() {
		return words;
	}

	public final IntVar[] getLengths() {
		return lengths;
	}

	public final IntVar getMaxLength() {
		return maxLength;
	}

	public final IntVar getWordCount() {
		return wordCount;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < words.length; i++) {
			// TODO store prefix
			if(words[i].isInstantiatedTo(1)) b.append(words[i].getName().substring(offset)).append(" + "); 
		}
		if(b.length() > 0) b.delete(b.length()-3, b.length());
		return b.toString();
	}



}
public class CryptaGenListModel {

	Model m;

	private final String[] words;
	
	private final Map<Character, BoolVar> symbolsToVariables = new HashMap<>();

	private CryptaEqnMember l;

	private CryptaEqnMember r;

	private IntVar digitCount;

	public CryptaGenListModel(String[] words) {
		this.words = words;
	}


	private void postLenConstraint() {
		r.maxLength.ge(l.maxLength).post();
		// TODO maxLenR.sub(maxLenL).gt(1).imp(wordCountL.ge(10)).post();	
	}


	private void buildSymbolVars(Map<Character, Collection<BoolVar>> symbolsToExpressions) {
		for (Character c : symbolsToExpressions.keySet()) {
			BoolVar max = m.boolVar(String.valueOf(c));
			symbolsToVariables.put(c, max);
			Collection<BoolVar> varsL = symbolsToExpressions.get(c);
			BoolVar[] vars = varsL.toArray(new BoolVar[varsL.size()]);
			m.max(max, vars).post();
		}
	}
	
	private void buildMembers() {
		l = new CryptaEqnMember(m, words, "L_");
		r = new CryptaEqnMember(m, words, "R_");
		for (int i = 0; i < words.length; i++) {
			l.words[i].add(r.words[i]).le(1).post();
		}
	}
	
	private final IntVar[] buildSymbolVarArray() {
		return symbolsToVariables.values().toArray(new IntVar[symbolsToVariables.size()]);
	}
	
	private void postDigitCountingConstraint() {
		final IntVar[] symbols = buildSymbolVarArray();
		digitCount = m.intVar("digitCount", 0, symbols.length);
		m.sum(symbols, "=", digitCount).post();	
	}

	private Map<Character, Collection<BoolVar>> buildExpressions() {
		final Map<Character, Collection<BoolVar>> symbolsToMaxVars = new HashMap<>();
		for (int i = 0; i < words.length; i++) {
			for (char c : words[i].toCharArray()) {
				if(! symbolsToMaxVars.containsKey(c) ) {
					symbolsToMaxVars.put(c, new ArrayList<>());
				} 
				symbolsToMaxVars.get(c).add(l.words[i]);
				symbolsToMaxVars.get(c).add(r.words[i]);
			}
		}
		return symbolsToMaxVars;
	}
	public void modeling() {
		m = new Model("GenerateFromWordList");
		int n = words.length;

		// Core Constraints
		buildMembers();
		
		final Map<Character, Collection<BoolVar>> symbolsToExpressions = buildExpressions();
		buildSymbolVars(symbolsToExpressions);
		postDigitCountingConstraint();
		
		// Structural Constraints
		l.wordCount.ge(2).post();
		r.wordCount.eq(1).post();

		postLenConstraint();

		
		digitCount.le(10).post();; //.or(digitCount.eq(20)).or(digitCount.eq(30)).post();
			


		//wordCountL.eq(13).post();
		System.out.println(m);
		Solution sol = new Solution(m, ArrayUtils.append(l.words, r.words, buildSymbolVarArray()));
		int solutionCount = 0;
		while(m.getSolver().solve()) {
			solutionCount++;
			sol.record();
			//System.out.println(sol);
			System.out.println(l + " = " + r);
		}
		m.getSolver().printStatistics();
		System.out.println(solutionCount);

	}


	public static void main(String[] args) {
		CryptaGenListModel gen = new CryptaGenListModel(args);
		gen.modeling();
	}

}
