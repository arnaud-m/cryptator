/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.specs;

import java.util.Optional;

public interface ICryptaSolutionStore {

    int getSolutionCount();

    Optional<ICryptaSolution> getLastSolution();

    default Optional<ICryptaSolution> getUniqueSolution() {
        return getSolutionCount() <= 1 ? getLastSolution() : Optional.empty();
    }

}
