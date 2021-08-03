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

import org.chocosolver.solver.Solver;

import cryptator.CryptaConfig;
import cryptator.specs.ICryptaModeler;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ICryptaSolver;

public class CryptaSolver implements ICryptaSolver {

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
	public boolean solve(ICryptaNode cryptarithm, CryptaConfig config, Consumer<ICryptaSolution> solutionConsumer) {
		try {
			final CryptaModel m = modeler.model(cryptarithm, config);
			final Solver solver = m.getModel().getSolver();
			if(timeLimit > 0) solver.limitTime(timeLimit);
			if(solutionLimit > 0) {
				long n = solutionLimit; 
				while(n > 0 && solver.solve()) {
					solutionConsumer.accept(m.recordSolution());
					n--;
				}
			} else {
				while(solver.solve()) {
					solutionConsumer.accept(m.recordSolution());
				}
			}
			solver.printStatistics();
			solver.showStatistics();
		} catch (CryptaModelException e) {
			e.printStackTrace();
		}
		return false;
	}

}
