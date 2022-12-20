/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.specs;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import cryptator.config.CryptaConfig;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolverException;

public interface ICryptaSolver {

    void limitTime(long limit);

    void limitSolution(long limit);

    boolean solve(ICryptaNode cryptarithm, CryptaConfig config, Consumer<ICryptaSolution> solutionConsumer)
            throws CryptaModelException, CryptaSolverException;

    default boolean solve(ICryptaNode cryptarithm, CryptaConfig config,
            final BiConsumer<ICryptaNode, ICryptaSolution> consumer)
            throws CryptaModelException, CryptaSolverException {
        return solve(cryptarithm, config, solution -> consumer.accept(cryptarithm, solution));
    }

}
