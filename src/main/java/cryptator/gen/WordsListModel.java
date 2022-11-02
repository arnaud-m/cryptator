/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
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

public class WordsListModel {

	protected final Model model;

	protected final String[] words;

	protected final BoolVar[] wordVariables;

	private final IntVar wordCount;

	private final Map<Character, BoolVar> symbolsToVariables;

	private final IntVar symbolCount;
	

	public WordsListModel(Model model, String[] words) {
		this.model = model;		
		this.words = words;
		wordVariables = buildWordVars(model, words, "");
		wordCount = buildCountVariable(model, "wordCount", wordVariables);
		symbolsToVariables = buildSymbolVars(model, words);
		symbolCount = buildSymbolCountVariable(model, symbolsToVariables);
		postChannelingConstraints();
	}

	public final String[] getWords() {
		return words;
	}

	public final Model getModel() {
		return model;
	}


	protected static BoolVar[] buildWordVars(Model model, String[] words, String prefix) {
		final BoolVar[] vars = new BoolVar[words.length];
		for (int i = 0; i < words.length; i++) {
			vars[i] = model.boolVar(prefix + words[i]);
		}
		return vars;
	}
	
	private static Map<Character, BoolVar> buildSymbolVars(Model model, String[] words) {
		final Map<Character, BoolVar> symbolsToVariables = new HashMap<>();
		for (int i = 0; i < words.length; i++) {
			for (char c : words[i].toCharArray()) {
				symbolsToVariables.computeIfAbsent(c, s -> model.boolVar(String.valueOf(s)));
			}
		}
		return symbolsToVariables;
	}
	

	private IntVar buildSymbolCountVariable(Model model, Map<Character, BoolVar>  symbolsToVariables) {
		final BoolVar[] symbols = symbolsToVariables.values().toArray(new BoolVar[symbolsToVariables.size()]);
		return buildCountVariable(model, "digitCount", symbols);
	}
	
	private static IntVar buildCountVariable(Model model, String name, BoolVar[] vars) {
		final IntVar count = model.intVar(name, 0, vars.length);
		model.sum(vars, "=", count).post();
		return count;
	}

	private Map<Character, List<BoolVar>> buildSymbolsToWords() {
		final Map<Character, List<BoolVar>> map = new HashMap<>();
		for (int i = 0; i < words.length; i++) {
			for (char c : words[i].toCharArray()) {
				final Collection<BoolVar> list = map.computeIfAbsent(c, s -> new ArrayList<>());
				list.add(wordVariables[i]);
			}
		}
		return map;
	}
	
	private void postChannelingConstraints() {
		final Map<Character, List<BoolVar>> symbolsToWords = buildSymbolsToWords();
		for (Map.Entry<Character, List<BoolVar>> entry : symbolsToWords.entrySet()) {
			final Collection<BoolVar> varsL = entry.getValue();
			final BoolVar[] vars = varsL.toArray(new BoolVar[varsL.size()]);
			model.max(symbolsToVariables.get(entry.getKey()), vars).post();
		}
	}
	
	public void postMaxDigitCountConstraint(int max) {
		symbolCount.le(max).post();		
	}

	public void postMaxWordCountConstraint(int max) {
		wordCount.le(max).post();		
	}

	public static void main(String[] args) {
		WordsListModel m = new WordsListModel(new Model(), new String[] {"send", "more", "money"});
		System.out.println(m.getModel());
	}
	

}
