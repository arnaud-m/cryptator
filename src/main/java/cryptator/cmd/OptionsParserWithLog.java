/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.cmd;

import cryptator.config.CryptaLogConfig;
import cryptator.specs.ICryptaLogManager;

public class OptionsParserWithLog<E extends CryptaLogConfig> extends AbstractOptionsParser<E> {

    private final ICryptaLogManager logManager;

    public OptionsParserWithLog(final Class<?> mainClass, final E config, final String argumentName,
            final ICryptaLogManager logManager) {
        super(mainClass, config, argumentName);
        this.logManager = logManager;
    }

    @Override
    protected void configureLoggers() {
        config.getVerbosity().applyTo(logManager);
    }

}
