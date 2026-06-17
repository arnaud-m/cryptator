/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.specs;

import org.chocosolver.solver.Solver;

/**
 * Immutable container for search performance measures.
 *
 * @param solutionCount number of solutions found
 * @param timeCount execution time of the search in seconds
 * @param nodeCount number of explored nodes
 * @param errorCount number of errors
 */
public record SearchMeasures(long solutionCount, double timeCount, long nodeCount, int errorCount) {

	  public SearchMeasures(Solver solver) {
		  this(solver.getSolutionCount(), solver.getTimeCount(), solver.getSolutionCount(), 0);
	  }
	  
	  public final SearchMeasures withErrorCount(int errorCount) {
		  return new SearchMeasures(this.solutionCount, this.timeCount, this.nodeCount, errorCount);
	  }
	  
    /**
     * Indicates whether at least one solution was found.
     *
     * @return {@code true} if at least one solution exists
     */
    public final boolean isFeasible() {
        return solutionCount > 0;
    }
}