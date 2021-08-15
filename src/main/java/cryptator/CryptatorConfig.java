/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.ExplicitBooleanOptionHandler;

public class CryptatorConfig extends CryptaConfig {
	
	@Option(name="-s", usage="limit the number of solutions returned by the solver")
	private int solutionLimit;

	@Option(name="-t", usage="limit the time taken by a solver")
	private int timeLimit;

	@Option(name="-c",handler=ExplicitBooleanOptionHandler.class,usage="check solutions by evaluation")
	private boolean checkSolution;

	@Option(name="-g",handler=ExplicitBooleanOptionHandler.class,usage="export solutions to graphviz format")
	private boolean exportGraphiz;

	@Option(name="-v",handler=ExplicitBooleanOptionHandler.class,usage="increase the verbosity of the program")
	private boolean verbose;

	public CryptatorConfig() {}

	public final int getSolutionLimit() {
		return solutionLimit;
	}

	public final int getTimeLimit() {
		return timeLimit;
	}

	public final boolean isExportGraphiz() {
		return exportGraphiz;
	}

	public final boolean isCheckSolution() {
		return checkSolution;
	}

	public final boolean isVerbose() {
		return verbose;
	}
	
	@Override
	public String toString() {
		return super.toString() + 
				"\nc TIME_LIMIT "+getTimeLimit() + 
				"\nc SOLUTION_LIMIT "+getSolutionLimit();
	}
}
