/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import static cryptator.tree.TreeUtils.computeSymbols;
import static cryptator.tree.TreeUtils.writeInorder;
import static cryptator.tree.TreeUtils.writePostorder;
import static cryptator.tree.TreeUtils.writePreorder;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import cryptator.specs.ICryptaNode;
import cryptator.specs.ITraversalEdgeConsumer;
import cryptator.tree.CryptaConstant;
import cryptator.tree.CryptaLeaf;
import cryptator.tree.CryptaNode;
import cryptator.tree.CryptaOperatorDetection;
import cryptator.tree.TreeTraversals;

public class TreeTest {

    private final ICryptaNode sendMoreMoney = new CryptaNode(CryptaOperator.EQ,
            new CryptaNode(CryptaOperator.ADD, new CryptaLeaf("send"), new CryptaLeaf("more")),
            new CryptaLeaf("money"));

    private ICryptaNode sendMuchMoreMoney = new CryptaNode(CryptaOperator.EQ,
            new CryptaNode(CryptaOperator.ADD, new CryptaLeaf("send"),
                    new CryptaNode(CryptaOperator.ADD, new CryptaLeaf("much"), new CryptaLeaf("more"))),
            new CryptaLeaf("money"));

    public TreeTest() {
    }

    public static void testInorder(final String expected, final ICryptaNode node) {
        testInorder(expected, node, false);
    }

    public static void testInorder(final String expected, final ICryptaNode node, final boolean allParenthesis) {
        assertEquals(expected, writeInorder(node, allParenthesis));
    }

    public static void testPostorder(final String expected, final ICryptaNode node) {
        assertEquals(expected, writePostorder(node));
    }

    public static void testPreorder(final String expected, final ICryptaNode node) {
        assertEquals(expected, writePreorder(node));
    }

    @Test
    public void testSendMoreMoney() throws Exception {
        assertEquals("=", sendMoreMoney.toString());
        assertEquals("+", sendMoreMoney.getLeftChild().toString());
        assertEquals("send", sendMoreMoney.getLeftChild().getLeftChild().toString());
        assertEquals("more", sendMoreMoney.getLeftChild().getRightChild().toString());
        assertEquals("money", sendMoreMoney.getRightChild().toString());

        TreeTest.testPreorder("= + send more money ", sendMoreMoney);
        TreeTest.testPostorder("send more + money = ", sendMoreMoney);
        TreeTest.testInorder("send + more = money", sendMoreMoney);

        assertArrayEquals("demnorsy".toCharArray(), computeSymbols(sendMoreMoney));
    }

    @Test
    public void testSendMuchMoreMoney() throws Exception {
        assertEquals("=", sendMuchMoreMoney.toString());
        assertEquals("+", sendMuchMoreMoney.getLeftChild().toString());
        assertEquals("send", sendMuchMoreMoney.getLeftChild().getLeftChild().toString());
        assertEquals("+", sendMuchMoreMoney.getLeftChild().getRightChild().toString());
        assertEquals("much", sendMuchMoreMoney.getLeftChild().getRightChild().getLeftChild().toString());
        assertEquals("more", sendMuchMoreMoney.getLeftChild().getRightChild().getRightChild().toString());
        assertEquals("money", sendMuchMoreMoney.getRightChild().toString());

        TreeTest.testPreorder("= + send + much more money ", sendMuchMoreMoney);
        TreeTest.testPostorder("send much more + + money = ", sendMuchMoreMoney);
        TreeTest.testInorder("send + much + more = money", sendMuchMoreMoney);

        assertArrayEquals("cdehmnorsuy".toCharArray(), computeSymbols(sendMuchMoreMoney));

    }

    @Test
    public void testOperatorDetection() {
        CryptaOperatorDetection detect = new CryptaOperatorDetection(CryptaOperator.ID, CryptaOperator.EQ);
        TreeTraversals.postorderTraversal(sendMoreMoney, detect);
        assertTrue(detect.hasUnsupportedOperator());
        assertEquals(Set.of(CryptaOperator.ADD), detect.getUnsupportedOperators());

        detect = new CryptaOperatorDetection(CryptaOperator.ID, CryptaOperator.ADD, CryptaOperator.EQ);
        TreeTraversals.postorderTraversal(sendMuchMoreMoney, detect);
        assertFalse(detect.hasUnsupportedOperator());
        assertTrue(detect.getUnsupportedOperators().isEmpty());

    }

    @Test
    public void testEdgeTraversal() {
        EdgeCounter cons = new EdgeCounter();
        TreeTraversals.preorderTraversal(sendMoreMoney, cons);
        assertEquals(4, cons.getCount());

        cons = new EdgeCounter();
        TreeTraversals.preorderTraversal(sendMuchMoreMoney, cons);
        assertEquals(6, cons.getCount());

    }

    @Test
    public void testNodeProperties() {
        ICryptaNode node = new CryptaNode(CryptaOperator.EQ,
                new CryptaNode(CryptaOperator.ADD, new CryptaConstant("0"), new CryptaConstant("0")),
                new CryptaLeaf("word"));

        assertFalse(node.isConstant());
        assertTrue(node.getLeftChild().isConstant());
        assertTrue(node.getLeftChild().getLeftChild().isConstant());
        assertFalse(node.getRightChild().isConstant());

        assertTrue(node.isInternalNode());
        assertTrue(node.getLeftChild().isInternalNode());
        assertFalse(node.getLeftChild().getLeftChild().isInternalNode());
        assertFalse(node.getRightChild().isInternalNode());

    }

    private static class EdgeCounter implements ITraversalEdgeConsumer {

        private int count;

        @Override
        public void accept(final ICryptaNode node, final int numNode, final ICryptaNode father, final int numFather) {
            count++;
        }

        public final int getCount() {
            return count;
        }

    }

}
