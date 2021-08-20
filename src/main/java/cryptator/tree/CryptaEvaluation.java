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
	public int evaluate(ICryptaNode cryptarithm, ICryptaSolution solution, int base) throws CryptaEvaluationException {
		final EvaluationConsumer evaluationNodeConsumer = new EvaluationConsumer(solution, base);
		TreeTraversals.postorderTraversal(cryptarithm, evaluationNodeConsumer);
		return evaluationNodeConsumer.eval();
	}	

	private static class EvaluationConsumer implements ITraversalNodeConsumer {

		private final ICryptaSolution solution;

		private final int base;

		private final Stack<Integer> stack = new Stack<Integer>();

		private CryptaEvaluationException exception;

		private ArithmeticException arithmeticException;



		public EvaluationConsumer(ICryptaSolution solution, int base) {
			super();
			this.solution = solution;
			this.base = base;
		}

		private Integer getWordValue(ICryptaNode node) throws CryptaEvaluationException {
			try {
				int v = 0;
				for (char c : node.getWord()) {
					final int digit = solution.getDigit(c);
					if(digit < 0 || digit >= base) {
						System.out.println(digit);
						System.out.println(base);
						throw new CryptaEvaluationException("cannot evaluate because of an invalid digit for the evaluation base.");
					}
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
			if(exception == null && arithmeticException==null) {
				if(node==null){
					stack.push(0);
				}
				else {
					if (node.isLeaf()) {
						try {
							stack.push(getWordValue(node));
						} catch (CryptaEvaluationException e) {
							exception = e;
						}
					} else {
						final int b = stack.pop();
						final int a = stack.pop();
						try {
							stack.push(node.getOperator().getFunction().applyAsInt(a, b));
						} catch (ArithmeticException e) {
							arithmeticException = e;
						}
					}
				}
			}
		}


		public int eval() throws CryptaEvaluationException {
			if(exception != null) throw exception;
			if(arithmeticException != null) return -1;
			if(stack.size() != 1) throw new CryptaEvaluationException("Invalid stack size at the end of evaluation.");
			return stack.peek();
		}
	}

}
