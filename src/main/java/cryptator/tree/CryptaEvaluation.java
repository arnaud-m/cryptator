/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.tree;

import java.util.Stack;

import cryptator.solver.CryptaSolutionException;
import cryptator.specs.ICryptaEvaluation;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ITraversalNodeConsumer;

public class CryptaEvaluation implements ICryptaEvaluation {

	@Override
	public long evaluate(ICryptaNode cryptarithm, ICryptaSolution solution, int base) throws CryptaEvaluationException {
		final EvaluationConsumer evaluationNodeConsumer = new EvaluationConsumer(solution, base);
		TreeTraversals.postorderTraversal(cryptarithm, evaluationNodeConsumer);
		return evaluationNodeConsumer.eval();
	}	

	private static class EvaluationConsumer implements ITraversalNodeConsumer {

		private final ICryptaSolution solution;

		private final long base;

		private final Stack<Long> stack = new Stack<>();

		private CryptaEvaluationException exception;

		public EvaluationConsumer(ICryptaSolution solution, int base) {
			super();
			this.solution = solution;
			this.base = base;
		}

		private Long getWordValue(ICryptaNode node) throws CryptaEvaluationException {
			try {
				long v = 0;
				for (char c : node.getWord()) {
					final int digit = solution.getDigit(c);
					if(digit < 0 || digit >= base) throw new CryptaEvaluationException("cannot evaluate because of an invalid digit for the evaluation base.");
					v = v * base + digit;
				}
				return v;
			} catch (CryptaSolutionException e) {
				throw new CryptaEvaluationException("Cannot use a partial solution for evaluation", e);
			}
		}

		@Override
		public void accept(ICryptaNode node, int numNode) {
			// Check for the exception because it cannot be thrown here without changing the method signature.
			if(exception == null) {
				if(node.isLeaf()) {
					try {
						stack.push(getWordValue(node));
					} catch (CryptaEvaluationException e) {
						exception = e;
					}
				} else {
					final long b = stack.pop();
					final long a = stack.pop();
					stack.push(node.getOperator().getFunction().applyAsLong(a, b));
				}
			}
		}


		public long eval() throws CryptaEvaluationException {
			if(exception != null) throw exception;
			if(stack.size() != 1) throw new CryptaEvaluationException("Invalid stack size at the end of evaluation.");
			return stack.peek();
		}
	}

}
