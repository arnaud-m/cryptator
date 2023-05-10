/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.config;

import org.kohsuke.args4j.Option;

public class CryptatorConfig extends CryptaCmdConfig {

    @Option(name = "--solution", usage = "Limit the number of solutions returned by the solver.")
    private int solutionLimit;

    @Option(name = "--time", usage = "Limit the time taken by the solver (in seconds).")
    private int timeLimit;

    public final int getSolutionLimit() {
        return solutionLimit;
    }

    public final int getTimeLimit() {
        return timeLimit;
    }

    public final void setSolutionLimit(final int solutionLimit) {
        this.solutionLimit = solutionLimit;
    }

    public final void setTimeLimit(final int timeLimit) {
        this.timeLimit = timeLimit;
    }

    @Override
    public String toString() {
        return super.toString() + "\nc TIME_LIMIT " + getTimeLimit() + "\nc SOLUTION_LIMIT " + getSolutionLimit();
    }
}
