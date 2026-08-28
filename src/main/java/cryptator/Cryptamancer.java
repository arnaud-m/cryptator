/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.util.OptionalInt;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cryptator.cmd.OptionsParserWithLog;
import cryptator.config.CryptaConfig;
import cryptator.config.CryptaLogConfig;
import cryptator.game.CryptaGameDecision;
import cryptator.game.CryptaGameEngine;
import cryptator.game.CryptaGameException;
import cryptator.parser.CryptaParserException;
import cryptator.parser.CryptaParserWrapper;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaModeler;
import cryptator.specs.ICryptaGameEngine;
import cryptator.specs.ICryptaNode;

public final class Cryptamancer {

	private static final Logger LOGGER = LoggerFactory.getLogger(Cryptamancer.class);
	       
    private static class CryptamancerOptionsParser extends OptionsParserWithLog<CryptaLogConfig> {

        private static final String ARG_NAME = "CRYPTARITHM";

        protected CryptamancerOptionsParser() {
            super(Cryptamancer.class, new CryptaLogConfig(), ARG_NAME, JULogUtil.getDefaultLogManager());
        }

        @Override
        protected boolean checkArguments() {
            return config.getArguments().size() == 1;
        }
    }

    private Cryptamancer() {
        super();
    }

    public static ICryptaNode parseCryptarithm(final CryptaLogConfig config) {
        final String cryptarithm = config.getArguments().get(0);
        try {
            return Cryptator.parseCryptarithm(cryptarithm, new CryptaParserWrapper(), LOGGER);
        } catch (CryptaParserException e) {
            LOGGER.error("Parse cryptarithm {} [FAIL]", cryptarithm, e);
            return null;
        }
    }

    public static ICryptaGameEngine buildEngine(final ICryptaNode node, final CryptaConfig config) {
        final CryptaModeler modeler = new CryptaModeler();
        try {
            final CryptaGameEngine engine = new CryptaGameEngine();
            engine.setUp(modeler.model(node, config));
            return engine;
        } catch (CryptaGameException | CryptaModelException e) {
            LOGGER.error("Failed to build the game engine", e);
            return null;
        }
    }

    private static void play(final ICryptaGameEngine engine) {
        final Scanner scanner = new Scanner(System.in);
        int n = 1;
        while ((!engine.isSolved())) {
            LOGGER.info("Turn {}\nEnter a decision (symbol operator value):", n);
            try {
                final CryptaGameDecision decision = CryptaGameDecision.parseDecision(scanner);
                if (decision == null) {
                    LOGGER.warn("Cannot parse the decision.");
                } else {
                    final boolean answer = engine.takeDecision(decision);
                    if (answer) {
                        LOGGER.info("Decision accepted.");
                    } else {
                        LOGGER.info("Decision rejected.");
                    }
                    LOGGER.info("Display the current partial solution.\n{}", engine);
                }
            } catch (CryptaGameException e) {
                LOGGER.warn("Failure while taking the decision.", e);
            }
            n++;
        }
    }

    public static void main(final String[] args) throws Exception {
        JULogUtil.configureDefaultLoggers();

        CryptamancerOptionsParser optparser = new CryptamancerOptionsParser();
        final OptionalInt parserExitCode = optparser.parseOptions(args);
        if (parserExitCode.isPresent()) {
            return;
        }
        final CryptaLogConfig config = optparser.getConfig();

        final ICryptaNode node = parseCryptarithm(config);
        if (node == null) {
            return;
        }

        final ICryptaGameEngine engine = buildEngine(node, config);
        if (engine == null) {
            return;
        }

        play(engine);

        engine.tearDown();
    }

}
