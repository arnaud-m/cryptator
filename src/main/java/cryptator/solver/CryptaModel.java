/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import java.util.Map;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class CryptaModel {

	public final Model model; 
	
	public final Map<Character, IntVar> symbolsToVariables;

	public CryptaModel(Model model, Map<Character, IntVar> symbolsToVariables) {
		super();
		this.model = model;
		this.symbolsToVariables = symbolsToVariables;
	}

	public final Model getModel() {
		return model;
	}

	public final Map<Character, IntVar> getMap() {
		return symbolsToVariables;
	}
	
	
	
	
	
}
