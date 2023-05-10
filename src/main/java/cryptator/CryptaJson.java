/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
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

public final class CryptaJson {

    private CryptaJson() {
        super();
    }

    public static SolveOutput solve(final SolveInput input) throws CryptaModelException, CryptaSolverException {
        final CryptaParserWrapper parser = new CryptaParserWrapper();
        final ICryptaNode node = parser.parse(input.getCryptarithm());
        final CryptatorConfig config = input.getConfig();

        final SolveOutput output = new SolveOutput(input);
        output.setSymbols(TreeUtils.computeSymbols(node));

        final ICryptaSolver solver = new AdaptiveSolver(false);
        solver.limitSolution(config.getSolutionLimit());
        solver.limitTime(config.getTimeLimit());
        solver.solve(node, input.getConfig(), s -> output.accept(node, s));

        return output;
    }

}
