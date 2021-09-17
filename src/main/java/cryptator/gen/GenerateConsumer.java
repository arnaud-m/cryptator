/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import cryptator.CryptaConfig;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolverException;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ICryptaSolver;

public class GenerateConsumer implements Consumer<ICryptaNode> {


	private final ICryptaSolver solver;

	private final CryptaConfig config;

	private final BiConsumer<ICryptaNode, ICryptaSolution> internal;

	private Logger logger; 

	public GenerateConsumer(ICryptaSolver solver, CryptaConfig config,
			BiConsumer<ICryptaNode, ICryptaSolution> internal) {
		super();
		this.solver = solver;
		this.config = config;
		this.internal = internal;
		this.solver.limitSolution(2);

	}

	private static class SolutionCollect implements Consumer<ICryptaSolution> {

		private int solutionCount;

		private ICryptaSolution solution;

		@Override
		public void accept(ICryptaSolution u) {
			solutionCount++;
			this.solution = u;
		}	

		public final boolean hasUniqueSolution() {
			return solutionCount == 1;
		}

		public final ICryptaSolution getSolution() {
			return solution;
		}

	}

	@Override
	public void accept(ICryptaNode t) {
		try {
			final SolutionCollect collect = new SolutionCollect();
			solver.solve(t, config, collect);
			if(collect.hasUniqueSolution()) {
				internal.accept(t, collect.getSolution());
			}
		} catch (CryptaModelException|CryptaSolverException e) {
			// TODO Must count errors ?
			// FIXME logger is null !
			logger.log(Level.WARNING, "failed to solve the cryptarithm", e);
		}
	}
}