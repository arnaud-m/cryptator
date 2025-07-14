/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.specs;

/**
 * The Interface ICryptaGenSolver can record a cryptarithm found by the solver.
 */
public interface ICryptaGenSolver extends IChocoModel {

    /**
     * Record a cryptarithm found by the solver.
     *
     * It is required that a solution is currently available.
     *
     * @return the cryptarithm node or null if a problem has been detected.
     */
    ICryptaNode recordCryptarithm();
}
