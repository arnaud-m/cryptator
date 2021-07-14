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

import cryptator.solver.CryptaSolution;
import cryptator.specs.ICryptaNode;
import cryptator.tree.CryptaEvaluation;
import cryptator.tree.TreeUtils;

public class Cryptator {

	public Cryptator() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		CryptaParserWrapper parser = new CryptaParserWrapper();
    	ICryptaNode node;
		// KO
		
		node = parser.parse("p1+di*n2=z=z");
		TreeUtils.printPostorder(node);
		
    	node = parser.parse("p1+di*n2=z=u");
    	TreeUtils.printPostorder(node);
		
    	node = parser.parse("p1+di*+n2=z");
    	TreeUtils.printPostorder(node);
		
    	node = parser.parse("p1 di*+n2=z");
    	TreeUtils.printPostorder(node);
		
    	try {
			node = parser.parse("p1+di*++n2=z");
			TreeUtils.printPostorder(node);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	// TODO What is the largest integer with choco ?
    	node = parser.parse("pppppppppppppppppppp + aaaaaaaaaaaaaaaaaaaaaaa = zzzzzzzzzzzzzzzzzzzzzzza");
		TreeUtils.printPostorder(node);
		
		// TODO What about negative numbers ?
		node = parser.parse("- a - ( - c) = d");
		TreeUtils.printPostorder(node);
		
		// OK
		
		node = parser.parse("p+di*n=z");
		TreeUtils.printPostorder(node);
		
		node = parser.parse("p+  di*n =  z");
		TreeUtils.printPostorder(node);
		
		HashMap<Character, Integer> map = new HashMap<Character, Integer>();
		map.put('p', 0);
		map.put('d', 1);
		map.put('i', 2);
		map.put('n', 3);
		map.put('z', 4);
		CryptaSolution sol = new CryptaSolution(map);
		
		CryptaEvaluation eval = new CryptaEvaluation();
		
		System.out.println(eval.evaluate(node, sol, 10));
		
		map.remove('p');
		// FIXME missing value not detected 
		System.out.println(eval.evaluate(node, sol, 10));
	}

}
