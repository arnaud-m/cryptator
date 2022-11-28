/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.cmd;

import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionHandlerFilter;

import cryptator.config.CryptaConfig;

public abstract class AbstractOptionsParser<E extends CryptaConfig> {

    private final Class<?> mainClass;

    protected final E config;

    protected AbstractOptionsParser(final Class<?> mainClass, final E config) {
        super();
        this.mainClass = mainClass;
        this.config = config;
    }

    public final Logger getLogger() {
        return Logger.getLogger(mainClass.getName());
    }

    protected final String getCommandName() {
        return mainClass.getName();
    }

    protected abstract String getArgumentName();

    protected abstract void configureLoggers();

    protected boolean checkConfiguration() {
        if (config.getArithmeticBase() < 2) {
            getLogger().severe("The Arithmetic base must be greater than 1.");
            return false;
        }
        if ((config.getRelaxMinDigitOccurence() < 0) || (config.getRelaxMaxDigitOccurence() < 0)) {
            getLogger().severe("Min/Max digit occurence is ignored because it cannot be negative.");
        }
        return true;
    }

    protected boolean checkArguments() {
        return !config.getArguments().isEmpty();
    }

    public final E getConfig() {
        return config;
    }

    public final boolean parseOptions(final String[] args) {
        final CmdLineParser parser = new CmdLineParser(config);
        try {
            // parse the arguments.
            parser.parseArgument(args);
            configureLoggers();
            if (checkConfiguration()) {
                if (checkArguments()) {
                    getLogger().log(Level.CONFIG, "Parse options [OK]\n{0}", config);
                    return true;
                } else {
                    getLogger().log(Level.SEVERE, "Parse arguments [FAIL]\n{0}", config.getArguments());
                }
            } else {
                getLogger().log(Level.SEVERE, "Check options [FAIL]");
            }

        } catch (CmdLineException e) {
            getLogger().log(Level.SEVERE, "Parse options [FAIL]", e);
        }

        if (getLogger().isLoggable(Level.INFO)) {
            getLogger().info(buildHelpMessage(parser));
        }
        return false;

    }

    private String buildHelpMessage(CmdLineParser parser) {
        StringBuilder b = new StringBuilder();
        b.append(" Help:\n");
        b.append("java ").append(getCommandName()).append(" [options...] ").append(getArgumentName()).append("\n");
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        parser.printUsage(os);
        b.append(os.toString());
        b.append("\nExamples:");
        b.append("\njava ").append(getCommandName()).append(" ")
                .append(parser.printExample(OptionHandlerFilter.REQUIRED)).append(" ").append(getArgumentName());

        b.append("\njava ").append(getCommandName()).append(" ").append(parser.printExample(OptionHandlerFilter.ALL))
                .append(" ").append(getArgumentName());
        return b.toString();
    }
}
