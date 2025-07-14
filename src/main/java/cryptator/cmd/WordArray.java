/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.cmd;

import static cryptator.gen.TransformWord.translateAndNormalize;

import java.util.Arrays;
import java.util.List;

public class WordArray {

    private final String[] words;

    private final int lb;

    private final int ub;

    public WordArray(final String... words) {
        lb = -1;
        ub = -1;
        this.words = words;
    }

    public WordArray(final List<String> words) {
        super();
        lb = -1;
        ub = -1;
        this.words = new String[words.size()];
        words.toArray(this.words);
    }

    public WordArray(final String countryCode, final String lang, final int lb, final int ub) {
        super();
        this.lb = lb;
        this.ub = ub;
        this.words = new String[ub + 1];
        for (int i = 0; i <= ub; i++) {
            words[i] = translateAndNormalize(countryCode, lang, i);
        }
    }

    public final String[] getWords() {
        return words;
    }

    public boolean isDoublyTrue() {
        return ub >= 0;
    }

    public final int getLB() {
        return lb;
    }

    public final int getUB() {
        return ub;
    }

    public String toDimacs() {
        return "c WORDS " + words.length + "\nc DOUBLY_TRUE " + (isDoublyTrue() ? lb + "-" + ub : "NO");
    }

    @Override
    public String toString() {
        return "WordArray [words=" + Arrays.toString(words) + ", lb=" + lb + ", ub=" + ub + "]";
    }
}
