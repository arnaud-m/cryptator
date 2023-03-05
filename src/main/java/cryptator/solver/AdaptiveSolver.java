/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import java.util.function.Consumer;

import org.chocosolver.solver.variables.IntVar;

import cryptator.CryptaOperator;
import cryptator.config.CryptaCmdConfig;
import cryptator.config.CryptaConfig;
import cryptator.solver.crypt.CryptSolver;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ICryptaSolver;
import cryptator.specs.ITraversalNodeConsumer;
import cryptator.tree.TreeTraversals;

public class AdaptiveSolver implements ICryptaSolver {

    private final CryptaSolver solver = new CryptaSolver();
    private final CryptSolver crypt = new CryptSolver();

    public AdaptiveSolver() {
        super();
    }

    public static int computeThreshold(final int base) {
        int n = IntVar.MAX_INT_BOUND;
        int i = 1;
        while (n >= base) {
            n /= base;
            i++;
        }
        return i;
    }

    @Override
    public void limitTime(final long limit) {
        solver.limitTime(limit);
        crypt.limitTime(limit);
    }

    @Override
    public void limitSolution(final long limit) {
        solver.limitSolution(limit);
        crypt.limitSolution(limit);

    }

    public static final boolean useCrypt(final CryptaConfig config) {
        if (config instanceof CryptaCmdConfig) {
            return ((CryptaCmdConfig) config).useCrypt();
        } else {
            return false;
        }
    }

    @Override
    public boolean solve(final ICryptaNode cryptarithm, final CryptaConfig config,
            final Consumer<ICryptaSolution> solutionConsumer) throws CryptaModelException, CryptaSolverException {
        final AdaptiveConsumer cons = new AdaptiveConsumer();
        TreeTraversals.preorderTraversal(cryptarithm, cons);
        final int threshold = computeThreshold(config.getArithmeticBase());
        ICryptaSolver s = solver;
        if (cons.getMaxWordLength() > threshold) {
            solver.setBignum();
        } else {
            solver.unsetBignum();
            if (useCrypt(config) && cons.isCryptAddition()) {
                s = crypt;
            }
        }
        return s.solve(cryptarithm, config, solutionConsumer);
    }

    private static class AdaptiveConsumer implements ITraversalNodeConsumer {

        private int maxWordLength = 0;

        private boolean cryptAddition = true;

        @Override
        public void accept(final ICryptaNode node, final int numNode) {
            if (node.isWord()) {
                maxWordLength = Math.max(maxWordLength, node.getWord().length);
            } else if (node.isInternalNode()) {
                if (node.getOperator() == CryptaOperator.EQ) {
                    cryptAddition &= (!node.getLeftChild().isInternalNode() || !node.getRightChild().isInternalNode());
                } else if (node.getOperator() != CryptaOperator.ADD) {
                    cryptAddition = false;
                }
            }
        }

        public final int getMaxWordLength() {
            return maxWordLength;
        }

        public final boolean isCryptAddition() {
            return cryptAddition;
        }

    }

}
