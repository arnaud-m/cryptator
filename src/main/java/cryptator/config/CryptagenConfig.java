/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.config;

import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.ExplicitBooleanOptionHandler;

public class CryptagenConfig extends CryptaCmdConfig {
	
	@Option(name="-d",handler=ExplicitBooleanOptionHandler.class,usage="dry run (generate but do not solve candidate cryptarithms)")
	private boolean dryRun;

	public final boolean isDryRun() {
		return dryRun;
	}

	
}
