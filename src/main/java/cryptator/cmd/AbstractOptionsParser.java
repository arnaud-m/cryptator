/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
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
import java.util.Comparator;
import java.util.OptionalInt;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.OptionHandlerFilter;
import org.kohsuke.args4j.ParserProperties;
import org.kohsuke.args4j.spi.OptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cryptator.config.CryptaConfig;

public abstract class AbstractOptionsParser<E extends CryptaConfig> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractOptionsParser.class);
	   
    private static final OptionalInt ERROR_CODE = OptionalInt.of(64);
    private static final OptionalInt EXIT_CODE = OptionalInt.of(0);
    private static final OptionalInt EMPTY_CODE = OptionalInt.empty();

    protected final E config;

    private final String commandName;

    private final String argumentName;
    

    protected AbstractOptionsParser(final E config, final String commandName, final String argumentName) {
    	super();
        this.config = config;
        this.commandName = commandName;
        this.argumentName = argumentName;
    }
    
    private String getCommandName() {
        return commandName;
    }

    private String getArgumentName() {
        return argumentName;
    }

    protected abstract void configureLoggers();

    protected boolean checkConfiguration() {
        if (config.getArithmeticBase() < 2) {
            LOGGER.error("The Arithmetic base must be greater than 1.");
            return false;
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
        final ParserProperties properties = ParserProperties.defaults().withOptionSorter(new CryptaOptionSorter());
        final CmdLineParser parser = new CmdLineParser(config, properties);
        try {
            // parse the arguments.
            parser.parseArgument(args);
            configureLoggers();
            if (config.isDisplayHelp()) {
                logManual(parser);
                return EXIT_CODE;
            } else if (config.getArguments().isEmpty()) {
                logHelp(parser);
                return EXIT_CODE;
            } else if (checkConfiguration()) {
                if (checkArguments()) {
                    LOGGER.debug("Parse options [OK]");
                    LOGGER.trace("Parse configuration [OK]\n{}", config);
                    return EMPTY_CODE;
                } else {
                    LOGGER.error("Parse arguments [FAIL]\n{}", config.getArguments());
                }
            } else {
                LOGGER.error("Check options [FAIL]");
            }

        } catch (CmdLineException e) {
            LOGGER.error("Parse options [FAIL]", e);
        }
        logHelp(parser);
        return ERROR_CODE;
    }

    private String printUsage(final CmdLineParser parser, final OptionHandlerFilter filter) {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        parser.printUsage(new OutputStreamWriter(os), null, filter);
        return os.toString();
    }

    private void appendExample(final StringBuilder b, final CmdLineParser parser, final OptionHandlerFilter filter) {
        b.append("java ").append(getCommandName()).append(' ');
        final String opts = parser.printExample(filter);
        if (!opts.isEmpty()) {
            b.append(opts).append(" ");
        }
        if (filter == OptionHandlerFilter.REQUIRED) {
            b.append("[options...] ");
        }
        b.append(getArgumentName()).append('\n');
    }

    private void appendHeader(final StringBuilder b) {
        b.append("Help of ").append(getCommandName()).append("\n");
    }

    private void appendMessage(final StringBuilder b) {
        appendResource(b, "help/" + getCommandName() + ".txt");
    }

    private void appendExamples(final StringBuilder b) {
        appendResource(b, "help/" + getCommandName() + "-examples.txt");
    }

    private void appendResource(final StringBuilder b, final String resourceName) {
        final InputStream in = this.getClass().getClassLoader().getResourceAsStream(resourceName);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        reader.lines().forEach(x -> b.append(x).append('\n'));
    }

    private void appendFooter(final StringBuilder b) {
        b.append("\nReport bugs: <https://github.com/arnaud-m/cryptator/issues>\n");
        b.append("Cryptator home page: <https://github.com/arnaud-m/cryptator>\n");
    }

    private void appendShortFooter(final StringBuilder b) {
        b.append("\nTry the option '--help' for more information.\n");
    }

    private void logHelp(final CmdLineParser parser) {
        if (LOGGER.isInfoEnabled()) {
            final StringBuilder b = new StringBuilder();
            appendHeader(b);
            appendExample(b, parser, OptionHandlerFilter.REQUIRED);
            b.append("\n");
            appendMessage(b);
            b.append("\nPublic options:\n");
            b.append(printUsage(parser, OptionHandlerFilter.PUBLIC));
            appendShortFooter(b);
            LOGGER.info(b.toString());
        }
    }

    private void logManual(final CmdLineParser parser) {
        if (LOGGER.isInfoEnabled()) {
            final StringBuilder b = new StringBuilder();
            appendHeader(b);
            b.append("SYNOPSIS\n");
            appendExample(b, parser, OptionHandlerFilter.REQUIRED);
            b.append("\n");
            appendExample(b, parser, OptionHandlerFilter.PUBLIC);
            appendExample(b, parser, OptionHandlerFilter.ALL);
            b.append("\nDESCRIPTION\n");
            appendMessage(b);
            b.append("\nOPTIONS\n");
            b.append(printUsage(parser, OptionHandlerFilter.ALL));
            b.append("\nEXAMPLE\n");
            appendExamples(b);
            appendFooter(b);
            LOGGER.info(b.toString());
        }

    }

    private static class CryptaOptionSorter implements Comparator<OptionHandler> {

        private boolean longOption(final OptionDef x) {
            return x.toString().charAt(1) == '-';
        }

        @Override
        public int compare(final OptionHandler arg0, final OptionHandler arg1) {
            final OptionDef x = arg0.option;
            final OptionDef y = arg1.option;
            // Required option
            int comp = Boolean.compare(x.required(), y.required());
            if (comp == 0) {
                // Hidden options
                comp = Boolean.compare(x.hidden(), y.hidden());
                if (comp == 0) {
                    // Long options
                    comp = Boolean.compare(longOption(x), longOption(y));
                    if (comp == 0) {
                        // Option strings
                        comp = x.toString().compareTo(y.toString());
                    }
                }
            }
            return comp;
        }
    }

}
