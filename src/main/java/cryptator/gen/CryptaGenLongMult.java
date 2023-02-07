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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.tools.ArrayUtils;

import cryptator.CryptaOperator;
import cryptator.solver.AdaptiveSolver;
import cryptator.specs.ICryptaGenSolver;
import cryptator.specs.ICryptaNode;
import cryptator.tree.CryptaConstant;
import cryptator.tree.CryptaLeaf;
import cryptator.tree.CryptaNode;

public class CryptaGenLongMult extends AbstractCryptaListModel implements ICryptaGenSolver {

    public final CryptaLongMultModel longMult;

    // FIXME Set value according to the config !
    private int arithmeticBase = 10;

    public CryptaGenLongMult(String[] words) {
        super(new Model("Generate-Long-Multiplication"), words);
        final int[] lengths = getLengths(words);
        final int[] cards = getCards(words);
        longMult = new CryptaLongMultModel(model, lengths, cards);
    }

    @Override
    public void buildModel() {
        super.buildModel();
        longMult.buildModel();
    }

    @Override
    protected void postWordConstraints() {
        final int n = getN();
        final BoolVar[] vars = Arrays.copyOf(vwords, n + 1);
        vars[n] = model.boolVar(true);
        final SetVar wordSet = model.setVar("wordSet", new int[] {}, ArrayUtils.linspace(0, n + 1));
        model.setBoolsChanneling(vars, wordSet).post();

        final IntVar[] indices = longMult.getWordIndices();
        model.union(indices, wordSet).post();
    }

    @Override
    protected void postMaxLengthConstraints() {
        maxLength.eq(longMult.getProductLength()).post();
    }

    public void postFixedRightMemberConstraint() {
        longMult.getProduct().eq(getN() - 1).post();
    }

    @Override
    public void postDoublyTrueConstraint(int lowerBound) {
        // TODO post doubly true constraints for the long multiplication
        System.err.println("Not yet implemented");
    }

    @Override
    public void postMinLeftCountConstraints(int base) {
        // Nothing to do
    }

    @Override
    public void postPrecisionConstraints(int base) {
        final int thresh = AdaptiveSolver.computeThreshold(base);
        longMult.getProductLength().le(thresh).post();
    }

    private String[] getTermWords() {
        final int[] terms = longMult.getTermIndices();
        final char[] mult = words[longMult.getMultiplierIndex()].toCharArray();
        final String[] twords = new String[mult.length];
        Map<Character, String> map = new HashMap<>();
        int j = 0;
        for (int i = mult.length - 1; i >= 0; i--) {
            if (!map.containsKey(mult[i])) {
                map.put(mult[i], words[terms[j++]]);
            }
            twords[i] = map.get(mult[i]);
        }
        return twords;
    }

    private static ICryptaNode recordTermAddition(final String[] terms, String product, int base) {
        ArrayList<ICryptaNode> nodes = new ArrayList<>();
        int exponent = 1;
        for (int i = terms.length - 1; i >= 0; i--) {
            nodes.add(new CryptaNode(CryptaOperator.MUL, new CryptaLeaf(terms[i]),
                    new CryptaConstant(Integer.toString(exponent))));
            exponent *= base;
        }
        ICryptaNode addition = GenerateUtil.reduceOperation(CryptaOperator.ADD, nodes.stream());
        return new CryptaNode(CryptaOperator.EQ, addition, new CryptaLeaf(product));
    }

    private static ICryptaNode recordTermMultiplications(String[] terms, String multiplicand, String multiplier) {
        final char[] multipliers = multiplier.toCharArray();
        final ArrayList<ICryptaNode> nodes = new ArrayList<>();
        for (int i = 0; i < terms.length; i++) {
            nodes.add(GenerateUtil.recordMultiplication(multiplicand, Character.toString(multipliers[i]), terms[i]));
        }
        return GenerateUtil.reduceOperation(CryptaOperator.AND, nodes.stream());
    }

    @Override
    public ICryptaNode recordCryptarithm() {

        final String multiplier = words[longMult.getMultiplierIndex()];
        final String multiplicand = words[longMult.getMultiplicandIndex()];
        final String product = words[longMult.getProductIndex()];
        final String[] terms = getTermWords();
        return GenerateUtil.reduceOperation(CryptaOperator.AND,
                GenerateUtil.recordMultiplication(multiplicand, multiplier, product),
                recordTermAddition(terms, product, arithmeticBase),
                recordTermMultiplications(terms, multiplicand, multiplier));
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(words[longMult.getMultiplicandIndex()]).append(" * ").append(words[longMult.getMultiplierIndex()]);
        final int[] terms = longMult.getTermIndices();
        b.append(" |=");
        for (int i : terms) {
            b.append(" ").append(words[i]);
        }
        b.append(" |= ").append(words[longMult.getProductIndex()]);

        return b.toString();
    }

}
