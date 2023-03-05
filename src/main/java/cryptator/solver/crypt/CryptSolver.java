package cryptator.solver.crypt;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.logging.Level;

import cryptator.config.CryptaConfig;
import cryptator.solver.AbstractCryptaSolver;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolutionMap;
import cryptator.solver.CryptaSolverException;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.tree.TreeUtils;

public class CryptSolver extends AbstractCryptaSolver {

    @Override
    public boolean solve(ICryptaNode cryptarithm, CryptaConfig config, Consumer<ICryptaSolution> consumer)
            throws CryptaModelException, CryptaSolverException {
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
            CryptExec crypt = new CryptExec("/home/nono/workspace/cryptator/src/main/benchmarks/crypt/crypt");
            CryptConsumer cryptConsumer = new CryptConsumer(consumer);
            crypt.exec(b.toString().getBytes(), cryptConsumer);
            return cryptConsumer.getSolutionCount() > 0;
        } catch (IOException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Crypt solver error", e);
            Thread.currentThread().interrupt();
        }
        return false;
    }

    class CryptConsumer implements Consumer<String> {

        private static final String PCRYPT = "[\\sa-zA-Z\\+=]*";

        private static final String PSOL = "[\\s0-9\\+=]*";

        private static final String PSTATS = "\\s*[0-9]+ solution\\(s\\),\\s[0-9]+\\smsec.";

        private final Consumer<ICryptaSolution> consumer;

        private int solutionCount;

        private String current;

        public CryptConsumer(Consumer<ICryptaSolution> consumer) {
            super();
            this.consumer = consumer;
        }

        public final int getSolutionCount() {
            return solutionCount;
        }

        private void acceptCryptarithm(String str) {
            current = str.trim();
            LOGGER.finer(current);
        }

        private void acceptSolution(String str) {
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

        private void acceptStatistics(String str) {
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
            // // System.out.println(Arrays.toString(split));
            // StringBuilder b = new StringBuilder();
            // double runtime = Double.parseDouble(split[2]) / MS;
            // b.append(String.format("d TIME %.3f%n", runtime));
            // b.append("d NBSOLS ").append(split[0]).append("\n");
            //
            // System.out.println(b.toString());
        }

        private void acceptOther(String str) {
            LOGGER.finer(str::trim);
        }

        @Override
        public void accept(String str) {
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
