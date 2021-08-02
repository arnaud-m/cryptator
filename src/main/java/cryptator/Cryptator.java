/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import cryptator.parser.CryptaParserWrapper;
import cryptator.solver.CryptaSolver;
import cryptator.specs.ICryptaNode;
import cryptator.tree.GraphizExporter;

public class Cryptator {

	public Cryptator() {}

	public static void main(String[] args) throws Exception {
		final CryptaParserWrapper parser = new CryptaParserWrapper();
    	final ICryptaNode node = parser.parse("donald+gerald=robert");
		final CryptaConfig config = new CryptaConfig();
		final GraphizExporter export = new GraphizExporter();
		config.setSolutionLimit(2);
		final CryptaSolver solver = new CryptaSolver();
		solver.solve(node, config, (s) -> {
			System.out.println(s);
			export.print(node, s, System.out);
		} );		
	}

}
