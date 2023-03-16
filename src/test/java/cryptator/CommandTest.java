/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

public class CommandTest {

    @Before
    public void configureLoggers() {
        JULogUtil.configureSilentLoggers();
    }

    @Test
    public void testCryptatorException1() throws FileNotFoundException {
        String[] args = {"-b", "N", "saturn+uranus=planets"};
        assertNotEquals(0, Cryptator.doMain(args));
    }

    @Test
    public void testCryptatorException2() throws FileNotFoundException {
        String[] args = {"-v", "SILENT", "more+more+veryverylongword=requirebignummodel", "send+more=money"};
        assertEquals(1, Cryptator.doMain(args));
    }

    @Test
    public void testCryptator() throws FileNotFoundException {
        String[] args = {"-b", "10", "-c", "--horner", "-a", "SCALAR", "-s", "1", "-t", "5", "-v", "SILENT",
                "--leading-zeros", "www+imac=crash "};
        assertEquals(0, Cryptator.doMain(args));
    }

    @Test
    public void testCryptagenException() throws FileNotFoundException {
        String[] args = {"-v", "FOO", "saturn+uranus=planets"};
        assertNotEquals(0, Cryptagen.doMain(args));
    }

    @Test
    public void testCryptagen() throws FileNotFoundException {
        String[] args = {"-v", "SILENT", "www", "imac", "crash"};
        assertEquals(0, Cryptagen.doMain(args));
    }

    @Test
    public void testCryptagenDoublyTrue() throws FileNotFoundException {
        String[] args = {"-v", "SILENT", "3", "4"};
        assertEquals(0, Cryptagen.doMain(args));
    }

}
