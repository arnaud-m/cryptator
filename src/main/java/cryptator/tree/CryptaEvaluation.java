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
	public int evaluate(ICryptaNode cryptarithm, ICryptaSolution solution, int base) {
		EvaluationConsumer evaluationNodeConsumer = new EvaluationConsumer(solution, base);
		TreeTraversals.postorderTraversal(cryptarithm, evaluationNodeConsumer);
		return evaluationNodeConsumer.peek();
	}	
	
	private static class EvaluationConsumer implements ITraversalNodeConsumer {

		private final ICryptaSolution solution;
		
		private final int base;
		
		private final Stack<Integer> stack = new Stack<Integer>();
		
		
		public EvaluationConsumer(ICryptaSolution solution, int base) {
			super();
			this.solution = solution;
			this.base = base;
		}

		private Integer getWordValue(ICryptaNode node) throws CryptaSolutionException {
			int v = 0;
			for (char c : node.getWord()) {
				v = v * base + solution.getDigit(c);
			}
			return v;
		}
		
		@Override
		public void accept(ICryptaNode node, int numNode) {
			try {
				if(node.isLeaf()) {
					stack.push(getWordValue(node));
				} else {
					final int b = stack.pop();
					final int a = stack.pop();
					stack.push(node.getOperator().getFunction().applyAsInt(a, b));
				}
			}
			catch(CryptaSolutionException e){
				System.out.println(e.getMessage());
			}
		}
		
		public int peek() {
			// TODO Check that the stack contains a single value
			try {
				return stack.peek();
			}
			catch(Exception e){
				System.out.println(e.getMessage());
				return -1;
			}
		}
	}
	
}