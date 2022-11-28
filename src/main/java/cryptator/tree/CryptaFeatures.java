/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.tree;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import cryptator.CryptaOperator;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ITraversalNodeConsumer;

public class CryptaFeatures implements ITraversalNodeConsumer {

    private int constantCount;
    private int wordCount;
    private int charCount;
    private int minWordLength = Integer.MAX_VALUE;
    private int maxWordLength;
    private Set<Character> symbols = new TreeSet<>();
    private Set<CryptaOperator> operators = new TreeSet<>();

    public CryptaFeatures() {
        super();
    }

    private void accept(final char[] word) {
        final int n = word.length;
        wordCount++;
        charCount += n;
        if (n < minWordLength) {
            minWordLength = n;
        } else if (n > maxWordLength) {
            maxWordLength = n;
        }
        for (char c : word) {
            symbols.add(c);
        }
    }

    @Override
    public void accept(final ICryptaNode node, final int numNode) {
        if (node.isInternalNode()) {
            operators.add(node.getOperator());
        } else {
            if (node.isConstant()) {
                constantCount++;
            } else {
                accept(node.getWord());
            }
        }
    }

    public final int getWordCount() {
        return wordCount;
    }

    public final int getCharCount() {
        return charCount;
    }

    public int getConstantCount() {
        return constantCount;
    }

    public final int getMinWordLength() {
        return minWordLength;
    }

    public final int getMaxWordLength() {
        return maxWordLength;
    }

    public final Set<Character> getSymbols() {
        return Collections.unmodifiableSet(symbols);
    }

    public final Set<CryptaOperator> getOperators() {
        return Collections.unmodifiableSet(operators);
    }

    public final String generateFilename() {
        return String.format("N02%dL%02d-%s", wordCount, maxWordLength, buildSymbols());
    }

    private final String buildSymbols() {
        return symbols.stream().map(String::valueOf).collect(Collectors.joining());
    }

    private final String buildOperators() {
        return operators.stream().map(String::valueOf).collect(Collectors.joining(" ", "", ""));
    }

    public final String buildInstanceName() {
        return String.format("N%02dC%03d-L%02d-%02d-%s", wordCount, charCount, minWordLength, maxWordLength,
                buildSymbols());
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("c WORD_COUNT ").append(wordCount);
        b.append("\nc CHAR_COUNT ").append(charCount);
        b.append("\nc SYMBOL_COUNT ").append(symbols.size());
        b.append("\nc OPERATOR_COUNT ").append(operators.size());
        b.append("\nc MIN_WORD_LEN ").append(minWordLength);
        b.append("\nc MAX_WORD_LEN ").append(maxWordLength);
        b.append("\nc SYMBOLS ").append(buildSymbols());
        b.append("\nc OPERATORS ").append(buildOperators());
        return b.toString();
    }

}
