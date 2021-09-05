/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

import cryptator.CryptaConfig;
import cryptator.specs.ITraversalNodeConsumer;

public abstract class AbstractModelerNodeConsumer implements ITraversalNodeConsumer {

	public final Model model;
	public final CryptaConfig config;
	public final Map<Character, IntVar> symbolsToVariables;
	protected final Consumer<char[]> firstSymbolConstraint;

	public AbstractModelerNodeConsumer(Model model, CryptaConfig config) {
		super();
		this.model = model;
		this.config = config;
		symbolsToVariables = new HashMap<Character, IntVar>();
		firstSymbolConstraint = config.allowLeadingZeros() ? 
				w -> {} : w -> {if(w.length > 0) getSymbolVar(w[0]).gt(0).post();};	
	}

	private IntVar createSymbolVar(char symbol) {
		return model.intVar(String.valueOf(symbol), 0, config.getArithmeticBase()-1, false);
	}

	protected IntVar getSymbolVar(char symbol) {
		if(! symbolsToVariables.containsKey(symbol)) {
			symbolsToVariables.put(symbol, createSymbolVar(symbol));
		} 
		return symbolsToVariables.get(symbol);
	}

	private IntVar[] getGCCVars() {
		final Collection<IntVar> vars = symbolsToVariables.values();
		return vars.toArray(new IntVar[vars.size()]);
	}

	private int[] getGCCValues() {
		int[] values = new int[config.getArithmeticBase()];
		for (int i = 0; i < values.length; i++) {
			values[i] = i;
		}
		return values;
	}

	private IntVar[] getGCCOccs(int lb, int ub) {
		return model.intVarArray("O", config.getArithmeticBase(), lb, ub, false);
	}

	public Constraint globalCardinalityConstraint() {
		final IntVar[] vars = getGCCVars();
		final int n = vars.length;
		if(n == 0) return model.trueConstraint();
	
		final int maxOcc = config.getMaxDigitOccurence(n);		
		if(maxOcc == 1) {
			return model.allDifferent(vars);
		} else {
			final int minOcc = config.getMinDigitOccurence(n);
			return model.globalCardinality(
					vars, 
					getGCCValues(), 
					getGCCOccs(minOcc, maxOcc), 
					true);
	
		}
	}
	
	public abstract Constraint cryptarithmEquationConstraint() throws CryptaModelException;

}