/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.config;

import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.ExplicitBooleanOptionHandler;

public class CryptaCmdConfig extends CryptaLogConfig {

    @Option(name = "-c", handler = ExplicitBooleanOptionHandler.class, usage = "check solutions by evaluation")
    private boolean checkSolution;

    @Option(name = "-g", handler = ExplicitBooleanOptionHandler.class, usage = "export solutions to graphviz format")
    private boolean exportGraphiz;

    @Option(name = "-l", handler = ExplicitBooleanOptionHandler.class, usage = "use the bignum model (only + and =)")
    private boolean useBigNum;

    // TODO Check the argument
    @Option(name = "--crypt-command", usage = "the crypt command")
    private String cryptCommand = "crypt";

    @Option(name = "-crypt", handler = ExplicitBooleanOptionHandler.class, usage = "use the crypt solver (only + and =)")
    private boolean useCrypt;

    public final boolean isExportGraphiz() {
        return exportGraphiz;
    }

    public final boolean isCheckSolution() {
        return checkSolution;
    }

    public final boolean useBignum() {
        return useBigNum;
    }

    public final boolean useCrypt() {
        return useCrypt;
    }

    public final void setUseCrypt(boolean useCrypt) {
        this.useCrypt = useCrypt;
    }

    public final String getCryptCommand() {
        return cryptCommand;
    }

    @Override
    public String toString() {
        return super.toString() + "\nc BIGNUM " + useBignum();
    }
}
