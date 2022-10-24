/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.specs;

import java.math.BigInteger;

import cryptator.tree.CryptaEvaluationException;

/**
 * The Interface ICryptaEvaluation evaluates a cryptarithm for a given solution and base.
 */
public interface ICryptaEvaluation {

	/**
	 * Evaluate the value of a cryptarithm for a given solution and base.
	 * A cryptarithm that contains relational operator returns a logical value, and a numeric value otherwise.
	 * A logical value is false if equal to 0 and true otherwise. 
	 * 
	 * @param cryptarithm the cryptarithm
	 * @param solution the solution of the cryptarithm
	 * @param base the base of the cryptarithm
	 * @return the evaluation of cryptarithm (numeric or logical)
	 * @throws CryptaEvaluationException if the solution is partial (some symbols miss digits).
	 */
	BigInteger evaluate(ICryptaNode cryptarithm, ICryptaSolution solution, int base) throws CryptaEvaluationException;

}
