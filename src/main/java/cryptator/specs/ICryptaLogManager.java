/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.specs;

/**
 * The Interface ILogManager manages the logging verbosity.
 */
public interface ICryptaLogManager {

    /**
     * Sets the silent level.
     */
    void setSilent();

    /**
     * Sets the quiet level.
     */
    void setQuiet();

    /**
     * Sets the normal level.
     */
    void setNormal();

    /**
     * Sets the verbose level.
     */
    void setVerbose();

    /**
     * Sets the debug level.
     */
    void setDebug();
}
