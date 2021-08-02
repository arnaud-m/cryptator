package cryptator.game;

import java.util.HashMap;
import java.util.Map;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.expression.discrete.relational.ReExpression;
import org.chocosolver.solver.variables.IntVar;

import cryptator.CryptaOperator;
import cryptator.solver.CryptaModel;
import cryptator.specs.ICryptaGameEngine;

public class CryptaGameEngine implements ICryptaGameEngine {

	public final CryptaModel gameModel;

	public final CryptaModel decisionModel;

	public CryptaGameEngine(CryptaModel model) {
		this.gameModel = model;
		this.decisionModel = makeDecisionModel(model);
	}

	private final static CryptaModel makeDecisionModel(CryptaModel model) {
		Map<Character, IntVar> symbolsToVariables = new HashMap<Character, IntVar>();
		final Model m = new Model("Cryptarithm-Decisions");
		model.getMap().forEach( (symbol, var) -> {
			symbolsToVariables.put(symbol, m.intVar(var.getName(), var.getLB(), var.getUB(), false));
		});
		return new CryptaModel(m, symbolsToVariables);
	}

	public void setUp() throws CryptaGameException {
		Solver solver = gameModel.getModel().getSolver();
		if( ! solver.solve() ) throw new CryptaGameException("Cryptarithm has no solution.");
		System.out.println(gameModel.recordSolution());
	}

	private final Constraint makeDecision(char symbol, CryptaOperator reOperator, int value) throws CryptaGameException {
		final IntVar var = gameModel.getMap().get(symbol);
		if(var == null) throw new CryptaGameException("Cannot find variable for symbol: " + symbol);
		final IntVar val = gameModel.getModel().intVar(value);
		return ( (ReExpression) reOperator.getExpression().apply(var, val)).decompose();
	}
	
	private final boolean probeDecision(Constraint decision) {
		final Model m = gameModel.getModel();
		m.post(decision);
		
//		m.getSolver().reset();
//		return m.getSolver().solve();

		try {
			m.getSolver().propagate();
			// System.out.println("\nPROPAGATE");
			return true;
		} catch (ContradictionException e) {
			return m.getSolver().solve();
		}
	}

	private final void refuteDecision(Constraint decision) throws CryptaGameException {
		final Model m = gameModel.getModel();
		m.unpost(decision);
		m.post(decision.getOpposite());
		// FIXME m.getSolver().restart();
		m.getSolver().reset();
		if( ! m.getSolver().solve()) throw new CryptaGameException("Cannot refute decision" + decision);
	}

	@Override
	public boolean takeDecision(char symbol, CryptaOperator reOperator, int value) throws CryptaGameException {
		final IntVar var = gameModel.getMap().get(symbol);
		if(var == null) throw new CryptaGameException("Cannot find variable for symbol: " + symbol);
		final Constraint dec = makeDecision(symbol, reOperator, value);
		if(probeDecision(dec)) {
			// System.out.println("\nPROBE " + dec + "\n" + gameModel.recordSolution());
			return true;
		}
		else {
			refuteDecision(dec);
			// System.out.println("\nREFUTE " + dec + "\n" + gameModel.recordSolution());
			return false;
		}
	}

	public void tearDown() {

	}




}
