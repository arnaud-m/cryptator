/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.cmd;

import cryptator.config.CryptaLogConfig;
import cryptator.specs.ICryptaLogManager;

public class OptionsParserWithLog<E extends CryptaLogConfig> extends AbstractOptionsParser<E> {

    private final ICryptaLogManager logManager;

    public OptionsParserWithLog(Class<?> mainClass, E config, String argumentName, ICryptaLogManager logManager) {
        super(mainClass, config, argumentName);
        this.logManager = logManager;
    }

    @Override
    protected void configureLoggers() {
        config.getVerbosity().applyTo(logManager);
    }

}
