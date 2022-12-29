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

    protected AbstractCryptaGenModel(final Model model, final String[] words, final String prefix,
            final boolean boundedDomain) {
        super();
        this.model = model;
        this.words = words;
        this.vwords = buildWordVars(model, words, prefix);
        this.wordCount = model.intVar(prefix + "wordCount", 0, words.length);
        this.maxLength = model.intVar(prefix + "maxLength", 0, AbstractCryptaGenModel.getMaxLength(words),
                boundedDomain);
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

    protected abstract void postWordConstraints();

    protected void postWordCountConstraint() {
        model.sum(vwords, "=", wordCount).post();
    }

    protected abstract void postMaxLengthConstraints();

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

    public void postWordCountConstraints(final int val) {
        wordCount.eq(val).post();
    }

    @Override
    public String toString() {
        return GenerateUtil.recordString(this, " ");
    }

    protected static int getMaxLength(final String[] words) {
        return Arrays.stream(words).mapToInt(String::length).max().orElse(0);
    }

    protected static int[] getLengths(final String[] words) {
        return Arrays.stream(words).mapToInt(String::length).toArray();
    }
}
