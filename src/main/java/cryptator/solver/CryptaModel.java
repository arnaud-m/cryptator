/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import java.util.HashMap;
import java.util.Map;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import cryptator.specs.ICryptaSolution;

public final class CryptaModel {

	public final Model model; 
	
	public final CryptaSolutionVars solution;

	public CryptaModel(Model model, Map<Character, IntVar> symbolsToVariables) {
		super();
		this.model = model;
		this.solution = new CryptaSolutionVars(symbolsToVariables);
	}

	public final Model getModel() {
		return model;
	}

	
	public final CryptaSolutionVars getSolution() {
		return solution;
	}

	public ICryptaSolution recordSolution() {
		return solution.recordSolution();
	}

	@Override
	public String toString() {
		return solution.toString();
	}
	
	
	
	
}
