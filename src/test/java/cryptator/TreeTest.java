/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import static cryptator.tree.TreeUtils.writeInorder;
import static cryptator.tree.TreeUtils.writePostorder;
import static cryptator.tree.TreeUtils.writePreorder;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;

import org.junit.Test;

import cryptator.specs.ICryptaNode;
import cryptator.tree.CryptaLeaf;
import cryptator.tree.CryptaNode;

public class TreeTest {


	public TreeTest() {}

	@Test
	public void testSendMoreMoney() throws Exception {
		ICryptaNode sendMoreMoney = new CryptaNode(CryptaOperator.EQ,
				new CryptaNode(CryptaOperator.ADD, new CryptaLeaf("send"), new CryptaLeaf("more")),
				new CryptaLeaf("money"));

		assertEquals("=", sendMoreMoney.toString());
		assertEquals("+", sendMoreMoney.getLeftChild().toString());
		assertEquals("send", sendMoreMoney.getLeftChild().getLeftChild().toString());
		assertEquals("more", sendMoreMoney.getLeftChild().getRightChild().toString());
		assertEquals("money", sendMoreMoney.getRightChild().toString());

		TreeTest.testPreorder("= + send more money ", sendMoreMoney);
		TreeTest.testPostorder("send more + money = ", sendMoreMoney);
		TreeTest.testInorder("send + more = money ", sendMoreMoney);
	}

	@Test
	public void testSendMuchMoreMoney() throws Exception {
		ICryptaNode sendMoreMoney = new CryptaNode(CryptaOperator.EQ,
				new CryptaNode(CryptaOperator.ADD, 
						new CryptaLeaf("send"),
						new CryptaNode(CryptaOperator.ADD, new CryptaLeaf("much"), new CryptaLeaf("more"))
						),
				new CryptaLeaf("money"));

		assertEquals("=", sendMoreMoney.toString());
		assertEquals("+", sendMoreMoney.getLeftChild().toString());
		assertEquals("send", sendMoreMoney.getLeftChild().getLeftChild().toString());
		assertEquals("+", sendMoreMoney.getLeftChild().getRightChild().toString());
		assertEquals("much", sendMoreMoney.getLeftChild().getRightChild().getLeftChild().toString());
		assertEquals("more", sendMoreMoney.getLeftChild().getRightChild().getRightChild().toString());
		assertEquals("money", sendMoreMoney.getRightChild().toString());

		TreeTest.testPreorder("= + send + much more money ", sendMoreMoney);
		TreeTest.testPostorder("send much more + + money = ", sendMoreMoney);
		TreeTest.testInorder("send + much + more = money ", sendMoreMoney);
	}

	public static void testInorder(String expected, ICryptaNode node) {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		writeInorder(node, os);
		assertEquals(expected, os.toString());
	}

	public static void testPostorder(String expected, ICryptaNode node) {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		writePostorder(node, os);
		assertEquals(expected, os.toString());
	}

	public static void testPreorder(String expected, ICryptaNode node) {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		writePreorder(node, os);
		assertEquals(expected, os.toString());
	}







}


