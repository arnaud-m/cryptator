package cryptator.gen;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import cryptator.CryptaConfig;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolver;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.tree.TreeUtils;

public class GenerateConsumer implements Consumer<ICryptaNode> {


	private final CryptaSolver solver;

	private final CryptaConfig config;
	
	private final BiConsumer<ICryptaNode, ICryptaSolution> internal;
	
	public GenerateConsumer(CryptaSolver solver, CryptaConfig config,
			BiConsumer<ICryptaNode, ICryptaSolution> internal) {
		super();
		this.solver = solver;
		this.config = config;
		this.internal = internal;
		this.solver.limitSolution(2);
		
	}

	private static class SolutionCollect implements Consumer<ICryptaSolution> {
		
		private int solutionCount;
		
		private ICryptaSolution solution;

		@Override
		public void accept(ICryptaSolution u) {
			solutionCount++;
			this.solution = u;
		}	
		
		public final boolean hasUniqueSolution() {
			return solutionCount == 1;
		}

		public final ICryptaSolution getSolution() {
			return solution;
		}
		
	}
	
	@Override
	public void accept(ICryptaNode t) {
		TreeUtils.printInorder(t);
			try {
				final SolutionCollect collect = new SolutionCollect();
				solver.solve(t, config, collect);
				if(collect.hasUniqueSolution()) {
					System.err.println(">> FOUND !");
					internal.accept(t, collect.getSolution());
				}
			} catch (CryptaModelException e) {
				e.printStackTrace();
			}
	}
}
