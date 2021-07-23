/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.util.ArrayList;

import cryptator.solver.Variable;
import cryptator.specs.ICryptaNode;
import cryptator.tree.TreeUtils;

import static cryptator.tree.TreeUtils.*;

public class Cryptator {

	public Cryptator() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		CryptaParserWrapper parser = new CryptaParserWrapper();
    	ICryptaNode node;

		node = parser.parse("send+more=money");
		ArrayList<Variable> map=TreeUtils.mapPostorder(node);
		ArrayList<Integer> tab =makeArray(map.size());
		System.out.println(arrayVarToString(findSolCrypta(tab, map.size(), 10, map, node)));


	}

}
