/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.specs;

import cryptator.game.CryptaGameDecision;
import cryptator.game.CryptaGameException;
import cryptator.solver.CryptaModel;

/**
 * The Interface ICryptaGameEngine defines a simple pedagogical game engine for solving a cryptarithm.
 */
public interface ICryptaGameEngine {


	/**
	 * Set up the constraint programming model for solving the cryptarithm.
	 *
	 * @param model the model that represents the cryptarithm to solve.
	 * @throws CryptaGameException if the game cannot be initialized. For instance, the cryptarithm has no solution.
	 */
	void setUp(CryptaModel model) throws CryptaGameException;

	/**
	 * Take a decision for solving the cryptarithm.
	 *
	 * @param decision the decision taken y the player
	 * @return true, if the decision was applied, and false if its refutation was applied.
	 * @throws CryptaGameException if the decision is invalid.
	 */
	boolean takeDecision(CryptaGameDecision decision) throws CryptaGameException;

	/**
	 * Checks if the cryptarithm is solved.
	 *
	 * @return true, if the cryptarithm is solved, and false if some letters remain unknown.
	 */
	boolean isSolved();

	/**
	 * Tear down the game.
	 *
	 * @throws CryptaGameException if the game cannot be teared down.
	 */
	void tearDown() throws CryptaGameException;

}
