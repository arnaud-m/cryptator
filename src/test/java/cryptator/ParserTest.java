/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import cryptator.parser.CryptaParserException;
import cryptator.parser.CryptaParserWrapper;
import cryptator.specs.ICryptaNode;
import org.junit.Test;

import static cryptator.TreeTest.*;

public class ParserTest {

    public final CryptaParserWrapper parser = new CryptaParserWrapper();
	public static final String ZERO = "'0'";

    public ParserTest() {
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
        testPreorder("= / - " + ParserTest.ZERO + " send more money ", node);
        testPostorder("" + ParserTest.ZERO + " send - more / money = ", node);
        testInorder("" + ParserTest.ZERO + " - send / more = money ", node);
    }

    @Test
    public void testParser4() throws CryptaParserException {
        final ICryptaNode node = parser.parse("send/-more<money");
        testPreorder("< / send - " + ParserTest.ZERO + " more money ", node);
        testPostorder("send " + ParserTest.ZERO + " more - / money < ", node);
        testInorder("send / " + ParserTest.ZERO + " - more < money ", node);
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

    @Test
    public void testParser8() throws CryptaParserException {
        final ICryptaNode node = parser.parse("-send -more = -money");
        testPreorder("= - - " + ParserTest.ZERO + " send more - " + ParserTest.ZERO + " money ", node);
        testPostorder(ParserTest.ZERO + " send - more - " + ParserTest.ZERO + " money - = ", node);
        testInorder(ParserTest.ZERO + " - send - more = " + ParserTest.ZERO + " - money ", node);
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
        parser.parse("send + more > = money");
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

    @Test
    public void testParserAND() throws CryptaParserException {
        final ICryptaNode node = parser.parse("send+more=money; d+e>=y");

        testPreorder("; = + send more money >= + d e y ", node);
        testPostorder("send more + money = d e + y >= ; ", node);
        testInorder("send + more = money ; d + e >= y ", node);
    }

    @Test
    public void testParserAND2() throws CryptaParserException {
        final ICryptaNode node = parser.parse("send+more=	money; -send -more= \n -money");

        testPreorder("; = + send more money = - - " + ParserTest.ZERO + " send more - " + ParserTest.ZERO + " money ", node);
        testPostorder("send more + money = " + ParserTest.ZERO + " send - more - " + ParserTest.ZERO + " money - = ; ", node);
        testInorder("send + more = money ; " + ParserTest.ZERO + " - send - more = " + ParserTest.ZERO + " - money ", node);

    }

    @Test
    public void testParserAND3() throws CryptaParserException {
        var str = "send + more ^ more = money ";
        final ICryptaNode node = parser.parse(str + ";" + str);
        var preord = "= + send ^ more more money ";
        testPreorder("; " + preord + preord, node);
        var postord = "send more more ^ + money =";
        testPostorder(postord + " " + postord + " ; ", node);
        testInorder(str + "; " + str, node);
    }

    @Test
    public void testParserAND4() throws CryptaParserException {
        final ICryptaNode node = parser.parse("send+more=money;; d+e>=y");

        testPreorder("; = + send more money >= + d e y ", node);
        testPostorder("send more + money = d e + y >= ; ", node);
        testInorder("send + more = money ; d + e >= y ", node);
    }

    @Test
    public void testParserAND5() throws CryptaParserException {
        final ICryptaNode node = parser.parse("A = B;; A = B");
        testPreorder("; = A B = A B ", node);
        testPostorder("A B = A B = ; ", node);
        testInorder("A = B ; A = B ", node);
    }

    @Test
    public void testParserAND6() throws CryptaParserException {
        final ICryptaNode node = parser.parse("A = B;; A = B;;;;;");
        testPreorder("; = A B = A B ", node);
        testPostorder("A B = A B = ; ", node);
        testInorder("A = B ; A = B ", node);
    }

    @Test
    public void testParserAND7() throws CryptaParserException {
        final ICryptaNode node = parser.parse("a=b;");
        testPreorder("= a b ", node);
        testPostorder("a b = ", node);
        testInorder("a = b ", node);
    }

    @Test(expected = CryptaParserException.class)
    public void testParserErrorAND1() throws CryptaParserException {
        parser.parse("a=b b=c");
    }

    @Test(expected = CryptaParserException.class)
    public void testParserErrorAND2() throws CryptaParserException {
        parser.parse("; a=b");
    }

    @Test(expected = CryptaParserException.class)
    public void testParserErrorAND3() throws CryptaParserException {
        parser.parse("(A = B; A = B)");
    }

    @Test(expected = CryptaParserException.class)
    public void testParserErrorAND4() throws CryptaParserException {
        parser.parse("(A !=; A = B)");
    }

    @Test(expected = CryptaParserException.class)
    public void testParserErrorAND5() throws CryptaParserException {
        parser.parse("A !=; (A = B)");
    }

    @Test(expected = CryptaParserException.class)
    public void testParserErrorAND6() throws CryptaParserException {
        parser.parse(";");
    }

    @Test(expected = CryptaParserException.class)
    public void testParserErrorAND7() throws CryptaParserException {
        parser.parse(";a=b; b=c;");
    }

    // Test on parse integers
    @Test
    public void testParserInteger1() throws CryptaParserException {
        final ICryptaNode node = parser.parse("send+more='1234';; d+e>=y");

        testPreorder("; = + send more '1234' >= + d e y ", node);
        testPostorder("send more + '1234' = d e + y >= ; ", node);
        testInorder("send + more = '1234' ; d + e >= y ", node);
    }

    @Test(expected = CryptaParserException.class)
    public void testParserIntegerError1() throws CryptaParserException {
        parser.parse("send + more >= money; 1 + 'aabc' = 3");
    }

    @Test(expected = CryptaParserException.class)
    public void testParserIntegerError2() throws CryptaParserException {
        parser.parse("send + more >= money; 1 + '12a45' = 3");
    }

}
