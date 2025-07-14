/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import java.util.logging.Level;
import java.util.logging.Logger;

import cryptator.config.CryptaConfig;
import cryptator.gen.TransformWord;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolver;
import cryptator.tree.TreeUtils;

public abstract class AbstractCryptaSolver implements ICryptaSolver {

    protected static final int MS = 1000;

    public static final Logger LOGGER = Logger.getLogger(AbstractCryptaSolver.class.getName());

    protected long timeLimit = 0;

    protected long solutionLimit = 0;

    protected AbstractCryptaSolver() {
        super();
    }

    protected static void logOnCryptarithm(final ICryptaNode cryptarithm) {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "Declare instance:\ni {0}",
                    TransformWord.removeWhitespaces(TreeUtils.writeInorder(cryptarithm)));
            if (LOGGER.isLoggable(Level.CONFIG)) {
                LOGGER.log(Level.CONFIG, "Cryptarithm features:\n{0}", TreeUtils.computeFeatures(cryptarithm));
            }
        }
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    @Override
    public void limitTime(final long limit) {
        this.timeLimit = limit;
    }

    public long getSolutionLimit() {
        return solutionLimit;
    }

    @Override
    public void limitSolution(final long limit) {
        this.solutionLimit = limit;
    }

    protected static void logOnConfiguration(final CryptaConfig config) {
        LOGGER.log(Level.CONFIG, "Configuration:\n{0}", config);
    }

}
