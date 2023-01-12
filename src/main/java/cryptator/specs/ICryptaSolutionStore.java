/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.specs;

import java.util.Optional;

/**
 * The Interface ICryptaSolutionStore provides access to the last solution.
 */
public interface ICryptaSolutionStore {

    /**
     * Total number of solutions found.
     *
     * @return the number of solution
     */
    int getSolutionCount();

    /**
     * Getter for the last solution found.
     *
     * @return the last solution found if any.
     */
    Optional<ICryptaSolution> getLastSolution();

    /**
     * Getter for the unique solution.
     *
     * @return the unique solution.
     */
    default Optional<ICryptaSolution> getUniqueSolution() {
        return getSolutionCount() <= 1 ? getLastSolution() : Optional.empty();
    }

}
