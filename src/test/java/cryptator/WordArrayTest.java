/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import cryptator.cmd.WordArray;

public class WordArrayTest {

    private final String[] words = {"a", "bb", "ccc"};

    @Test
    public void testWordArray1() {
        final WordArray w = new WordArray(Arrays.asList(words), null);
        assertArrayEquals(words, w.getWords());
        assertFalse(w.hasRightMember());
        assertTrue(w.getLB() < 0);
        assertTrue(w.getUB() < 0);
    }

    @Test
    public void testWordArray2() {
        final WordArray w = new WordArray(Arrays.asList(words), "dddd");
        assertEquals(4, w.getWords().length);
        assertTrue(w.hasRightMember());
        assertTrue(w.getLB() < 0);
        assertTrue(w.getUB() < 0);
    }

    @Test
    public void testWordArray3() {
        final WordArray w = new WordArray("FR", "fr", 1, 3);
        assertArrayEquals(new String[] {"zero", "un", "deux", "trois"}, w.getWords());
        assertFalse(w.hasRightMember());
        assertEquals(1, w.getLB());
        assertEquals(3, w.getUB());
    }
}
