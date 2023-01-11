/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
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

    @Override
    public void setUp(final CryptaModel model) throws CryptaGameException {
        this.gameModel = model;
        this.userModel = makeUserDecisionModel(model);
        if (!solveGame()) {
            throw new CryptaGameException("Cryptarithm has no solution.");
        }
    }

    private boolean solveGame() {
        final Solver solver = gameModel.getModel().getSolver();
        if (solver.solve()) {
            LOGGER.log(Level.CONFIG, "display the current cryptarithm solution.\n{0}", gameModel);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isSolved() {
        return userModel.getSolution().isTotalSolution();
    }

    private static CryptaModel makeUserDecisionModel(final CryptaModel model) {
        Map<Character, IntVar> symbolsToVariables = new HashMap<>();
        final Model m = new Model("Cryptarithm-Decisions");
        model.getSolution().forEach((symbol, ivar) -> symbolsToVariables.put(symbol,
                m.intVar(ivar.getName(), ivar.getLB(), ivar.getUB(), false)));
        return new CryptaModel(m, symbolsToVariables);
    }

    private static Constraint makeDecision(final CryptaModel model, final CryptaGameDecision decision)
            throws CryptaGameException {
        final IntVar ivar = model.getSolution().getVar(decision.getSymbol());
        if (ivar == null) {
            throw new CryptaGameException("Cannot find variable for symbol: " + decision.getSymbol());
        }
        final IntVar val = model.getModel().intVar(decision.getValue());
        return ((ReExpression) decision.getOperator().getExpression().apply(ivar, val)).decompose();
    }

    private void propagate(final CryptaModel model, final Constraint decision) throws ContradictionException {
        final Model m = model.getModel();
        m.post(decision);
        LOGGER.log(Level.CONFIG, "propagate decision {0} in model {1}", new Object[] {decision, m.getName()});
        m.getSolver().propagate();
    }

    private boolean probeGameDecision(final Constraint decision) {
        try {
            propagate(gameModel, decision);
            return true;
        } catch (ContradictionException e) {
            LOGGER.log(Level.CONFIG, "solve decision {0} in model {1}",
                    new Object[] {decision, gameModel.getModel().getName()});
            return solveGame();
        }
    }

    private void propagateUserDecision(final Constraint decision) throws CryptaGameException {
        try {
            propagate(userModel, decision);
        } catch (ContradictionException e) {
            throw new CryptaGameException("decision solver should not fail!");
        }

    }

    private void refuteGameDecision(final Constraint decision) throws CryptaGameException {
        final Model m = gameModel.getModel();
        m.unpost(decision);
        final Constraint opposite = decision.getOpposite();
        m.post(opposite);
        LOGGER.log(Level.CONFIG, "solve decision {0} in model {1}", new Object[] {opposite, m.getName()});
        m.getSolver().reset();
        if (!solveGame()) {
            throw new CryptaGameException("Cannot refute decision" + decision);
        }

    }

    @Override
    public boolean takeDecision(final CryptaGameDecision decision) throws CryptaGameException {
        LOGGER.log(Level.INFO, "take decision ({0}).", decision);
        final Constraint gdec = makeDecision(gameModel, decision);
        final Constraint ddec = makeDecision(userModel, decision);
        if (probeGameDecision(gdec)) {
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
        // Do nothing
    }

    @Override
    public String toString() {
        return userModel.getSolution().toString();
    }

}
