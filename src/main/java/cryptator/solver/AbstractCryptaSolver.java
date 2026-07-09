/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import java.util.logging.Logger;

import org.slf4j.LoggerFactory;

import cryptator.JULogUtil;
import cryptator.config.CryptaConfig;
import cryptator.gen.TransformWord;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolver;
import cryptator.tree.TreeUtils;

public abstract class AbstractCryptaSolver implements ICryptaSolver {

    protected static final int MS = 1000;

    @Deprecated
    public static final Logger JUL_LOGGER = Logger.getLogger(AbstractCryptaSolver.class.getName());

    protected final org.slf4j.Logger logger;
    
    protected long timeLimit = 0;

    protected long solutionLimit = 0;

    protected AbstractCryptaSolver() {
        this(true);
    }
    
    protected AbstractCryptaSolver(boolean withPrimaryLogger) {
        super();
        this.logger = JULogUtil.getLogger(AbstractCryptaSolver.class, withPrimaryLogger);
    }

    protected final void logOnCryptarithm(final ICryptaNode cryptarithm) {
        	logger.atInfo().setMessage("Declare instance:\ni {}").addArgument(() -> TransformWord.removeWhitespaces(TreeUtils.writeInorder(cryptarithm)));
            logger.atDebug().setMessage("Cryptarithm features:\n{}").addArgument(() -> TreeUtils.computeFeatures(cryptarithm));
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
