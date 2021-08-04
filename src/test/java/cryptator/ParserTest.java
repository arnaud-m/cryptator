/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import static cryptator.tree.TreeUtils.*;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;

import org.junit.Test;

import cryptator.parser.CryptaParserException;
import cryptator.parser.CryptaParserWrapper;
import cryptator.specs.ICryptaNode;

public class ParserTest {

	public final CryptaParserWrapper parser = new CryptaParserWrapper();
	
	public ParserTest() {}
	
	public static void testPreorder(String expected, ICryptaNode node) {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		writePreorder(node, os);
		assertEquals(expected, new String(os.toByteArray()));
	}
	
	public static void testPostorder(String expected, ICryptaNode node) {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		writePostorder(node, os);
		assertEquals(expected, new String(os.toByteArray()));
	}

	public static void testInorder(String expected, ICryptaNode node) {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		writeInorder(node, os);
		assertEquals(expected, new String(os.toByteArray()));
	}
	
	@Test
	public void testParser1() throws CryptaParserException {
		final ICryptaNode node = parser.parse("send+more=money");
		testPreorder("= + send more money ", node);
		testPostorder("send more + money = ", node);
		testInorder("send + more = money ", node);
	}
	
	@Test
	public void testParser2() throws CryptaParserException {
		final ICryptaNode node = parser.parse("(send + more1 * more2) >=  money");
		testPreorder(">= + send * more1 more2 money ", node);
		testPostorder("send more1 more2 * + money >= ", node);
		testInorder("send + more1 * more2 >= money ", node);
	}

	@Test
	public void testParser3() throws CryptaParserException {
		final ICryptaNode node = parser.parse("-send/more=money");
		testPreorder("= / - 0 send more money ", node);
		testPostorder("0 send - more / money = ", node);
		testInorder("0 - send / more = money ", node);
	}

	@Test
	public void testParser4() throws CryptaParserException {
		final ICryptaNode node = parser.parse("send/-more<money");
		testPreorder("< / send - 0 more money ", node);
		testPostorder("send 0 more - / money < ", node);
		testInorder("send / 0 - more < money ", node);
	}

	@Test
	public void testParser5() throws CryptaParserException {
		final ICryptaNode node = parser.parse("(send/money)%(send+more)>money");
		testPreorder("> % / send money + send more money ", node);
		testPostorder("send money / send more + % money > ", node);
		testInorder("send / money % send + more > money ", node);
	}

	@Test
	public void testParser6() throws CryptaParserException {
		final ICryptaNode node = parser.parse("(send/money)%(send+more)<=money");
		testPreorder("<= % / send money + send more money ", node);
		testPostorder("send money / send more + % money <= ", node);
		testInorder("send / money % send + more <= money ", node);
	}

	@Test
	public void testParser7() throws CryptaParserException {
		final ICryptaNode node = parser.parse("send+more^more=money");
		testPreorder("= + send ^ more more money ", node);
		testPostorder("send more more ^ + money = ", node);
		testInorder("send + more ^ more = money ", node);
	}

	@Test(expected = CryptaParserException.class)
	public void testParserError1() throws CryptaParserException {
		parser.parse("send + more - money");
	}
	
	@Test(expected = CryptaParserException.class)
	public void testParserError2() throws CryptaParserException {
		parser.parse("send + more - money = = foo");
	}
	
	@Test(expected = CryptaParserException.class)
	public void testParserError3() throws CryptaParserException {
		parser.parse("send + * more = money");
	}
	
	@Test(expected = CryptaParserException.class)
	public void testParserError4() throws CryptaParserException {
		parser.parse("aaaaaaaaa <= bbbbbbbbbb");
	}

	@Test(expected = CryptaParserException.class)
	public void testParserError5() throws CryptaParserException {
		parser.parse("s&nd+more=0");
	}

	@Test(expected = CryptaParserException.class)
	public void testParserError6() throws CryptaParserException {
		parser.parse("send + more =< money");
	}

	@Test(expected = CryptaParserException.class)
	public void testParserError7() throws CryptaParserException {
		parser.parse("[send + more] >= money");
	}

	@Test(expected = CryptaParserException.class)
	public void testParserError8() throws CryptaParserException {
		parser.parse("send + more > = money");
	}

}
