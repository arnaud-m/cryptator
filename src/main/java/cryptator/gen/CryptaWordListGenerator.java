package cryptator.gen;

import java.util.List;
import java.util.function.BiConsumer;

import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;

import cryptator.CryptaConfig;
import cryptator.solver.AdaptiveSolver;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolver;
import cryptator.specs.ICryptaGenerator;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;

public class CryptaWordListGenerator implements ICryptaGenerator {

	public String[] words;
	
	public CryptaWordListGenerator(List<String> arguments) {
		super();
		this.words = arguments.toArray(new String[arguments.size()]);
	}

	public CryptaWordListGenerator(String[] words) {
		super();
		this.words = words;
	}

	@Override
	public void generate(BiConsumer<ICryptaNode, ICryptaSolution> consumer) throws CryptaModelException {
		if(words == null || words.length == 0) return;
		final CryptaGenModel gen = new CryptaGenModel(words);
		gen.postMemberCardConstraints();
		gen.postMemberMaxLenConstraint();
		gen.postMaxDigitCountConstraint(10);
		Solver s = gen.getModel().getSolver();
		// Solution sol = new Solution(gen.getModel());
		GenerateConsumer genConsumer= new GenerateConsumer(
				new AdaptiveSolver(3),
				new CryptaConfig(), //TODO Pass as ctor parameter
				consumer
				);
		while(s.solve()) {
			// TODO Log cryptarithm here
			genConsumer.accept(gen.recordCryptarithm());
		}
	}


}
