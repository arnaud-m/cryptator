/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.tree;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Deque;

import cryptator.solver.CryptaSolutionException;
import cryptator.specs.ICryptaEvaluation;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ITraversalNodeConsumer;

public class CryptaEvaluation implements ICryptaEvaluation {

    @Override
    public BigInteger evaluate(final ICryptaNode cryptarithm, final ICryptaSolution solution, final int base)
            throws CryptaEvaluationException {
        final EvaluationConsumer evaluationNodeConsumer = new EvaluationConsumer(solution, base);
        TreeTraversals.postorderTraversal(cryptarithm, evaluationNodeConsumer);
        return evaluationNodeConsumer.eval();
    }

    private static class EvaluationConsumer implements ITraversalNodeConsumer {

        private final ICryptaSolution solution;

        private final int base;

        private final Deque<BigInteger> stack = new ArrayDeque<>();

        private CryptaEvaluationException exception;

        EvaluationConsumer(final ICryptaSolution solution, final int base) {
            super();
            this.solution = solution;
            this.base = base;
        }

        private BigInteger getWordValue(final ICryptaNode node) throws CryptaEvaluationException {
            try {
                BigInteger v = BigInteger.ZERO;
                final BigInteger b = BigInteger.valueOf(base);
                for (char c : node.getWord()) {
                    final int digit = solution.getDigit(c);
                    if ((digit < 0) || (digit >= base)) {
                        throw new CryptaEvaluationException(
                                "cannot evaluate because of an invalid digit for the evaluation base.");
                    }
                    v = v.multiply(b).add(BigInteger.valueOf(digit));
                }
                return v;
            } catch (CryptaSolutionException e) {
                throw new CryptaEvaluationException("Cannot use a partial solution for evaluation", e);
            }
        }

        private BigInteger getConstValue(final ICryptaNode node) {
            return new BigInteger(new String(node.getWord()));
        }

        @Override
        public void accept(final ICryptaNode node, final int numNode) {
            // Check for the exception because it cannot be thrown here.
            if (exception == null) {
                if (node.isInternalNode()) {
                    final BigInteger b = stack.pop();
                    final BigInteger a = stack.pop();
                    stack.push(node.getOperator().getFunction().apply(a, b));
                } else {
                    try {
                        if (node.isConstant()) {
                            stack.push(getConstValue(node));
                        } else {
                            stack.push(getWordValue(node));
                        }
                    } catch (CryptaEvaluationException e) {
                        exception = e;
                    }
                }
            }
        }

        public BigInteger eval() throws CryptaEvaluationException {
            if (exception != null) {
                throw exception;
            }
            if (stack.size() != 1) {
                throw new CryptaEvaluationException("Invalid stack size at the end of evaluation.");
            }
            return new BigInteger(stack.peek().toString());
        }
    }

}
