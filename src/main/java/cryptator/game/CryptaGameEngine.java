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
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

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

	private static final Logger LOGGER = Logger.getLogger(CryptaGameEngine.class.getName());

	public CryptaModel gameModel;

	public CryptaModel decisionModel;

	public CryptaGameEngine(CryptaModel model) {
		this.gameModel = model;
		this.decisionModel = makeDecisionModel(model);
	}


	@Override
	public void setUp(CryptaModel model) throws CryptaGameException {
		this.gameModel = model;
		this.decisionModel = makeDecisionModel(model);	
	}

	@Override
	public void forEachSymbolDomain(BiConsumer<Character, String> consumer) {
		decisionModel.getMap().forEach((s, v) -> {
			String str = v.toString();
			consumer.accept(s, str.replaceFirst(".*=\\s*", ""));	
		}
				);
	}

	@Override
	public boolean isSolved() {
		for(IntVar v : decisionModel.getMap().values()) {
			if(! v.isInstantiated()) return false;
		}
		return true;
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
		LOGGER.log(Level.CONFIG, "display the initial cryptarithm solution:\n{0}", gameModel.recordSolution());
	}

	private final static Constraint makeDecision(CryptaModel model, char symbol, CryptaOperator reOperator, int value) throws CryptaGameException {
		final IntVar var = model.getMap().get(symbol);
		if(var == null) throw new CryptaGameException("Cannot find variable for symbol: " + symbol);
		final IntVar val = model.getModel().intVar(value);
		return ( (ReExpression) reOperator.getExpression().apply(var, val)).decompose();
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
			propagate(decisionModel, decision);
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
	public boolean takeDecision(char symbol, CryptaOperator reOperator, int value) throws CryptaGameException {
		LOGGER.log(Level.INFO, "take decision ({0} {1} {2}).", new Object[] {symbol, reOperator, value});
		final IntVar var = gameModel.getMap().get(symbol);
		if(var == null) throw new CryptaGameException("Cannot find variable for symbol: " + symbol);
		final Constraint gdec = makeDecision(gameModel, symbol, reOperator, value);
		final Constraint ddec = makeDecision(decisionModel, symbol, reOperator, value);
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
	public void tearDown() {

	}


	@Override
	public String toString() {
		return "CryptaGameEngine [gameModel=" + gameModel + "\n, decisionModel=" + decisionModel + "]";
	}




}
