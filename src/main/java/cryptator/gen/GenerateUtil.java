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
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.chocosolver.solver.variables.BoolVar;

import cryptator.CryptaOperator;
import cryptator.specs.ICryptaGenModel;
import cryptator.specs.ICryptaNode;
import cryptator.tree.CryptaLeaf;
import cryptator.tree.CryptaNode;

public class GenerateUtil {

    private GenerateUtil() {
        super();
    }

    public static Stream<String> wordStream(final ICryptaGenModel model) {
        final BoolVar[] v = model.getWordVars();
        final String[] w = model.getWords();
        return IntStream.range(0, model.getN()).filter(i -> v[i].isInstantiatedTo(1)).mapToObj(i -> w[i]);
    }

    public static Stream<ICryptaNode> leafStream(final ICryptaGenModel model) {
        return wordStream(model).map(CryptaLeaf::new).map(ICryptaNode.class::cast);
    }

    public static String recordString(final ICryptaGenModel model, final String separator) {
        return wordStream(model).collect(Collectors.joining(separator));
    }

    public static ICryptaNode reduceOperation(final Stream<ICryptaNode> nodeStream, final CryptaOperator operator) {
        BinaryOperator<ICryptaNode> binop = (a, b) -> a == null ? b : new CryptaNode(operator, a, b);
        return nodeStream.reduce(null, binop);
    }

    public static ICryptaNode recordAddition(final ICryptaGenModel model) {
        return reduceOperation(leafStream(model), CryptaOperator.ADD);
    }

    public static int getMaxLength(final String[] words) {
        return Arrays.stream(words).mapToInt(String::length).max().orElse(0);
    }

    public static int[] getLengthCounts(final String[] words) {
        final int n = getMaxLength(words);
        int[] v = new int[n + 1];
        for (String w : words) {
            v[w.length()]++;
        }
        return v;
    }

}
