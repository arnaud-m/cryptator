/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.tree;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.function.Predicate;

import cryptator.CryptaOperator;
import cryptator.specs.ICryptaNode;

public final class TreeUtils {

    private TreeUtils() {
    }

    private static void writeWord(final ICryptaNode node, final PrintWriter out) {
        out.write(node.toGrammarString());
        out.write(" ");
    }

    public static void writePreorder(final ICryptaNode root, final OutputStream outstream) {
        final PrintWriter out = new PrintWriter(outstream);
        TreeTraversals.preorderTraversal(root, (node, num) -> writeWord(node, out));
        out.flush();
    }

    public static String writePreorder(final ICryptaNode root) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TreeUtils.writePreorder(root, out);
        return out.toString();
    }

    public static void printPostorder(final ICryptaNode root) {
        writePostorder(root, System.out);
        System.out.println();
    }

    public static void writePostorder(final ICryptaNode root, final OutputStream outstream) {
        final PrintWriter out = new PrintWriter(outstream);
        TreeTraversals.postorderTraversal(root, (node, num) -> writeWord(node, out));
        out.flush();
    }

    public static String writePostorder(final ICryptaNode root) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TreeUtils.writePostorder(root, out);
        return out.toString();
    }

    private static void writeInorderChild(final ICryptaNode node, final PrintWriter out, final boolean hasPar,
            final Predicate<ICryptaNode> hasLeftPar, final Predicate<ICryptaNode> hasRightPar) {
        if (hasPar) {
            out.write("(");
            writeInorder(node, out, hasLeftPar, hasRightPar);
            out.write(")");
        } else {
            writeInorder(node, out, hasLeftPar, hasRightPar);
        }
    }

    private static void writeInorder(final ICryptaNode node, final PrintWriter out,
            final Predicate<ICryptaNode> hasLeftPar, final Predicate<ICryptaNode> hasRightPar) {
        // Handle internal node or leaf.
        if (node.isInternalNode()) {
            // Write the left child
            writeInorderChild(node.getLeftChild(), out, hasLeftPar.test(node), hasLeftPar, hasRightPar);
            // Write the operator
            out.write(" ");
            out.write(node.getOperator().getToken());
            out.write(" ");
            // Write the right child
            writeInorderChild(node.getRightChild(), out, hasRightPar.test(node), hasLeftPar, hasRightPar);
        } else {
            // Write a leaf
            out.write(node.toGrammarString());
        }

    }

    private static void writeInorder(final ICryptaNode node, final OutputStream outstream,
            final boolean allParenthesis) {
        final PrintWriter out = new PrintWriter(outstream);
        if (allParenthesis) {
            // Left parenthesis
            final Predicate<ICryptaNode> hasLeftPar = n -> n.getOperator().getPriority() > 1
                    && n.getLeftChild().isInternalNode();
            // Right parenthesis
            final Predicate<ICryptaNode> hasRightPar = n -> n.getOperator().getPriority() > 1
                    && n.getRightChild().isInternalNode();
            // Recursive traversal
            writeInorder(node, out, hasLeftPar, hasRightPar);
        } else {
            // Left parenthesis
            final Predicate<ICryptaNode> hasLeftPar = n -> n.getOperator().getPriority() > n.getLeftChild()
                    .getOperator().getPriority();

            // Right parenthesis
            final Predicate<ICryptaNode> hasRightPar = n -> {
                final int p1 = n.getOperator().getPriority();
                final int p2 = n.getRightChild().getOperator().getPriority();
                return p1 > p2 || (p1 == p2 && !n.getOperator().isCommutative());
            };
            // Recursive traversal
            writeInorder(node, out, hasLeftPar, hasRightPar);
        }
        out.flush();
    }

    public static String writeInorder(final ICryptaNode root, final boolean allParenthesis) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TreeUtils.writeInorder(root, out, allParenthesis);
        return out.toString();
    }

    public static String writeInorder(final ICryptaNode root) {
        return writeInorder(root, false);
    }

    public static void printInorder(final ICryptaNode root) {
        printInorder(root, false);
    }

    public static void printInorder(final ICryptaNode root, final boolean allParenthesis) {
        writeInorder(root, System.out, allParenthesis);
        System.out.println();
    }

    public static CryptaFeatures computeFeatures(final ICryptaNode cryptarithm) {
        final CryptaFeatures feat = new CryptaFeatures();
        TreeTraversals.preorderTraversal(cryptarithm, feat);
        return feat;
    }

    public static CryptaOperatorDetection computeUnsupportedBignumOperator(final ICryptaNode cryptarithm) {
        final CryptaOperatorDetection detect = new CryptaOperatorDetection(CryptaOperator.ID, CryptaOperator.ADD,
                CryptaOperator.MUL, CryptaOperator.EQ, CryptaOperator.AND);
        TreeTraversals.preorderTraversal(cryptarithm, detect);
        return detect;
    }

    public static char[] computeSymbols(final ICryptaNode cryptarithm) {
        final CryptaSymbols sym = new CryptaSymbols();
        TreeTraversals.preorderTraversal(cryptarithm, sym);
        return sym.getSymbols();
    }

}
