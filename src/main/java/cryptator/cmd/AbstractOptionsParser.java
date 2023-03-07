/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.cmd;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.OptionalInt;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionHandlerFilter;

import cryptator.config.CryptaConfig;

public abstract class AbstractOptionsParser<E extends CryptaConfig> {

    private static final OptionalInt ERROR_CODE = OptionalInt.of(64);
    private static final OptionalInt EXIT_CODE = OptionalInt.of(0);
    private static final OptionalInt EMPTY_CODE = OptionalInt.empty();

    private final Class<?> mainClass;

    protected final E config;

    private final String argumentName;

    protected AbstractOptionsParser(final Class<?> mainClass, final E config, final String argumentName) {
        super();
        this.mainClass = mainClass;
        this.config = config;
        this.argumentName = argumentName;
    }

    public final Logger getLogger() {
        return Logger.getLogger(mainClass.getName());
    }

    private String getCommandName() {
        return mainClass.getName();
    }

    private String getArgumentName() {
        return argumentName;
    }

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

    public enum CmdLineParserStatus {
        ERROR, EXIT, CONTINUE
    }

    public final OptionalInt parseOptions(final String[] args) {

        final CmdLineParser parser = new CmdLineParser(config);
        try {
            // parse the arguments.
            parser.parseArgument(args);
            configureLoggers();
            if (config.isHelpMessage()) {
                logHelp(parser);
                return EXIT_CODE;
            } else if (config.getArguments().isEmpty()) {
                logConciseHelp(parser);
                return EXIT_CODE;
            } else if (checkConfiguration()) {
                if (checkArguments()) {
                    getLogger().log(Level.CONFIG, "Parse options [OK]");
                    getLogger().log(Level.FINE, "Parse configuration [OK]\n{0}", config);
                    return EMPTY_CODE;
                } else {
                    getLogger().log(Level.SEVERE, "Parse arguments [FAIL]\n{0}", config.getArguments());
                }
            } else {
                getLogger().log(Level.SEVERE, "Check options [FAIL]");
            }

        } catch (CmdLineException e) {
            getLogger().log(Level.SEVERE, "Parse options [FAIL]", e);
        }
        logConciseHelp(parser);
        return ERROR_CODE;
    }

    private final void logConciseHelp(CmdLineParser parser) {
        if (getLogger().isLoggable(Level.INFO)) {
            getLogger().info(buildHelpMessage(parser, OptionHandlerFilter.PUBLIC));
        }

    }

    private final void logHelp(CmdLineParser parser) {
        if (getLogger().isLoggable(Level.INFO)) {
            getLogger().info(buildHelpMessage(parser, OptionHandlerFilter.PUBLIC));
        }
    }

    private String printUsage(final CmdLineParser parser, final OptionHandlerFilter filter) {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        parser.printUsage(new OutputStreamWriter(os), null, filter);
        return os.toString();
    }

    private String printExample(final String options) {
        return "java " + getCommandName() + " " + options + " " + getArgumentName();
    }

    private String printExample(final CmdLineParser parser, final OptionHandlerFilter filter) {
        return printExample(parser.printExample(filter));
    }

    private void appendHeader(final StringBuilder b) {
        b.append("Help of ").append(getCommandName()).append("\n\n");
        b.append(printExample("[options...]")).append("\n\n");
    }

    private void appendMessage(final StringBuilder b) {
        appendResource(b, "help/" + getCommandName() + ".txt");
    }

    private void appendResource(final StringBuilder b, final String resourceName) {
        final InputStream in = mainClass.getClassLoader().getResourceAsStream(resourceName);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        reader.lines().forEach(x -> b.append(x).append('\n'));
    }

    private void appendFooter(final StringBuilder b) {
        b.append("\n\nReport bugs: <https://github.com/arnaud-m/cryptator/issues>\n");
        b.append("Cryptator home page: <https://github.com/arnaud-m/cryptator>\n");
    }

    private String buildHelpMessage(final CmdLineParser parser, final OptionHandlerFilter filter) {
        final StringBuilder b = new StringBuilder();
        appendHeader(b);
        appendMessage(b);
        b.append("\nUsage:\n");
        b.append(printUsage(parser, filter));
        b.append("\nExamples:");
        b.append("\n").append(printExample(parser, OptionHandlerFilter.REQUIRED));
        b.append("\n").append(printExample(parser, filter));
        appendFooter(b);
        return b.toString();
    }

}
