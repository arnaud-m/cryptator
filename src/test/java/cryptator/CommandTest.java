/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.FileNotFoundException;

import org.junit.Test;

public class CommandTest {

    @Test
    public void testCryptatorException() throws FileNotFoundException {
        String[] args = {"-b", "N", "saturn+uranus=planets"};
        assertNotEquals(0, Cryptator.doMain(args));
    }

    @Test
    public void testCryptator() throws FileNotFoundException {
        String[] args = {"-b", "10", "-c", "TRUE", "-g", "FALSE", "-h", "TRUE", "-l", "FALSE", "-max", "0", "-min", "0",
                "-s", "1", "-t", "5", "-v", "FALSE", "-z", "FALSE", "www+imac=crash "};
        assertEquals(0, Cryptator.doMain(args));
    }

    @Test
    public void testCryptagenException() throws FileNotFoundException {
        String[] args = {"-v", "FOO", "saturn+uranus=planets"};
        assertNotEquals(0, Cryptagen.doMain(args));
    }

    @Test
    public void testCryptagen() throws FileNotFoundException {
        String[] args = {"-c", "FALSE", "-g", "FALSE", "-v", "FALSE", "www", "imac", "crash"};
        assertEquals(0, Cryptagen.doMain(args));
    }

    @Test
    public void testCryptagenDoublyTrue() throws FileNotFoundException {
        String[] args = {"-c", "FALSE", "-g", "FALSE", "-v", "FALSE", "3", "4"};
        assertEquals(0, Cryptagen.doMain(args));
    }

}
