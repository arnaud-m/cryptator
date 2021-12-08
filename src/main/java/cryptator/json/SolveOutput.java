/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.json;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import cryptator.solver.CryptaSolutionException;
import cryptator.specs.ICryptaEvaluation;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.tree.CryptaEvaluation;
import cryptator.tree.CryptaEvaluationException;

public final class SolveOutput implements BiConsumer<ICryptaNode, ICryptaSolution> {

	private String cryptarithm;

	private int base;

	private char[] symbols;
	
	private List<int[]> solutions = new ArrayList<>();

	private int invalidSolution;

	public SolveOutput() {
		super();
	}

	public SolveOutput(SolveInput input) {
		this(input.getCryptarithm(), input.getConfig().getArithmeticBase());
	}
	
	public SolveOutput(String cryptarithm, int base) {
		super();
		this.cryptarithm = cryptarithm;
		this.base = base;
	}
	
	
	public String getCryptarithm() {
		return cryptarithm;
	}

	public void setCryptarithm(String cryptarithm) {
		this.cryptarithm = cryptarithm;
	}

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}
	
	public char[] getSymbols() {
		return symbols;
	}

	public void setSymbols(char[] symbols) {
		this.symbols = symbols;
	}

	public List<int[]> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<int[]> solutions) {
		this.solutions = solutions;
	}

	public int getInvalidSolution() {
		return invalidSolution;
	}

	public void setInvalidSolution(int invalidSolution) {
		this.invalidSolution = invalidSolution;
	}

	@Override
	public void accept(ICryptaNode n, ICryptaSolution s) {
		final ICryptaEvaluation eval = new CryptaEvaluation();
		try {
			if (eval.evaluate(n, s, base).compareTo(BigInteger.ZERO) == 0) invalidSolution++;
			else {
				int[] solution = new int[symbols.length];
				for (int i = 0; i < solution.length; i++) {
					solution[i] = s.getDigit(symbols[i]);
				}
				solutions.add(solution);
			}
		} catch (CryptaEvaluationException|CryptaSolutionException e) {
			invalidSolution++;
		}
	}

}