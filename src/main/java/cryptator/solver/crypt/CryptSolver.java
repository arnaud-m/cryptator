/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver.crypt;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.logging.Level;

import cryptator.config.CryptaCmdConfig;
import cryptator.config.CryptaConfig;
import cryptator.solver.AbstractCryptaSolver;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolutionMap;
import cryptator.solver.CryptaSolverException;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.tree.TreeUtils;

/**
 * The Class CryptSolver is a wrapper for the crypt solver in C.
 */
public class CryptSolver extends AbstractCryptaSolver {

    @Override
    public boolean solve(final ICryptaNode cryptarithm, final CryptaConfig config,
            final Consumer<ICryptaSolution> consumer) throws CryptaModelException, CryptaSolverException {
        logOnCryptarithm(cryptarithm);
        logOnConfiguration(config);
        StringBuilder b = new StringBuilder();
        // Set the solution limit if any
        if (solutionLimit > 0) {
            b.append("limit ").append(solutionLimit).append("\n");
        }
        // The time limit is ignored.

        // Enter the cryptarithm
        b.append(TreeUtils.writeInorder(cryptarithm)).append("\n");
        // Solve the cryptarithm with the crypt solver
        try {
            final CryptExec crypt = new CryptExec(((CryptaCmdConfig) config).getCryptCommand());
            final CryptConsumer cryptConsumer = new CryptConsumer(consumer);
            crypt.exec(b.toString().getBytes(), cryptConsumer);
            return cryptConsumer.getSolutionCount() > 0;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Crypt solver error", e);
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Crypt solver interruption", e);
            Thread.currentThread().interrupt();
        }
        return false;
    }

    /**
     * The Class CryptConsumer.
     */
    private static class CryptConsumer implements Consumer<String> {

        /** The constant pattern PCRYPT that matches a cryptarithm. */
        private static final String PCRYPT = "[\\sa-zA-Z\\+=]*";

        /** The constant pattern PSOL that matches a cryptarithm solution. */
        private static final String PSOL = "[\\s0-9\\+=]*";

        /** The constant pattern PSTATS that matches solution statistics. */
        private static final String PSTATS = "\\s*[0-9]+ solution\\(s\\),\\s[0-9]+\\smsec.";

        /** The consumer for the solution. */
        private final Consumer<ICryptaSolution> consumer;

        /** The solution count. */
        private int solutionCount;

        /** The current cryptarithm. */
        private String current;

        /**
         * Instantiates a new crypt output consumer.
         *
         * @param consumer the internal solution consumer
         */
        public CryptConsumer(final Consumer<ICryptaSolution> consumer) {
            super();
            this.consumer = consumer;
        }

        /**
         * Gets the solution count.
         *
         * @return the solution count
         */
        public final int getSolutionCount() {
            return solutionCount;
        }

        /**
         * Accept a cryptarithm.
         *
         * @param str the cryptarithm line
         */
        private void acceptCryptarithm(final String str) {
            current = str.trim();
            LOGGER.finer(current);
        }

        /**
         * Accept a solution.
         *
         * @param str the solution line
         */
        private void acceptSolution(final String str) {
            final String solution = str.trim();
            LOGGER.finer(solution);
            final Map<Character, Integer> map = new TreeMap<>();
            final int n = current.length();
            for (int i = 0; i < n; i++) {
                final char letter = current.charAt(i);
                if (Character.isLetter(letter)) {
                    final Integer digit = Integer.parseInt(solution.substring(i, i + 1));
                    map.putIfAbsent(letter, digit);
                }
            }
            consumer.accept(new CryptaSolutionMap(map));
        }

        /**
         * Accept solution statistics.
         *
         * @param str the statistics line
         */
        private void acceptStatistics(final String str) {
            if (LOGGER.isLoggable(Level.INFO)) {
                final String stats = str.trim();
                LOGGER.finer(stats);
                final String[] split = stats.split("\\s+");
                double runtime = Double.parseDouble(split[2]) / MS;
                solutionCount = Integer.parseInt(split[0]);
                final String format = "Solver diagnostics:\nd TIME %.3f\nd NBSOLS %s";
                final String diagnostics = String.format(format, runtime, split[0]);
                LOGGER.info(diagnostics);
            }
        }

        /**
         * Accept other line.
         *
         * @param str the other line
         */
        private void acceptOther(final String str) {
            LOGGER.finer(str::trim);
        }

        /**
         * Accept a line of crypt output.
         *
         * @param str the output line
         */
        @Override
        public void accept(final String str) {
            if (str.matches(PCRYPT)) {
                acceptCryptarithm(str);
            } else if (str.matches(PSOL)) {
                acceptSolution(str);
            } else if (str.matches(PSTATS)) {
                acceptStatistics(str);
            } else {
                acceptOther(str);
            }
        }
    }

}
