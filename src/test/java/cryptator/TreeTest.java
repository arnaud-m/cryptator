/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import org.junit.Test;

import cryptator.tree.CryptaLeaf;
import cryptator.tree.CryptaNode;
import cryptator.tree.TreeUtils;

public class TreeTest {

	public TreeTest() {}
	
	@Test
	public void testTree() {
		CryptaNode l = new CryptaNode(CryptaOperator.ADD, new CryptaLeaf("send"), new CryptaLeaf("more"));
		CryptaNode t = new CryptaNode(CryptaOperator.EQ, l, new CryptaLeaf("money"));
		TreeUtils.toDotty(t, System.out);
		TreeUtils.writePreorder(t, System.out);
	}
}
