/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.tree;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import cryptator.CryptaOperator;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ITraversalNodeConsumer;

public class CryptaFeatures implements ITraversalNodeConsumer {

	private int wordCount;
	
	private int charCount;
	
	private int minWordLength = Integer.MAX_VALUE;
	
	private int maxWordLength;
	
	private Set<Character> symbols = new HashSet<>();
	
	private Set<CryptaOperator> operators = new HashSet<>();
	
	public CryptaFeatures() {
		super();
	}
	
	@Override
	public void accept(ICryptaNode node, int numNode) {
		if(node.isLeaf()) {
			final char[] word = node.getWord();
			final int n = word.length;
			if(n > 0) {
				wordCount++;
				charCount += n;
				if(n < minWordLength) minWordLength = n;
				else if(n > maxWordLength) maxWordLength = n;
				for (char c : word) {
					symbols.add(c);
				}
			}
		} else {
			operators.add(node.getOperator());
		}
	}

	public final int getWordCount() {
		return wordCount;
	}

	public final int getCharCount() {
		return charCount;
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
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("c WORD_COUNT ").append(wordCount);
		b.append("\nc CHAR_COUNT ").append(charCount);
		b.append("\nc SYMBOL_COUNT ").append(symbols.size());
		b.append("\nc OPERATOR_COUNT ").append(operators.size());
		b.append("\nc MIN_WORD_LEN ").append(minWordLength);
		b.append("\nc MAX_WORD_LEN ").append(maxWordLength);
		b.append("\nc SYMBOLS ").append(symbols.stream().map(String::valueOf).collect(Collectors.joining()));
		b.append("\nc OPERATORS ").append(operators.stream().map(String::valueOf).collect(Collectors.joining(" ", "", "")));	
		return b.toString();
	}

}
