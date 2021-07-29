/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.specs;

public interface ICryptaEvaluation {

	// TODO Throws an exception on failure ?
	int evaluate(ICryptaNode cryptarithm, ICryptaSolution solution, int base);

}
