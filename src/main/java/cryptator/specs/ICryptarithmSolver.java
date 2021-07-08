/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.specs;

import java.util.stream.Stream;

import cryptator.CryptaConfig;

public interface ICryptarithmSolver {

	Stream<ICryptaSolution> solve(String cryptarithm, CryptaConfig config);

}
