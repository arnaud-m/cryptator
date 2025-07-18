/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
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
        String[] args = {"-v", "silent", "-b", "N", "saturn+uranus=planets"};
        assertNotEquals(0, Cryptator.doMain(args));
    }

    @Test
    public void testCryptatorException2() throws FileNotFoundException {
        String[] args = {"-v", "silent", "more+more+veryverylongword=requirebignummodel", "send+more=money"};
        assertEquals(1, Cryptator.doMain(args));
    }

    @Test
    public void testCryptatorException3() throws FileNotFoundException {
        String[] args = {"-v", "silent", "-b", "1", "saturn+uranus=planets"};
        assertNotEquals(1, Cryptator.doMain(args));
    }

    @Test
    public void testCryptator1() throws FileNotFoundException {
        String[] args = {"-b", "10", "--check", "--horner", "-s", "SCALAR", "--solution", "1", "--time", "5", "-v",
                "SILENT", "--zeros", "www+imac=crash "};
        assertEquals(0, Cryptator.doMain(args));
    }

    @Test
    public void testCryptator2() throws FileNotFoundException {
        String[] args = {"-v", "silent", "--check", "-s", "bignum", "--solution", "1", "--time", "5",
                "marcussacapus + rictus + satiric = marcuscubitus"};
        assertEquals(0, Cryptator.doMain(args));
    }

    @Test
    public void testCryptator3() throws FileNotFoundException {
        String[] args = {"-v", "silent", "--assign", "a=1", "--assign", "e=3", "alpos + les + otos = talos"};
        assertEquals(0, Cryptator.doMain(args));
    }

    @Test
    public void testCryptator4() throws FileNotFoundException {
        String[] args = {"-v", "silent", "--assign", "z=1", "alpos + les + otos = talos"};
        assertEquals(1, Cryptator.doMain(args));
    }

    @Test
    public void testCryptagenException() throws FileNotFoundException {
        String[] args = {"-v", "FOO", "saturn+uranus=planets"};
        assertNotEquals(0, Cryptagen.doMain(args));
    }

    @Test
    public void testCryptagen1() throws FileNotFoundException {
        String[] args = {"-v", "SILENT", "www", "imac", "crash"};
        assertEquals(0, Cryptagen.doMain(args));
    }

    @Test
    public void testCryptagen2() throws FileNotFoundException {
        String[] args = {"-v", "SILENT", "-s", "adapt", "obelix", "marcussacapus", "rictus", "satiric",
                "marcuscubitus"};
        assertEquals(0, Cryptagen.doMain(args));
    }

    @Test
    public void testCryptagenDoublyTrue() throws FileNotFoundException {
        String[] args = {"-v", "SILENT", "3", "4"};
        assertEquals(0, Cryptagen.doMain(args));
    }

}
