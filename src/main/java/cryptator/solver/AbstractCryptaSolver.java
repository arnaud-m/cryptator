/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import org.slf4j.LoggerFactory;

import cryptator.config.CryptaConfig;
import cryptator.gen.TransformWord;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolver;
import cryptator.tree.TreeUtils;

public abstract class AbstractCryptaSolver implements ICryptaSolver {

    protected static final int MS = 1000;

    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(AbstractCryptaSolver.class);
    
    protected long timeLimit = 0;

    protected long solutionLimit = 0;

    protected AbstractCryptaSolver() {     
    }
   
    protected final void logOnCryptarithm(final ICryptaNode cryptarithm) {
        	logger.atInfo().setMessage("Declare instance:\ni {}").addArgument(() -> TransformWord.removeWhitespaces(TreeUtils.writeInorder(cryptarithm))).log();
            logger.atDebug().setMessage("Cryptarithm features:\n{}").addArgument(() -> TreeUtils.computeFeatures(cryptarithm)).log();
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

    protected final void logOnConfiguration(final CryptaConfig config) {
        logger.debug("Configuration:\n{}", config);
    }

}
