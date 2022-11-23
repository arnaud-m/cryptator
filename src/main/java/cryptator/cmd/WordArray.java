/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
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

    private String rightMember;

    private final int lb;

    private final int ub;

    public WordArray(List<String> words, String rightMember) {
        super();
        final int n = words.size();
        this.rightMember = rightMember;
        if (rightMember == null) {
            this.words = new String[n];
            words.toArray(this.words);
        } else {
            this.words = new String[n + 1];
            words.toArray(this.words);
            this.words[n] = rightMember;
        }
        lb = -1;
        ub = -1;
    }

    public WordArray(String countryCode, String lang, int lb, int ub) {
        super();
        this.lb = lb;
        this.ub = ub;
        this.words = new String[ub + 1];
        for (int i = 0; i <= ub; i++) {
            words[i] = translateAndNormalize(countryCode, lang, i);
        }
        this.rightMember = null;
    }

    public final String[] getWords() {
        return words;
    }

    public boolean hasRightMember() {
        return rightMember != null;
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

    @Override
    public String toString() {
        return "WordArray [words=" + Arrays.toString(words) + ", rightMember=" + rightMember + ", lb=" + lb + ", ub="
                + ub + "]";
    }
}
