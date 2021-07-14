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
import cryptator.specs.ICryptaEvaluator;
import cryptator.tree.CryptaLeaf;
import cryptator.tree.CryptaNode;
import cryptator.tree.GraphizExporter;
import cryptator.tree.TreeUtils;

public class TreeTest {

	public ICryptaNode sendMoreMoney;
	
	public ICryptaNode donaldGeraldRobert;
	
	public ICryptaNode bigCatLion;
	
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
		
	}
	
	public TreeTest() {}
	
	@Test
	public void testTree() {
		(new GraphizExporter()).print(sendMoreMoney, System.out);
		
		TreeUtils.writePreorder(sendMoreMoney, System.out);
		System.out.println();
		TreeUtils.writePreorder(donaldGeraldRobert, System.out);
		System.out.println();
		TreeUtils.writePreorder(bigCatLion, System.out);
		System.out.println();
		
		TreeUtils.writePostorder(sendMoreMoney, System.out);
		System.out.println();
		TreeUtils.writePostorder(donaldGeraldRobert, System.out);
		System.out.println();
		TreeUtils.writePostorder(bigCatLion, System.out);
		System.out.println();
		
		HashMap<Character, Integer> sendMory = new HashMap<Character, Integer>();
		// La solution est O = 0, M = 1, Y = 2, E = 5, N = 6, D = 7, R = 8 et S = 9. 
		sendMory.put('s', 9);
		sendMory.put('e', 5);
		sendMory.put('n', 6);
		sendMory.put('d', 7);
		sendMory.put('m', 1);
		sendMory.put('o', 0);
		sendMory.put('r', 8);
		sendMory.put('y', 2);
		
		ICryptaEvaluator chk = TreeUtils.makeCryptarithmChecker();
		int v = chk.evaluate(sendMoreMoney, new CryptaSolution(sendMory), 10);
		System.out.println(v);
		
		sendMory.replace('y', 0);	
		v = chk.evaluate(sendMoreMoney, new CryptaSolution(sendMory), 10);
		System.out.println(v);
	}
	
}
