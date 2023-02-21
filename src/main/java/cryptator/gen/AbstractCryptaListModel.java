/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
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

import cryptator.specs.ICryptaGenSolver;

/**
 * The Class WordsListModel defines a CP model for mapping words to symbols. A
 * symbol is present if and only if at least one word that contains the symbol
 * is present.
 */
public abstract class AbstractCryptaListModel extends AbstractCryptaGenModel implements ICryptaGenSolver {

    /** The map that associates a variable to each symbol of the words. */
    protected final Map<Character, BoolVar> symbolsToVariables;

    /** The symbol count. */
    protected final IntVar symbolCount;

    /**
     * Instantiates a new words list model.
     *
     * @param model the model to use
     * @param words the words list
     */
    protected AbstractCryptaListModel(final Model model, final String[] words) {
        super(model, words, "");
        symbolsToVariables = buildSymbolVars(model, words);
        symbolCount = model.intVar("symbCount", 0, symbolsToVariables.size());
    }

    public final IntVar getSymbolCount() {
        return symbolCount;
    }

    @Override
    public void buildModel() {
        super.buildModel();
        postSymbolCountConstraint();
        postChannelingConstraints();
    }

    public abstract void postFixedRightMemberConstraints();

    public abstract void postDoublyTrueConstraints(final int lowerBound);

    public abstract void postPrecisionConstraints(int base);

    public abstract void postHeavyConstraints(final int base);

    /**
     * Post a constraint over the maximum number of distinct symbols in the words.
     *
     * @param max the maximum number of symbols
     */
    public void postMaxSymbolCountConstraint(final int max) {
        symbolCount.le(max).post();
    }

    private void postSymbolCountConstraint() {
        final BoolVar[] symbols = AbstractCryptaListModel.toArray(symbolsToVariables.values());
        model.sum(symbols, "=", symbolCount).post();
    }

    /**
     * Builds the mapping between symbols and boolean variables.
     *
     * @param model the model
     * @param words the words list
     * @return the map that associates variables to symbols.
     */
    private static Map<Character, BoolVar> buildSymbolVars(final Model model, final String[] words) {
        final Map<Character, BoolVar> symbolsToVariables = new HashMap<>();
        for (String word : words) {
            for (char c : word.toCharArray()) {
                symbolsToVariables.computeIfAbsent(c, s -> model.boolVar("symb_" + s));
            }
        }
        return symbolsToVariables;
    }

    /**
     * Builds the mapping between each symbol and the boolean variables associated
     * to words that contains the symbol.
     *
     * @return the map that gives the boolean variables that contain the symbols.
     */
    private Map<Character, List<BoolVar>> buildSymbolsToWords() {
        final Map<Character, List<BoolVar>> map = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            for (char c : words[i].toCharArray()) {
                final Collection<BoolVar> list = map.computeIfAbsent(c, s -> new ArrayList<>());
                list.add(vwords[i]);
            }
        }
        return map;
    }

    /**
     * Post channeling constraints between symbols and words. A symbol is present if
     * and only if one word that contains the symbol is present.
     */
    private void postChannelingConstraints() {
        final Map<Character, List<BoolVar>> symbolsToWords = buildSymbolsToWords();
        for (Map.Entry<Character, List<BoolVar>> entry : symbolsToWords.entrySet()) {
            final BoolVar max = symbolsToVariables.get(entry.getKey());
            final BoolVar[] vars = AbstractCryptaListModel.toArray(entry.getValue());
            model.max(max, vars).post();
        }
    }

    private static BoolVar[] toArray(final Collection<BoolVar> vars) {
        return vars.toArray(new BoolVar[vars.size()]);
    }

}
