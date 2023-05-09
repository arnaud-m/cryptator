/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import java.util.Optional;
import java.util.function.Consumer;

import org.chocosolver.solver.variables.IntVar;

import cryptator.CryptaOperator;
import cryptator.config.CryptaConfig;
import cryptator.solver.crypt.CryptSolver;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ICryptaSolver;
import cryptator.specs.ITraversalNodeConsumer;
import cryptator.tree.TreeTraversals;

public class AdaptiveSolver implements ICryptaSolver {

    private final CryptaSolver solver;
    private final Optional<CryptSolver> crypt;

    public AdaptiveSolver(final boolean useCrypt) {
        super();
        solver = new CryptaSolver();
        crypt = useCrypt ? Optional.of(new CryptSolver()) : Optional.empty();
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
        crypt.ifPresent(x -> x.limitTime(limit));
    }

    @Override
    public void limitSolution(final long limit) {
        solver.limitSolution(limit);
        crypt.ifPresent(x -> x.limitSolution(limit));
    }

    @Override
    public boolean solve(final ICryptaNode cryptarithm, final CryptaConfig config,
            final Consumer<ICryptaSolution> solutionConsumer) throws CryptaModelException, CryptaSolverException {
        final AdaptiveConsumer cons = new AdaptiveConsumer();
        TreeTraversals.preorderTraversal(cryptarithm, cons);
        final int threshold = computeThreshold(config.getArithmeticBase());
        if (cons.getMaxWordLength() > threshold) {
            solver.setBignum();
        } else {
            solver.unsetBignum();
            if (crypt.isPresent() && cons.isCryptAddition()) {
                return crypt.get().solve(cryptarithm, config, solutionConsumer);
            }
        }
        return solver.solve(cryptarithm, config, solutionConsumer);
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
