package cryptator.solver;

import java.util.function.Consumer;

import cryptator.CryptaConfig;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ICryptaSolver;
import cryptator.specs.ITraversalNodeConsumer;
import cryptator.tree.TreeTraversals;

public class AdaptiveSolver implements ICryptaSolver {

	public final CryptaSolver solver = new CryptaSolver();

	public final int threshold;

	// FIXME the threshold depends on the base !
	public AdaptiveSolver(int threshold) {
		super();
		this.threshold = threshold;
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
		if(cons.getMaxLength() > threshold) solver.setBignum();
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
			if(node.isLeaf()) {
				final int len = node.getWord().length;
				if(len > maxLen) maxLen = len;
			}
		}	
	}
}
