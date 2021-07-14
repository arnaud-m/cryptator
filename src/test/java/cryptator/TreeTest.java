/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import org.junit.Before;
import org.junit.Test;

import cryptator.specs.ICryptaTree;
import cryptator.tree.CryptaLeaf;
import cryptator.tree.CryptaNode;
import cryptator.tree.TreeUtils;

public class TreeTest {

	public ICryptaTree sendMoreMoney;
	
	public ICryptaTree donaldGeraldRobert;
	
	public ICryptaTree bigCatLion;
	
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
		TreeUtils.toDotty(sendMoreMoney, System.out);
		
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
	}
}
