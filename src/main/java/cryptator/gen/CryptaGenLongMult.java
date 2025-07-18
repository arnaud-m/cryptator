/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.tools.ArrayUtils;

import cryptator.CryptaOperator;
import cryptator.gen.pattern.CryptaLongMultModel;
import cryptator.specs.ICryptaNode;
import cryptator.tree.CryptaConstant;
import cryptator.tree.CryptaLeaf;
import cryptator.tree.CryptaNode;

public class CryptaGenLongMult extends AbstractCryptaListModel {

    public final CryptaLongMultModel longMult;

    private final int arithmeticBase;

    public CryptaGenLongMult(final String[] words, final int arithmeticBase) {
        super(new Model("Generate-Long-Multiplication"), words);
        this.arithmeticBase = arithmeticBase;
        final int[] lengths = getLengths(words);
        final int[] cards = getCards(words);
        longMult = new CryptaLongMultModel(model, lengths, cards);
    }

    public final int getArithmeticBase() {
        return arithmeticBase;
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

    public void postFixedRightMemberConstraints() {
        longMult.getProduct().eq(getN() - 1).post();
    }

    @Override
    public void postDoublyTrueConstraints(final int lowerBound) {
        throw new UnsupportedOperationException("Doubly true long multiplication is undefined.");
    }

    @Override
    public void postHeavyConstraints(final int base) {
        // Nothing to do
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

    private static ICryptaNode recordTermAddition(final String[] terms, final String product, final int base) {

        ArrayList<ICryptaNode> nodes = new ArrayList<>();
        BigInteger exponent = BigInteger.valueOf(1);
        for (int i = terms.length - 1; i >= 0; i--) {
            nodes.add(new CryptaNode(CryptaOperator.MUL, new CryptaLeaf(terms[i]),
                    new CryptaConstant(exponent.toString())));
            exponent = exponent.multiply(BigInteger.valueOf(base));
        }
        ICryptaNode addition = GenerateUtil.reduceOperation(CryptaOperator.ADD, nodes.stream());
        return new CryptaNode(CryptaOperator.EQ, addition, new CryptaLeaf(product));
    }

    private static ICryptaNode recordTermMultiplications(final String[] terms, final String multiplicand,
            final String multiplier) {
        final char[] multipliers = multiplier.toCharArray();
        final ArrayList<ICryptaNode> nodes = new ArrayList<>();
        final Set<Character> letters = new HashSet<>();
        for (int i = 0; i < terms.length; i++) {
            Character letter = Character.valueOf(multipliers[i]);
            if (letters.add(letter)) {
                nodes.add(GenerateUtil.recordMultiplication(multiplicand, letter.toString(), terms[i]));
            }
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
