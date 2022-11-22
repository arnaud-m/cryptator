/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import cryptator.cmd.CryptaBiConsumer;
import cryptator.cmd.AbstractOptionsParser;
import cryptator.cmd.WordArray;
import cryptator.config.CryptagenConfig;
import cryptator.gen.CryptaListGenerator;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolver;

public class Cryptagen {

    public static final Logger LOGGER = Logger.getLogger(Cryptagen.class.getName());

    private Cryptagen() {}

    public static void main(String[] args) {
        final int exitCode = doMain(args);
        System.exit(exitCode);
    }

    public static int doMain(String[] args) {
        JULogUtil.configureLoggers();
        int exitCode = -1;

        CryptagenOptionsParser optparser = new CryptagenOptionsParser();
        if (optparser.parseOptions(args)) {
            final CryptagenConfig config = optparser.getConfig();
            final WordArray words = buildWords(config.getArguments(), config);
            if(words != null) {
                exitCode = generate(words, config);
            }
        }
        JULogUtil.flushLogs();
        return exitCode;
    }


    private static List<String> readWords(String filename) {
        final List<String> words = new ArrayList<>();
        try (final Scanner s = new Scanner(new File(filename))) {
            while (s.hasNext()) {
                words.add(s.next());
            }
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "cant read words in file", e);
        }
        return words;			
    }


    private static WordArray buildNumbers(List<String> arguments, CryptagenConfig config) {
        final int lb = Integer.parseInt(arguments.get(0));
        try {
            final int ub = Integer.parseInt(arguments.get(1));
            return new WordArray(config.getCountryCode(), config.getLangCode(), lb, ub);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "fail reading the second integer argument.", e);
            return null;
        }
    }

    private static WordArray buildWords(List<String> arguments, CryptagenConfig config) {
        switch(arguments.size()) {
        case 1: {
            final List<String> words = readWords(arguments.get(0));
            return words.isEmpty() ? null : new WordArray(words, null);
        }
        case 2: {
            try {
                return buildNumbers(arguments, config);
            } catch (NumberFormatException e) {
                // Cannot build a number list.
                // So, the first argument must be the pathname of a words list
                final List<String> words = readWords(arguments.get(0));
                return words.isEmpty() ? null : new WordArray(words, arguments.get(1));
            }
        } default : {
            return new WordArray(arguments, null);
        }

        }
    }


    private static class CryptagenOptionsParser extends AbstractOptionsParser<CryptagenConfig> {

        public CryptagenOptionsParser() {
            super(Cryptagen.class, new CryptagenConfig());
        }

        @Override
        protected void configureLoggers() {
            if(config.isDryRun()) {
                getLogger().setLevel(config.isVerbose() ? Level.FINE : Level.CONFIG);	
            } else {
                if(config.isVerbose()) {
                    JULogUtil.setLevel(Level.CONFIG, getLogger());		
                } else {
                    JULogUtil.setLevel(Level.WARNING, CryptaSolver.LOGGER);
                }
            }
        }

        @Override
        public String getArgumentName() {
            return "WORDS...";
        }	
    }

    private static CryptaBiConsumer buildBiConsumer(final CryptagenConfig config) {
        CryptaBiConsumer consumer = new CryptaBiConsumer(LOGGER);
        consumer.withCryptarithmLog();
        if(config.isCheckSolution()) consumer.withSolutionCheck(config.getArithmeticBase());
        if(config.isExportGraphiz()) consumer.withGraphvizExport();
        return consumer;
    }

    private static int generate(WordArray words, CryptagenConfig config) {
        final CryptaListGenerator gen = new CryptaListGenerator(words, config, LOGGER);
        final CryptaBiConsumer cons = buildBiConsumer(config);
        try {
            gen.generate(cons);
        } catch (CryptaModelException e) {
            LOGGER.log(Level.SEVERE, "fail to build the model.", e);    
            return -1;  
        }
        return gen.getErrorCount() + cons.getErrorCount();
    }

}
