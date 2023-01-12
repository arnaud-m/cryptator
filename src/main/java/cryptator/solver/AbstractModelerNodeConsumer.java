/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;

import cryptator.config.CryptaConfig;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ITraversalNodeConsumer;

public abstract class AbstractModelerNodeConsumer implements ITraversalNodeConsumer {

    protected final Model model;
    protected final CryptaConfig config;
    protected final Map<Character, IntVar> symbolsToVariables;
    protected final Set<Character> firstSymbols;

    protected AbstractModelerNodeConsumer(final Model model, final CryptaConfig config) {
        super();
        this.model = model;
        this.config = config;
        symbolsToVariables = new HashMap<>();
        firstSymbols = new HashSet<>();
    }

    private IntVar createSymbolVar(final char symbol) {
        return model.intVar(String.valueOf(symbol), 0, config.getArithmeticBase() - 1, false);
    }

    protected IntVar getSymbolVar(final char symbol) {
        return symbolsToVariables.computeIfAbsent(symbol, this::createSymbolVar);
    }

    private IntVar[] getGCCVars() {
        final Collection<IntVar> vars = symbolsToVariables.values();
        return vars.toArray(new IntVar[vars.size()]);
    }

    private IntVar[] getGCCOccs(final int lb, final int ub) {
        return model.intVarArray("O", config.getArithmeticBase(), lb, ub, false);
    }

    @Override
    public void accept(final ICryptaNode node, final int numNode) {
        if (node.isWord()) {
            final char[] w = node.getWord();
            if (w.length > 0) {
                firstSymbols.add(node.getWord()[0]);
            }
        }
    }

    private void postFirstSymbolConstraints() {
        if (!config.getAllowLeadingZeros()) {
            for (Character symbol : firstSymbols) {
                getSymbolVar(symbol).gt(0).post();
            }
        }
    }

    private Constraint globalCardinalityConstraint() {
        final IntVar[] vars = getGCCVars();
        final int n = vars.length;
        if (n == 0) {
            return model.trueConstraint();
        }

        final int maxOcc = config.getMaxDigitOccurence(n);
        if (maxOcc == 1) {
            return model.allDifferent(vars);
        } else {
            final int minOcc = config.getMinDigitOccurence(n);
            final int[] values = ArrayUtils.array(0, config.getArithmeticBase() - 1);
            return model.globalCardinality(vars, values, getGCCOccs(minOcc, maxOcc), true);

        }
    }

    protected abstract void postCryptarithmEquationConstraint() throws CryptaModelException;

    public void postConstraints() throws CryptaModelException {
        postFirstSymbolConstraints();
        globalCardinalityConstraint().post();
        postCryptarithmEquationConstraint();
    }

    public void configureSearch() {
        // model.getSolver().showDecisions();
        final int searchType = config.getSearchStrategy();
        if (searchType == 1) {
            IntVar[] vars = symbolsToVariables.values().toArray(new IntVar[symbolsToVariables.size()]);
            model.getSolver().setSearch(Search.intVarSearch(vars));
        }
    }

    public CryptaModel buildCryptaModel() {
        return new CryptaModel(model, symbolsToVariables);
    }

}
