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
import java.util.function.Consumer;

/**
 * The Class CryptExec.
 */
public final class CryptExec {

    /** The Constant QUIT command for the crypt solver. */
    private static final String QUIT = "quit\n";

    /** The Constant bytes for QUIT. */
    private static final byte[] QBYTES = QUIT.getBytes();

    /** The process builder. */
    final ProcessBuilder processBuilder;

    /**
     * Instantiates a new crypt executor.
     *
     * @param command the crypt command
     */
    public CryptExec(String command) {
        super();
        this.processBuilder = new ProcessBuilder(command);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE);

    }

    /**
     * Feed the crypt solver with the input file.
     *
     * @param filename the filename with the solver input
     * @param consumer the consumer for the solver output
     * @throws IOException          Signals that an I/O exception has occurred.
     * @throws InterruptedException the interrupted exception
     */
    public void exec(String filename, Consumer<String> consumer) throws IOException, InterruptedException {
        try (DataInputStream din = new DataInputStream(new FileInputStream(filename))) {
            byte[] bytes = new byte[din.available()];
            din.readFully(bytes);
            exec(bytes, consumer);
        }
    }

    /**
     * Exececute the crypt solver for the input bytes.
     *
     * @param bytes    the input bytes
     * @param consumer the consumer for the solver output
     * @throws IOException          Signals that an I/O exception has occurred.
     * @throws InterruptedException the interrupted exception
     */
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
