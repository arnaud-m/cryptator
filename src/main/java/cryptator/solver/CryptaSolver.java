/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.chocosolver.solver.Solver;

import cryptator.CryptaConfig;
import cryptator.specs.ICryptaModeler;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ICryptaSolver;

public class CryptaSolver implements ICryptaSolver {
	
	public static final Logger LOGGER = Logger.getLogger(CryptaSolver.class.getName());

	private final ICryptaModeler modeler = new CryptaModeler();

	private long timeLimit = 0;

	private long solutionLimit = 0;

	public CryptaSolver() {}


	public final long getTimeLimit() {
		return timeLimit;
	}

	@Override
	public final void limitTime(long limit) {
		this.timeLimit = limit;
	}

	public final long getSolutionLimit() {
		return solutionLimit;
	}


	public final void limitSolution(long limit) {
		this.solutionLimit = limit;
	}


	@Override
	public boolean solve(ICryptaNode cryptarithm, CryptaConfig config, Consumer<ICryptaSolution> solutionConsumer) throws CryptaModelException {
		final CryptaModel m = modeler.model(cryptarithm, config);
		LOGGER.log(Level.CONFIG, "Display model{0}", m.getModel().toString());
		final Solver s = m.getModel().getSolver();
		if(timeLimit > 0) s.limitTime(timeLimit);
		int solutionCount = 0;
		if(solutionLimit > 0) {
			while(solutionCount < solutionLimit && s.solve()) {
				solutionConsumer.accept(m.recordSolution());
				solutionCount++;
			}
		} else {
			while(s.solve()) {
				solutionConsumer.accept(m.recordSolution());
				solutionCount++;
			}
		}
		LOGGER.log(Level.INFO, "{0}", s.getMeasures());
		return solutionCount > 0;
	}

}
