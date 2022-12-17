/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.tree;

import cryptator.CryptaOperator;
import cryptator.specs.ICryptaNode;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

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

    public static void writeInorder(final ICryptaNode node,
                                    final PrintWriter out, int leftPar, int rightPar,
                                    int putPar, final CryptaOperator oldOp, boolean isLeft,
                                    final boolean allParenthesis){
        CryptaOperator op1 = node.getOperator();
        leftPar += isLeft ? putPar : 0; rightPar += !isLeft ? putPar : 0;
        putPar = (allParenthesis && oldOp != null && oldOp.getPriority() > 1) ||
                (oldOp != null && (op1.getPriority() < oldOp.getPriority() ||
                    (op1.getPriority() == oldOp.getPriority() && !isLeft && !oldOp.isCommutative()))) ? 1 : 0;
        if (node.isInternalNode()){
            writeInorder(node.getLeftChild(), out, leftPar, 0, putPar, op1, true, allParenthesis);
            out.write(op1.getToken() + " ");
            writeInorder(node.getRightChild(), out, 0, rightPar, putPar, op1, false, allParenthesis);
        } else {
            out.write(isLeft ?
                "( ".repeat(leftPar) + node.toGrammarString() + " " :
                node.toGrammarString() + " " + ") ".repeat(rightPar));
        }
    }

    public static void writeInorder(final ICryptaNode root, final OutputStream outstream, boolean allParenthesis) {
        final PrintWriter out = new PrintWriter(outstream);
        writeInorder(root, out, 0, 0, 0, null, false, allParenthesis);
        out.flush();
    }

    public static String writeInorder(final ICryptaNode root, boolean allParenthesis) {
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

    public static void printInorder(final ICryptaNode root, boolean allParenthesis) {
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
                CryptaOperator.EQ, CryptaOperator.AND);
        TreeTraversals.preorderTraversal(cryptarithm, detect);
        return detect;
    }

    public static char[] computeSymbols(final ICryptaNode cryptarithm) {
        final CryptaSymbols sym = new CryptaSymbols();
        TreeTraversals.preorderTraversal(cryptarithm, sym);
        return sym.getSymbols();
    }

}
