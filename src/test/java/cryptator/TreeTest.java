/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import cryptator.solver.CryptaSolution;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaEvaluation;
import cryptator.tree.CryptaEvaluation;
import cryptator.tree.CryptaLeaf;
import cryptator.tree.CryptaNode;
import cryptator.tree.GraphizExporter;
import cryptator.tree.TreeUtils;

import static cryptator.tree.TreeUtils.*;
import static org.junit.Assert.*;

public class TreeTest {

	public ICryptaNode sendMoreMoney;
	
	public ICryptaNode donaldGeraldRobert;
	
	public ICryptaNode bigCatLion;

	public ICryptaNode ABC;
	
	@Before
	public void initializeTrees() {
		sendMoreMoney = new CryptaNode(CryptaOperator.EQ, 
				new CryptaNode(CryptaOperator.ADD, new CryptaLeaf("send"), new CryptaLeaf("more")), 
				new CryptaLeaf("money"));
		
		donaldGeraldRobert = new CryptaNode(CryptaOperator.EQ, 
				new CryptaNode(CryptaOperator.ADD, new CryptaLeaf("donald"), new CryptaLeaf("gerald")), 
				new CryptaLeaf("robert"));
		
		bigCatLion= new CryptaNode(CryptaOperator.EQ, 
				new CryptaNode(CryptaOperator.ADD, new CryptaLeaf("big"), new CryptaLeaf("cat")), 
				new CryptaLeaf("lion"));

		//AB + BA = CBC
		ABC= new CryptaNode(CryptaOperator.EQ,
				new CryptaNode(CryptaOperator.ADD, new CryptaLeaf("AB"), new CryptaLeaf("BA")),
				new CryptaLeaf("CBC"));

	}
	
	public TreeTest() {}
	
	@Test
	public void testTree() {
		(new GraphizExporter()).print(sendMoreMoney, System.out);

		//preorder
		assertEquals(TreeUtils.writePreorder(sendMoreMoney, System.out), "= + send more money ");
		System.out.println();
		assertEquals(TreeUtils.writePreorder(donaldGeraldRobert, System.out), "= + donald gerald robert ");
		System.out.println();
		assertEquals(TreeUtils.writePreorder(bigCatLion, System.out), "= + big cat lion ");
		System.out.println();

		//postorder
		assertEquals(TreeUtils.writePostorder(sendMoreMoney, System.out), "send more + money = ");
		System.out.println();
		assertEquals(TreeUtils.writePostorder(donaldGeraldRobert, System.out), "donald gerald + robert = ");
		System.out.println();
		assertEquals(TreeUtils.writePostorder(bigCatLion, System.out), "big cat + lion = ");
		System.out.println();

		//inorder
		assertEquals(TreeUtils.writeInorder(sendMoreMoney, System.out), "send + more = money ");
		System.out.println();
		assertEquals(TreeUtils.writeInorder(donaldGeraldRobert, System.out), "donald + gerald = robert ");
		System.out.println();
		assertEquals(TreeUtils.writeInorder(bigCatLion, System.out), "big + cat = lion ");
		System.out.println();

		//evaluate
		HashMap<Character, Integer> abc = new HashMap<Character, Integer>();
		abc.put('A', 0);
		abc.put('B', 0);
		abc.put('C', 0);


		HashMap<Character, Integer> abcSol = new HashMap<Character, Integer>();
		// La solution est A=9 B=2 et C=1.
		abcSol.put('A', 9);
		abcSol.put('B', 2);
		abcSol.put('C', 1);

		//algo d'exploration methode 1
		int[] tab={0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		assertEquals(explorationRecursive(10, tab, null, abc, ABC), abcSol);

		//algo d'exploration methode 2
		int[] tab2={0,0,0};
		assertEquals(explorationRecursive2(tab2, abc, ABC), abcSol);

		HashMap<Character, Integer> sendMory = new HashMap<Character, Integer>();
		// La solution est O = 0, M = 1, Y = 2, E = 5, N = 6, D = 7, R = 8 et S = 9.
		sendMory.put('o', 0);
		sendMory.put('m', 1);
		sendMory.put('y', 2);
		sendMory.put('e', 5);
		sendMory.put('n', 6);
		sendMory.put('d', 7);
		sendMory.put('r', 8);
		sendMory.put('s', 9);


		ICryptaEvaluation chk = new CryptaEvaluation();
		int v = chk.evaluate(sendMoreMoney, new CryptaSolution(sendMory), 10);
		assertEquals(v,1);
		System.out.println(v);
		
		sendMory.replace('y', 0);
		v = chk.evaluate(sendMoreMoney, new CryptaSolution(sendMory), 10);
		assertEquals(v,0);
		System.out.println(v);
	}
	
}
