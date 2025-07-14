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

import cryptator.cmd.Verbosity;

public class CryptaLogConfig extends CryptaConfig {

    @Option(name = "-v", aliases = {"--verbose"}, usage = "Increase the verbosity of the program.")
    private Verbosity verbosity = Verbosity.NORMAL;

    public final Verbosity getVerbosity() {
        return verbosity;
    }

    public final void setVerbosity(final Verbosity verbosity) {
        this.verbosity = verbosity;
    }

}
