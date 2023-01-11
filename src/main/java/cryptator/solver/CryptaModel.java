/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import java.util.Map;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import cryptator.specs.IChocoModel;
import cryptator.specs.ICryptaSolution;

/**
 * The Class CryptaModel encapsulates the choco model and solution.
 */
public final class CryptaModel implements IChocoModel {

    /** The model. */
    private final Model model;

    /** The solution. */
    private final CryptaSolutionVars solution;

    /**
     * Instantiates a new model.
     *
     * @param model              the model
     * @param symbolsToVariables the symbols to variables used to instantiate the
     *                           solution
     */
    public CryptaModel(final Model model, final Map<Character, IntVar> symbolsToVariables) {
        super();
        this.model = model;
        this.solution = new CryptaSolutionVars(symbolsToVariables);
    }

    @Override
    public Model getModel() {
        return model;
    }

    /**
     * Gets the solution associated to the solver.
     *
     * @return the solution
     */
    public CryptaSolutionVars getSolution() {
        return solution;
    }

    /**
     * Record a solution into a new solution object independent from the solver.
     *
     * @return the new solution
     */
    public ICryptaSolution recordSolution() {
        return solution.recordSolution();
    }

    @Override
    public String toString() {
        return solution.toString();
    }

}
