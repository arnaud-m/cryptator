/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import cryptator.config.CryptaConfig;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ICryptaSolver;
import cryptator.specs.ITraversalNodeConsumer;
import cryptator.tree.TreeTraversals;
import org.chocosolver.solver.variables.IntVar;

import java.util.function.Consumer;

public class AdaptiveSolver implements ICryptaSolver {

    public final CryptaSolver solver = new CryptaSolver();

    public AdaptiveSolver() {
        super();
    }

    public static int computeThreshold(int base) {
        final int n = IntVar.MAX_INT_BOUND;
        int prod = base;
        int i = 1;
        while (prod < n) {
            i++;
            prod *= base;
        }
        return i;
    }

    @Override
    public void limitTime(long limit) {
        solver.limitTime(limit);
    }

    @Override
    public void limitSolution(long limit) {
        solver.limitSolution(limit);
    }

    @Override
    public boolean solve(ICryptaNode cryptarithm, CryptaConfig config, Consumer<ICryptaSolution> solutionConsumer)
            throws CryptaModelException, CryptaSolverException {
        MaxLenConsumer cons = new MaxLenConsumer();
        TreeTraversals.preorderTraversal(cryptarithm, cons);
        final int threshold = computeThreshold(config.getArithmeticBase());
        if (cons.getMaxLength() > threshold) solver.setBignum();
        else solver.unsetBignum();
        return solver.solve(cryptarithm, config, solutionConsumer);
    }

    private static class MaxLenConsumer implements ITraversalNodeConsumer {

        private int maxLen;

        public final int getMaxLength() {
            return maxLen;
        }

        @Override
        public void accept(ICryptaNode node, int numNode) {
            if (node.isWord()) {
                final int len = node.getWord().length;
                if (len > maxLen) maxLen = len;
            }
        }
    }
}
