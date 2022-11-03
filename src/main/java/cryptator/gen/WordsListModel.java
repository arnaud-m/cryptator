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
	
/**
 * The Class WordsListModel defines a CP model for mapping words to symbols.
 * A symbol is present if and only if at least one word that contains the symbol is present.
 */
public class WordsListModel {

	/** The model. */
	protected final Model model;

	/** The words list. */
	protected final String[] words;

	/** The word variables that indicates if the word is present. */
	protected final BoolVar[] wordVariables;

	/** The word count. */
	private final IntVar wordCount;

	/** The map that associates a variable to each symbol of the words. */
	private final Map<Character, BoolVar> symbolsToVariables;

	/** The symbol count. */
	private final IntVar symbolCount;
	

	/**
	 * Instantiates a new words list model.
	 *
	 * @param model the model to use
	 * @param words the words list
	 */
	public WordsListModel(Model model, String[] words) {
		this.model = model;		
		this.words = words;
		wordVariables = buildWordVars(model, words, "");
		wordCount = buildCountVariable(model, "wordCount", wordVariables);
		symbolsToVariables = buildSymbolVars(model, words);
		symbolCount = buildSymbolCountVariable(model, symbolsToVariables);
		postChannelingConstraints();
	}

	/**
	 * Gets the words list.
	 *
	 * @return the words
	 */
	public final String[] getWords() {
		return words;
	}

	/**
	 * Gets the CP model.
	 *
	 * @return the model
	 */
	public final Model getModel() {
		return model;
	}


	/**
	 * Builds named boolean variables associated to the words. 
	 *
	 * @param model the model
	 * @param words the words list
	 * @param prefix the prefix for the variable names
	 * @return the array of boolean variables
	 */
	protected static BoolVar[] buildWordVars(Model model, String[] words, String prefix) {
		final BoolVar[] vars = new BoolVar[words.length];
		for (int i = 0; i < words.length; i++) {
			vars[i] = model.boolVar(prefix + words[i]);
		}
		return vars;
	}
	
	/**
	 * Builds the mapping between symbols and boolean variables.
	 *
	 * @param model the model
	 * @param words the words list
	 * @return the map that associates variables to symbols.
	 */
	private static Map<Character, BoolVar> buildSymbolVars(Model model, String[] words) {
		final Map<Character, BoolVar> symbolsToVariables = new HashMap<>();
		for (int i = 0; i < words.length; i++) {
			for (char c : words[i].toCharArray()) {
				symbolsToVariables.computeIfAbsent(c, s -> model.boolVar(String.valueOf(s)));
			}
		}
		return symbolsToVariables;
	}
	

	/**
	 * Builds the symbol count variable and post the required sum constraint.
	 *
	 * @param model the model
	 * @param symbolsToVariables the variables associated to symbols 
	 * @return the variable that gives the number of symbols.
	 */
	private IntVar buildSymbolCountVariable(Model model, Map<Character, BoolVar>  symbolsToVariables) {
		final BoolVar[] symbols = symbolsToVariables.values().toArray(new BoolVar[symbolsToVariables.size()]);
		return buildCountVariable(model, "digitCount", symbols);
	}
	
	/**
	 * Builds the count variable that indicates how many boolean variables are true.
	 *
	 * @param model the model
	 * @param name the name of the count variable
	 * @param vars the boolean variables to be counted
	 * @return the variable giving the number of variables that are true.
	 */
	private static IntVar buildCountVariable(Model model, String name, BoolVar[] vars) {
		final IntVar count = model.intVar(name, 0, vars.length);
		model.sum(vars, "=", count).post();
		return count;
	}

	/**
	 * Builds the mapping between each symbol and the boolean variables associated to words that contains the symbol.
	 *
	 * @return the map that gives the boolean variables that contain the symbols.
	 */
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
	
	/**
	 * Post channeling constraints between symbols and words.
	 * A symbol is present if and only if one word that contains the symbol is present.
	 */
	private void postChannelingConstraints() {
		final Map<Character, List<BoolVar>> symbolsToWords = buildSymbolsToWords();
		for (Map.Entry<Character, List<BoolVar>> entry : symbolsToWords.entrySet()) {
			final Collection<BoolVar> varsL = entry.getValue();
			final BoolVar[] vars = varsL.toArray(new BoolVar[varsL.size()]);
			model.max(symbolsToVariables.get(entry.getKey()), vars).post();
		}
	}
	
	/**
	 * Post a constraint over the maximum number of distinct symbols in the words.
	 *
	 * @param max the maximum number of symbols
	 */
	public void postMaxSymbolCountConstraint(int max) {
		symbolCount.le(max).post();		
	}

	/**
	 * Post a constraint over the maximum number of distinct words.
	 *
	 * @param max the maximum number of words
	 */
	public void postMaxWordCountConstraint(int max) {
		wordCount.le(max).post();		
	}
	

}
