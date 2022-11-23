/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cryptator.config.CryptatorConfig;
import cryptator.json.SolveInput;
import cryptator.json.SolveOutput;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolverException;

public class JsonTest {

    private final ObjectMapper mapper = new ObjectMapper();

    private final PrintStream jsonOutput = System.out;

    private final CryptatorConfig config = new CryptatorConfig();

    @BeforeClass
    public static void configureTestLoggers() {
        JULogUtil.configureJsonLoggers();
    }

    @Before
    public void configure() {
        config.setSolutionLimit(10);
    }

    private InputStream buildJsonInput(Object object) throws StreamWriteException, DatabindException, IOException {
        // Export json
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        mapper.writeValue(out, object);
        // Print json
        jsonOutput.write(out.toByteArray());
        jsonOutput.print('\n');
        // Return json input stream
        return new ByteArrayInputStream(out.toByteArray());
    }

    public InputStream solve(String cryptarithm)
            throws StreamReadException, DatabindException, IOException, CryptaModelException, CryptaSolverException {
        // Create config
        SolveInput input = new SolveInput(cryptarithm, config);
        // Export to json
        final InputStream inputStream = buildJsonInput(input);
        // Read json config
        input = mapper.readValue(inputStream, SolveInput.class);
        // Solve the cryptarithm
        final SolveOutput output = CryptaJson.solve(input);
        // Return input json stream of solutions
        return buildJsonInput(output);
    }

    @Test
    public void testSendMoreMoney()
            throws StreamReadException, DatabindException, IOException, CryptaModelException, CryptaSolverException {
        assertNotNull(solve("send+more=money"));
    }

    @Test
    public void testBigCatLion()
            throws StreamReadException, DatabindException, IOException, CryptaModelException, CryptaSolverException {
        assertNotNull(solve("big + cat = lion"));
    }

    @Test
    public void testBigCatLionFrontEnd() throws CryptaModelException, CryptaSolverException, IOException {
        final String jsonInput = "{\"cryptarithm\":\"big+cat=lion\",\"config\":{\"arithmeticBase\":10,"
                + "\"allowLeadingZeros\":false," + " \"solutionLimit\":5," + "\"timeLimit\":0}}";
        jsonOutput.println(jsonInput);
        // Read json config
        final SolveInput input = mapper.readValue(jsonInput, SolveInput.class);
        // Solve the cryptarithm
        final SolveOutput output = CryptaJson.solve(input);
        // Return input json stream of solutions
        assertNotNull(buildJsonInput(output));
    }

}
