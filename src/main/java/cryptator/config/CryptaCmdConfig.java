/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.config;

import org.kohsuke.args4j.Option;

public class CryptaCmdConfig extends CryptaLogConfig {

    public enum SolverType {
        SCALAR, BIGNUM, CRYPT, ADAPT, ADAPTC
    }

    @Option(name = "-s", aliases = {"--solver"}, usage = "Select the type of solver.")
    private SolverType solverType = SolverType.SCALAR;

    @Option(name = "--check", usage = "Check solutions by evaluation.")
    private boolean checkSolution;

    @Option(name = "--graphviz", usage = "Export solutions to graphviz format.")
    private boolean exportGraphiz;

    @Option(name = "--crypt", hidden = true, usage = "Set the crypt command.")
    private String cryptCommand = "crypt";

    public final SolverType getSolverType() {
        return solverType;
    }

    public final void setSolverType(final SolverType solverType) {
        this.solverType = solverType;
    }

    public final boolean isExportGraphiz() {
        return exportGraphiz;
    }

    public final boolean isCheckSolution() {
        return checkSolution;
    }

    @Deprecated(forRemoval = true)
    public final boolean useCrypt() {
        return solverType == SolverType.CRYPT;
    }

    public final String getCryptCommand() {
        return cryptCommand;
    }

    @Override
    public String toString() {
        return super.toString() + "\nc SOLVER " + getSolverType();
    }
}
