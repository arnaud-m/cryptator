package cryptator;

import cryptator.config.CryptatorConfig;
import cryptator.json.SolveInput;
import cryptator.json.SolveOutput;
import cryptator.parser.CryptaParserWrapper;
import cryptator.solver.AdaptiveSolver;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolverException;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolver;
import cryptator.tree.TreeUtils;

public class CryptaJson {

	private CryptaJson() {
		super();
	}
	
	public static SolveOutput solve(SolveInput input) throws CryptaModelException, CryptaSolverException {
		final CryptaParserWrapper parser = new CryptaParserWrapper();
		final ICryptaNode node = parser.parse(input.getCryptarithm());
		CryptatorConfig config = input.getConfig();
		
		final SolveOutput output = new SolveOutput(input);
		output.setSymbols(TreeUtils.computeSymbols(node));
		
		final ICryptaSolver solver = new AdaptiveSolver();
		solver.limitSolution(config.getSolutionLimit());
		solver.limitTime(config.getTimeLimit());
		solver.solve(node, input.getConfig(), s -> output.accept(node, s));
		
		return output;
	}
	
}
