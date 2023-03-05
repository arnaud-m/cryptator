/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen;

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

/**
 * The Class GenerateUtil provides utilities for building cryptarithm tree.
 *
 * It is used by generation models for recording their solutions.
 */
public final class GenerateUtil {

    /**
     * Unusable private constructor.
     */
    private GenerateUtil() {
        super();
    }

    /**
     * The stream of present words of the model.
     *
     * @param model the generation model
     * @return the stream of present words
     */
    public static Stream<String> wordStream(final ICryptaGenModel model) {
        final BoolVar[] v = model.getWordVars();
        final String[] w = model.getWords();
        return IntStream.range(0, model.getN()).filter(i -> v[i].isInstantiatedTo(1)).mapToObj(i -> w[i]);
    }

    /**
     * The stream of present word leaves.
     *
     * A leaf is created for each present word of the model.
     *
     * @param model the generation model
     * @return the stream of present leaves
     */
    public static Stream<ICryptaNode> leafStream(final ICryptaGenModel model) {
        return wordStream(model).map(CryptaLeaf::new).map(ICryptaNode.class::cast);
    }

    /**
     * Record a string with the present words.
     *
     * @param model     the generation model
     * @param separator the word separator
     * @return the string of present words
     */
    public static String recordString(final ICryptaGenModel model, final String separator) {
        return wordStream(model).collect(Collectors.joining(separator));
    }

    /**
     * Reduce the nodes by an operation.
     *
     * @param operator the operator
     * @param nodes    the nodes to reduce
     * @return the reduced node
     */
    public static ICryptaNode reduceOperation(final CryptaOperator operator, final ICryptaNode... nodes) {
        return reduceOperation(operator, Stream.of(nodes));
    }

    /**
     * Reduce the nodes by an operation.
     *
     * @param operator   the operator
     * @param nodeStream the stream of nodes to reduce
     * @return the reduced node
     */
    public static ICryptaNode reduceOperation(final CryptaOperator operator, final Stream<ICryptaNode> nodeStream) {
        BinaryOperator<ICryptaNode> binop = (a, b) -> {
            if (a == null) {
                return b;
            }
            return b == null ? a : new CryptaNode(operator, a, b);
        };
        return nodeStream.reduce(null, binop);
    }

    /**
     * Record the addition of present words.
     *
     * @param model the generation model
     * @return the sum node
     */
    public static ICryptaNode recordAddition(final ICryptaGenModel model) {
        return reduceOperation(CryptaOperator.ADD, leafStream(model));
    }

    /**
     * Record the addition of two nodes.
     *
     * @param left  the left node
     * @param right the right node
     * @return the sum node
     */
    public static ICryptaNode recordAddition(final ICryptaGenModel left, final ICryptaGenModel right) {
        return reduceOperation(CryptaOperator.EQ, recordAddition(left), recordAddition(right));
    }

    /**
     * Record the multiplication of present words.
     *
     * @param model the generation model
     * @return the product node
     */
    public static ICryptaNode recordMultiplication(final ICryptaGenModel model) {
        return reduceOperation(CryptaOperator.MUL, leafStream(model));
    }

    /**
     * Record the multiplication of two nodes.
     *
     * @param left  the left node
     * @param right the right node
     * @return the product node
     */
    public static ICryptaNode recordMultiplication(final ICryptaGenModel left, final ICryptaGenModel right) {
        return reduceOperation(CryptaOperator.EQ, recordMultiplication(left), recordMultiplication(right));
    }

    /**
     * Record a short multiplication.
     *
     * @param multiplicand the multiplicand word
     * @param multiplier   the multiplier word
     * @param product      the product word
     * @return the short multiplication node
     */
    public static ICryptaNode recordMultiplication(final String multiplicand, final String multiplier,
            final String product) {
        return new CryptaNode(CryptaOperator.EQ,
                new CryptaNode(CryptaOperator.MUL, new CryptaLeaf(multiplicand), new CryptaLeaf(multiplier)),
                new CryptaLeaf(product));
    }

}
