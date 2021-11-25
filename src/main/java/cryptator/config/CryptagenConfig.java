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

	@Option(name="-ctry",usage="country code for doubly true cryptarithms)")
	private String countryCode = "EN";
	
	@Option(name="-lang",usage="language code for doubly true cryptarithms)")
	private String langCode = "en";
	
	@Option(name="-t", usage="number of threads (experimental)")
	private int nthreads = 1;
	
	@Option(name="-minop", usage="minimum number of left operands")
	private int minLeftOperands= 2;
	
	@Option(name="-maxop", usage="maximum number of left operands")
	private int maxLeftOperands= -1;
	
	
	public final boolean isDryRun() {
		return dryRun;
	}

	public final String getCountryCode() {
		return countryCode;
	}

	public final String getLangCode() {
		return langCode;
	}

	public final int getNthreads() {
		return nthreads;
	}

	public final int getMinLeftOperands() {
		return minLeftOperands;
	}

	public final int getMaxLeftOperands() {
		return maxLeftOperands;
	}

	@Override
	public String toString() {
		return super.toString() + "\nc LANG " + langCode + "\nc THREADS " + nthreads;
	}
	
	
	
}
