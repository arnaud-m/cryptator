package cryptator.tree;

import java.util.Stack;

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

		private final Integer getWordValue(ICryptaNode node) {
			int v = 0;
			for (char c : node.getWord()) {
				// TODO Handle missing value ?
				v = v * base + solution.getDigit(c);
			}
			return v;
		}
		
		@Override
		public void accept(ICryptaNode node, int numNode) {
			if(node.isLeaf()) {
				stack.push(getWordValue(node));
			} else {
				final int b = stack.pop();
				final int a = stack.pop();
				stack.push(node.getOperator().getFunction().applyAsInt(a, b));
			}
		}
		
		public int peek() {
			// TODO Check that the stack contains a single value
			return stack.peek();
		}
	}
	
}