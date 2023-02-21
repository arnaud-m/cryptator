/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen;

import java.util.Arrays;
import java.util.function.ToIntFunction;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import cryptator.specs.ICryptaGenModel;

/**
 * Abstract class for the generation model.
 */
public abstract class AbstractCryptaGenModel implements ICryptaGenModel {

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

    /**
     * Instantiates a new model for the generation.
     *
     * @param model  the model
     * @param words  the words
     * @param prefix the prefix for variable names
     */
    protected AbstractCryptaGenModel(final Model model, final String[] words, final String prefix) {
        super();
        this.model = model;
        this.words = words;
        this.vwords = buildWordVars(model, words, prefix);
        this.wordCount = model.intVar(prefix + "wordCount", 0, words.length, false);
        this.maxLength = model.intVar(prefix + "maxLength", 0, AbstractCryptaGenModel.getMaxLength(words), false);
    }

    /**
     * Gets the model.
     *
     * @return the model
     */
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
     * @param model  the model
     * @param words  the words list
     * @param prefix the prefix for the variable names
     * @return the array of boolean variables
     */
    private static BoolVar[] buildWordVars(final Model model, final String[] words, final String prefix) {
        final BoolVar[] vars = new BoolVar[words.length];
        for (int i = 0; i < words.length; i++) {
            vars[i] = model.boolVar(prefix + words[i]);
        }
        return vars;
    }

    /**
     * Post constraints for the presence of words.
     */
    protected abstract void postWordConstraints();

    /**
     * Post word count constraint.
     */
    protected void postWordCountConstraint() {
        model.sum(vwords, "=", wordCount).post();
    }

    /**
     * Post the constraints that set the maximum length.
     */
    protected abstract void postMaxLengthConstraints();

    /**
     * Builds the model.
     */
    @Override
    public void buildModel() {
        postWordConstraints();
        postWordCountConstraint();
        postMaxLengthConstraints();
    }

    /**
     * Post a constraint over the minimum and maximum number of distinct words.
     *
     * @param min the the minimum number of words
     * @param max the maximum number of words (ignored if lower than the min)
     */
    public void postWordCountConstraints(final int min, final int max) {
        wordCount.ge(min).post();
        if (max >= min) {
            wordCount.le(max).post();
        }
    }

    /**
     * Post a constraint that set the number of words.
     *
     * @param val the number of words
     */
    public void postWordCountConstraints(final int val) {
        wordCount.eq(val).post();
    }

    @Override
    public String toString() {
        return GenerateUtil.recordString(this, " ");
    }

    /**
     * Gets the maximum length of the words.
     *
     * @param words the words
     * @return the maximum length
     */
    public static final int getMaxLength(final String[] words) {
        return Arrays.stream(words).mapToInt(String::length).max().orElse(0);
    }

    /**
     * Gets the sum of the word lengths.
     *
     * @param words the words
     * @return the sum of the lengths
     */
    public static final int getSumLength(final String[] words) {
        return Arrays.stream(words).mapToInt(String::length).reduce(0, Integer::sum);
    }

    /**
     * Gets the array of word lengths.
     *
     * @param words the words
     * @return the array of lengths
     */
    public static final int[] getLengths(final String[] words) {
        return Arrays.stream(words).mapToInt(String::length).toArray();
    }

    /**
     * Gets the array of length cardinalities.
     *
     * The cardinality of a word is its number of distinct characters.
     *
     * @param words the words
     * @return the array of cardinalities
     */
    public static final int[] getCards(final String[] words) {
        final ToIntFunction<String> distinctCharCount = s -> (int) s.chars().distinct().count();
        return Arrays.stream(words).mapToInt(distinctCharCount).toArray();
    }

}
