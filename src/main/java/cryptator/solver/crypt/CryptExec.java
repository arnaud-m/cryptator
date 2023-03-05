/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver.crypt;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

class CryptConsumer implements Consumer<String> {

    private static final String PCRYPT = "[\\sa-zA-Z\\+=]*";

    private static final String PSOL = "[\\s0-9\\+=]*";

    private static final String PSTATS = "\\s*[0-9]+ solution\\(s\\),\\s[0-9]+\\smsec.";

    String current;

    private void acceptCryptarithm(String str) {
        current = str.trim();
        System.out.println("i " + current);
    }

    private void acceptSolution(String str) {
        final String solution = str.trim();
        System.out.println("v " + solution);
        final Map<Character, Integer> map = new TreeMap<>();
        final int n = current.length();
        for (int i = 0; i < n; i++) {
            final char letter = current.charAt(i);
            if (Character.isLetter(letter)) {
                final Integer digit = Integer.parseInt(solution.substring(i, i + 1));
                map.putIfAbsent(letter, digit);
            }

        }
        System.out.println(map);
    }

    private static final double MS = 1000;

    private void acceptStatistics(String str) {

        String[] split = str.trim().split("\\s+");
        System.out.println(Arrays.toString(split));
        StringBuilder b = new StringBuilder();
        double runtime = Double.parseDouble(split[2]) / MS;
        b.append(String.format("d TIME %.3f%n", runtime));
        b.append("d NBSOLS ").append(split[0]).append("\n");

        System.out.println(b.toString());
    }

    private void acceptOther(String str) {
        System.out.println(str.trim());
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

public final class CryptExec {

    private static final String QUIT = "quit\n";

    private static final byte[] QBYTES = QUIT.getBytes();

    final ProcessBuilder processBuilder;

    public CryptExec(String command) {
        super();
        this.processBuilder = new ProcessBuilder(command);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE);

    }

    public void exec(String filename, Consumer<String> consumer) throws IOException, InterruptedException {
        try (DataInputStream din = new DataInputStream(new FileInputStream(filename))) {
            byte[] bytes = new byte[din.available()];
            din.readFully(bytes);
            exec(bytes, consumer);
        }
    }

    public void exec(byte[] bytes, Consumer<String> consumer) throws IOException, InterruptedException {
        final Process proc = processBuilder.start();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        // Pipe input to the command
        OutputStream stdin = proc.getOutputStream();
        stdin.write(bytes);
        stdin.write(QBYTES);
        stdin.flush();
        // Read the command output
        reader.lines().forEach(consumer);
        // Wait for, and then destroy the process
        proc.waitFor();
        proc.destroy();
    }

}
