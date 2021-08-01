package cryptator.solver;

import java.util.function.Consumer;

import org.chocosolver.solver.Solver;

import cryptator.CryptaConfig;
import cryptator.specs.ICryptaModeler;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ICryptaSolver;

public class CryptaSolver implements ICryptaSolver {

	public final ICryptaModeler modeler = new CryptaModeler();
	
	public CryptaSolver() {}

	@Override
	public boolean solve(ICryptaNode cryptarithm, CryptaConfig config, Consumer<ICryptaSolution> solutionConsumer) {
		try {
			final CryptaModel m = modeler.model(cryptarithm, config);
			final Solver solver = m.getModel().getSolver();
			if(config.getTimeLimit() > 0) solver.limitTime(config.getTimeLimit());
			if(config.getSolutionLimit() > 0) {
				int n = config.getSolutionLimit(); 
				while(n > 0 && solver.solve()) {
					solutionConsumer.accept(m.recordSolution());
					n--;
				}
			} else {
				while(solver.solve()) {
					solutionConsumer.accept(m.recordSolution());
				}
			}
			solver.printStatistics();
			solver.showStatistics();
		} catch (CryptaModelException e) {
			e.printStackTrace();
		}
		return false;
	}

}
