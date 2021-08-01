/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;

import cryptator.parser.CryptaParserWrapper;
import cryptator.solver.CryptaModel;
import cryptator.solver.CryptaModeler;
import cryptator.specs.ICryptaNode;
import cryptator.tree.TreeUtils;

public class Cryptator {

	public Cryptator() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws Exception {
		//Model model=new Model("Cryptarithme");


		CryptaParserWrapper parser = new CryptaParserWrapper();
		ICryptaNode node;

		node = parser.parse("send+more=money");
		
		TreeUtils.printInorder(node);
		TreeUtils.printPostorder(node);
//		ArrayList<Variable> map=TreeUtils.mapPostorder(node);
//		ArrayList<Integer> tab =makeArray(map.size());
//		System.out.println(arrayVarToString(findSolCrypta(tab, map.size(), 10, map, node, 1)));

		CryptaModel model = new CryptaModeler().model(node, new CryptaConfig());
		System.out.println(model.getModel());

//		contraint(node, model);
//
		Solver solver = model.getModel().getSolver();
		Solution solution=new Solution(model.getModel());
		if(solver.solve()){
			System.out.println(solution.record());
		}
		solver.showStatistics();
		solver.showSolutions();
//		solver.findSolution();



	}

}
