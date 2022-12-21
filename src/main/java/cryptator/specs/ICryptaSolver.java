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

/**
 * The Interface ICryptaSolver provides the service for solving a cryptarithm.
 */
public interface ICryptaSolver {

    /**
     * Set the time limit of the solver.
     *
     * @param limit the time limit
     */
    void limitTime(long limit);

    /**
     * Set the solution limit of the solver.
     *
     * @param limit the solution limit
     */
    void limitSolution(long limit);

    /**
     * Solve the cryptarithm and feed the consumer with each solution found.
     *
     * @param cryptarithm      the cryptarithm
     * @param config           the configuration
     * @param solutionConsumer the solution consumer
     * @return true, if successful
     * @throws CryptaModelException  if there is a modeling exception
     * @throws CryptaSolverException if there is a solving exception.
     */
    boolean solve(ICryptaNode cryptarithm, CryptaConfig config, Consumer<ICryptaSolution> solutionConsumer)
            throws CryptaModelException, CryptaSolverException;

    /**
     * Solve the cryptarithm and feed the consumer with each pair (cryptarithm,
     * solution) found.
     *
     * @param cryptarithm      the cryptarithm
     * @param config           the configuration
     * @param solutionConsumer the solution consumer
     * @return true, if successful
     * @throws CryptaModelException  if there is a modeling exception
     * @throws CryptaSolverException if there is a solving exception.
     */
    default boolean solve(ICryptaNode cryptarithm, CryptaConfig config,
            final BiConsumer<ICryptaNode, ICryptaSolution> consumer)
            throws CryptaModelException, CryptaSolverException {
        return solve(cryptarithm, config, solution -> consumer.accept(cryptarithm, solution));
    }

}
