/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.game;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.expression.discrete.relational.ReExpression;
import org.chocosolver.solver.variables.IntVar;

import cryptator.solver.CryptaModel;
import cryptator.specs.ICryptaGameEngine;

public class CryptaGameEngine implements ICryptaGameEngine {

	public static final Logger LOGGER = Logger.getLogger(CryptaGameEngine.class.getName());

	private CryptaModel gameModel;

	private CryptaModel userModel;

	public CryptaGameEngine() {}

	@Override
	public void setUp(CryptaModel model) throws CryptaGameException {
		this.gameModel = model;
		this.userModel = makeUserDecisionModel(model);	
		final Solver solver = gameModel.getModel().getSolver();
		if( ! solver.solve() ) throw new CryptaGameException("Cryptarithm has no solution.");
		LOGGER.log(Level.CONFIG, "display the initial cryptarithm solution.\n{0}", gameModel.recordSolution());
	}


	@Override
	public boolean isSolved() {
		return userModel.getSolution().isTotalSolution();
	}


	private final static CryptaModel makeUserDecisionModel(CryptaModel model) {
		Map<Character, IntVar> symbolsToVariables = new HashMap<Character, IntVar>();
		final Model m = new Model("Cryptarithm-Decisions");
		model.getSolution().forEach( (symbol, var) -> {
			symbolsToVariables.put(symbol, m.intVar(var.getName(), var.getLB(), var.getUB(), false));
		});
		return new CryptaModel(m, symbolsToVariables);
	}

	private final static Constraint makeDecision(CryptaModel model, CryptaGameDecision decision) throws CryptaGameException {
		final IntVar var = model.getSolution().getVar(decision.getSymbol());
		if(var == null) throw new CryptaGameException("Cannot find variable for symbol: " + decision.getSymbol());
		final IntVar val = model.getModel().intVar(decision.getValue());
		return ( (ReExpression) decision.getOperator().getExpression().apply(var, val)).decompose();
	}

	private final void propagate(CryptaModel model, Constraint decision) throws ContradictionException {
		final Model m = model.getModel();
		m.post(decision);
		LOGGER.log(Level.CONFIG, "propagate decision {0} in model {1}", new Object[] {decision, m.getName()});
		m.getSolver().propagate();
	}

	private final boolean probeGameDecision(Constraint decision) {
		//		final Model m = gameModel.getModel();
		//		m.post(decision);	
		//		m.getSolver().reset();
		//		return m.getSolver().solve();

		try {
			propagate(gameModel, decision);
			return true;
		} catch (ContradictionException e) {
			final Model m = gameModel.getModel();
			LOGGER.log(Level.CONFIG, "solve decision {0} in model {1}", new Object[] {decision, m.getName()});
			return m.getSolver().solve();
		}
	}


	private final void propagateUserDecision(Constraint decision) throws CryptaGameException {
		try {
			propagate(userModel, decision);
		} catch (ContradictionException e) {
			throw new CryptaGameException("decision solver should not fail !");
		}

	}

	private final void refuteGameDecision(Constraint decision) throws CryptaGameException {
		final Model m = gameModel.getModel();
		m.unpost(decision);
		final Constraint opposite = decision.getOpposite();
		m.post(opposite);
		// FIXME m.getSolver().restart();
		LOGGER.log(Level.CONFIG, "solve decision {0} in model {1}", new Object[] {opposite, m.getName()});
		m.getSolver().reset();
		if( ! m.getSolver().solve()) throw new CryptaGameException("Cannot refute decision" + decision);

	}

	@Override
	public boolean takeDecision(CryptaGameDecision decision) throws CryptaGameException {
		LOGGER.log(Level.INFO, "take decision ({0})).", decision);
		final Constraint gdec = makeDecision(gameModel, decision);
		final Constraint ddec = makeDecision(userModel, decision);
		if(probeGameDecision(gdec)) {
			propagateUserDecision(ddec);
			return true;
		} else {
			refuteGameDecision(gdec);
			propagateUserDecision(ddec.getOpposite());
			return false;
		}
	}

	@Override
	public void tearDown() {}


	@Override
	public String toString() {
		return userModel.getSolution().toString();
	}




}
