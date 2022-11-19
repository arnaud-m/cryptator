/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen;

import java.util.Arrays;
import java.util.OptionalInt;
import java.util.function.IntSupplier;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import cryptator.specs.ICryptaGenVariables;

/**
 * Class that create unconstrained variables.
 */
class CryptaGenVariables implements ICryptaGenVariables {


	/** The model. */
	protected final Model model;

	/** The word variables that indicates if the word is present. */
	protected final BoolVar[] words;

	/** The word count. */
	protected final IntVar wordCount;

	/** The maximum length of a present word. */
	protected final IntVar maxLength;

	public CryptaGenVariables(Model model, String[] words, String prefix) {
		super();
		this.model = model;
		this.words = buildWordVars(model, words, prefix);
		this.wordCount = model.intVar(prefix+ "wordCount", 0, words.length);
		final OptionalInt maxLen = Arrays.stream(words).mapToInt(String::length).max();
		this.maxLength = model.intVar(prefix+ "maxLength", 0, maxLen.orElseGet(() -> 0));
	}

	@Override
	public final Model getModel() {
		return model;
	}

	@Override
	public final BoolVar[] getWords() {
		return words;
	}

	@Override
	public final IntVar getWordCount() {
		return wordCount;
	}

	@Override
	public final IntVar getMaxLength() {
		return maxLength;
	}

	/**
	 * Builds named boolean variables associated to the words. 
	 *
	 * @param model the model
	 * @param words the words list
	 * @param prefix the prefix for the variable names
	 * @return the array of boolean variables
	 */
	private static BoolVar[] buildWordVars(Model model, String[] words, String prefix) {
		final BoolVar[] vars = new BoolVar[words.length];
		for (int i = 0; i < words.length; i++) {
			vars[i] = model.boolVar(prefix + words[i]);
		}
		return vars;
	}
	
	protected void postWordCountBoolConstraint() {
		model.sum(words, "=", wordCount).post();

	}
	
}