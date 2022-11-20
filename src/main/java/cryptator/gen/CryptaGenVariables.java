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
import java.util.Collection;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import cryptator.specs.ICryptaGenVariables;

/**
 * Base class for generation model..
 */
class CryptaGenVariables implements ICryptaGenVariables {


	/** The model. */
	protected final Model model;

	/** The word array . */
	protected final String[] words;

	/** The word variables that indicates if the word is present. */
	protected final BoolVar[] vwords;

	/** The word count. */
	protected final IntVar wordCount;

	/** The maximum length of a present word. */
	protected final IntVar maxLength;

	public CryptaGenVariables(Model model, String[] words, String prefix, boolean boundedDomain) {
		super();
		this.model = model;
		this.words = words;
		this.vwords = buildWordVars(model, words, prefix);
		this.wordCount = model.intVar(prefix+ "wordCount", 0, words.length);
		this.maxLength = model.intVar(prefix+ "maxLength", 0, getMaxLength(words), boundedDomain);
	}

	@Override
	public final Model getModel() {
		return model;
	}

	@Override
	public final String[] getWords() {
		return words;
	}

	@Override
	public final BoolVar[] getWordVars() {
		return vwords;
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
	
	protected void postWordCountConstraint() {
		model.sum(vwords, "=", wordCount).post();
	}
	
	protected void postMaxLengthConstraints() {
		maxLength.eq(0).decompose().post();
	}
	
	@Override
	public void buildModel() {
		postWordCountConstraint();
		postMaxLengthConstraints();
	}

	public static int getMaxLength(String[] words) {
		return Arrays.stream(words).mapToInt(String::length).max().orElse(0);
	}
	
	public static BoolVar[] toArray(Collection<BoolVar> vars) {
		return vars.toArray(new BoolVar[vars.size()]);
	}

	public String toString(String separator) {
		final StringBuilder b = new StringBuilder();
		// Add words
		for (int i = 0; i < vwords.length; i++) {
			if(vwords[i].isInstantiatedTo(1)) b.append(words[i]).append(separator); 
		}
		// Trim the buffer
		final int n = b.length();
		final int s = separator.length();
		if(n >= s ) b.delete(n - s, n);
		return b.toString();
	}

	@Override
	public String toString() {
		return toString(" ");
	}
}