/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.specs;


import ch.qos.logback.classic.Level;
import cryptator.JULogUtil;

/**
 * The Interface ILogManager manages the logging verbosity.
 */
public interface ICryptaLogManager {

    /**
     * Sets the silent level.
     */
    default void setSilent() {
        JULogUtil.configureLoggers(Level.OFF);
    }

    /**
     * Sets the quiet level.
     */
    default void setQuiet() {
        JULogUtil.configureLoggers(Level.WARN);
    }

    /**
     * Sets the normal level.
     */
    default void setNormal() {
        JULogUtil.configureLoggers(Level.INFO);
    }

    /**
     * Sets the verbose level.
     */
    default void setVerbose() {
        JULogUtil.configureLoggers(Level.DEBUG);
    }

    /**
     * Sets the very verbose level.
     */
    default void setVeryVerbose() {
        JULogUtil.configureLoggers(Level.TRACE);
    }

    /**
     * Sets the debug level.
     */
    default void setDebug() {
        JULogUtil.configureLoggers(Level.TRACE);
    }
}
