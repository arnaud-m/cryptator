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

public class CryptatorConfig extends CryptaCmdConfig {
	
	@Option(name="-s", usage="limit the number of solutions returned by the solver")
	private int solutionLimit;
	
	@Option(name="-t", usage="limit the time taken by a solver (in seconds)")
	private int timeLimit;
	
	@Option(name="-l",handler=ExplicitBooleanOptionHandler.class,usage="use the bignum model (only + and =)")
	private boolean useBigNum;

	
	public final int getSolutionLimit() {
		return solutionLimit;
	}

	public final int getTimeLimit() {
		return timeLimit;
	}
	
	public final boolean useBignum() {
		return useBigNum;
	}
	
	public final void setSolutionLimit(int solutionLimit) {
		this.solutionLimit = solutionLimit;
	}

	public final void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	public final void setUseBigNum(boolean useBigNum) {
		this.useBigNum = useBigNum;
	}

	@Override
	public String toString() {
		return super.toString() + 
				"\nc TIME_LIMIT "+getTimeLimit() + 
				"\nc SOLUTION_LIMIT "+getSolutionLimit() + 
				"\nc BIGNUM "+useBignum(); 
	}
}
