/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.specs;

import cryptator.config.CryptaConfig;
import cryptator.solver.CryptaModel;
import cryptator.solver.CryptaModelException;

/**
 * The Interface ICryptaModeler builds a constraint programming model to solve a cryptarithm using the given configuration.
 */
public interface ICryptaModeler {

	/**
	 * Build a constraint programming model in choco to solve a cryptarithm.
	 *
	 * @param cryptarithm the cryptarithm
	 * @param config the config
	 * @return the choco model
	 * @throws CryptaModelException if the model cannot be built.
	 */
	CryptaModel model(ICryptaNode cryptarithm, CryptaConfig config) throws CryptaModelException;

}
