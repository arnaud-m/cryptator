/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.cmd;

import cryptator.specs.ICryptaLogManager;

public enum Verbosity {
    SILENT, QUIET, NORMAL, VERBOSE, VERY_VERBOSE, DEBUG;

    public void applyTo(ICryptaLogManager manager) {
        setVerbosity(manager, this);
    }

    public static void setVerbosity(ICryptaLogManager manager, Verbosity verbosity) {
        if (verbosity != null && manager != null) {
            if (verbosity.equals(SILENT)) {
                manager.setSilent();
            } else if (verbosity.equals(QUIET)) {
                manager.setQuiet();
            } else if (verbosity.equals(NORMAL)) {
                manager.setNormal();
            } else if (verbosity.equals(VERBOSE)) {
                manager.setVerbose();
            } else if (verbosity.equals(VERY_VERBOSE)) {
                manager.setVeryVerbose();
            } else if (verbosity.equals(DEBUG)) {
                manager.setDebug();
            }
        }
    }
}
