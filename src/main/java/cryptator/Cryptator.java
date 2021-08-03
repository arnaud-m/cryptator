/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.util.Arrays;
import java.util.logging.Logger;

import cryptator.parser.CryptaParserWrapper;
import cryptator.solver.CryptaModel2;
import cryptator.solver.CryptaSolver;
import cryptator.specs.ICryptaNode;
import cryptator.tree.GraphizExporter;
import org.chocosolver.solver.Solver;

import static cryptator.solver.SolverUtils.contraint;

public class Cryptator {
	
	private static final Logger LOGGER = Logger.getLogger(Cryptator.class.getName());


	public Cryptator() {}

	public static void main(String[] args) throws Exception {
		System.out.println(Arrays.toString(args));
		final CryptaParserWrapper parser = new CryptaParserWrapper();
    	final ICryptaNode node = parser.parse("donald+gerald=robert");
		final CryptaConfig config = new CryptaConfig();
		final GraphizExporter export = new GraphizExporter();
		final CryptaSolver solver = new CryptaSolver();
		solver.limitSolution(2);
		solver.solve(node, config, (s) -> {
			System.out.println(s);
			export.print(node, s, System.out);
		} );		
	}

}
