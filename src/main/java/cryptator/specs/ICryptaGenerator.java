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

import cryptator.solver.CryptaModelException;

/**
 * The Interface ICryptaGenerator generates new cryptarithms.
 */
public interface ICryptaGenerator {

	/**
	 * Generate cryptarithms and feed them to the consumer.
	 * Usually, there must be a unique solution to the cryptarithm.
	 *
	 * @param consumer the consumer that handles the generated cryptarithm along with its solution.
	 * @throws CryptaModelException if there was an error during the generation.
	 */
	void generate(BiConsumer<ICryptaNode, ICryptaSolution> consumer) throws CryptaModelException;

}
