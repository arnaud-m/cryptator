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

/**
 * The Enum Verbosity represents the verbosity levels of a program.
 */
public enum Verbosity {

    /** The silent level. */
    SILENT,
    /** The quiet level. */
    QUIET,
    /** The normal level. */
    NORMAL,
    /** The verbose level. */
    VERBOSE,
    /** The very verbose level. */
    VERY_VERBOSE,
    /** The debug level. */
    DEBUG;

    /**
     * Apply this verbosity level to a logging manager.
     *
     * @param manager the logging manager
     */
    public void applyTo(final ICryptaLogManager manager) {
        setVerbosity(manager, this);
    }

    /**
     * Sets the verbosity of a logging manager.
     *
     * @param manager   the manager
     * @param verbosity the verbosity level
     */
    public static void setVerbosity(final ICryptaLogManager manager, final Verbosity verbosity) {
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
