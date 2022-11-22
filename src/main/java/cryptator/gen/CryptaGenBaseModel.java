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
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import cryptator.CryptaOperator;
import cryptator.specs.ICryptaGenModel;
import cryptator.specs.ICryptaNode;
import cryptator.tree.CryptaLeaf;
import cryptator.tree.CryptaNode;

/**
 * Base class for generation model..
 */
class CryptaGenBaseModel implements ICryptaGenModel {


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

	public CryptaGenBaseModel(Model model, String[] words, String prefix, boolean boundedDomain) {
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

	public static int[] getLengthCounts(String[] words) {
		final int n = getMaxLength(words);
		int[] v = new int[n + 1];
		for (String w : words) {
			v[w.length()]++;
		}
		return v;
	}

	public static BoolVar[] toArray(Collection<BoolVar> vars) {
		return vars.toArray(new BoolVar[vars.size()]);
	}

	public static Stream<String> wordStream(ICryptaGenModel model) {
		final BoolVar[] v = model.getWordVars();
		final String[] w = model.getWords();
		return IntStream.range(0, model.getN())
				.filter(i -> v[i].isInstantiatedTo(1))
				.mapToObj(i -> w[i]);
	}

	public static String recordString(ICryptaGenModel model, String separator) {
		return wordStream(model).collect( Collectors.joining(separator ) );
	}
		
	public static ICryptaNode recordAddition(ICryptaGenModel model) {
		BinaryOperator<ICryptaNode> add = (a, b) ->  {
			return a == null ? b : new CryptaNode(CryptaOperator.ADD, a, b);
		};
		return wordStream(model)
				.map(CryptaLeaf::new)
				.map(ICryptaNode.class::cast)
				.reduce(null, add);	
	}
	
	
	@Override
	public String toString() {
		return recordString(this, " + ");
	}

}