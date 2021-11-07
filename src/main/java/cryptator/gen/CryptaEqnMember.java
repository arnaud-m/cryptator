/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

public class CryptaEqnMember {

	public final String[] strWords;
	
	public final BoolVar[] words;
	public final IntVar[] lengths;
	public final IntVar maxLength;
	public final IntVar wordCount;

	public CryptaEqnMember(Model m, String[] words, String prefix) {
		super();
		this.strWords = words;
		this.words = new BoolVar[words.length];
		for (int i = 0; i < words.length; i++) {
			this.words[i] = m.boolVar(prefix + words[i]);
		}	

		lengths = new IntVar[words.length];
		int maxLen = 0;
		for (int i = 0; i < words.length; i++) {
			if(maxLen < words[i].length()) maxLen = words[i].length();
			lengths[i] = this.words[i].mul(words[i].length()).intVar();
		}

		maxLength = m.intVar(prefix + "maxLen", 0, maxLen);
		m.max(maxLength, lengths).post();

		wordCount = m.intVar(prefix + "wordCount", 0, words.length);
		m.sum(this.words, "=", wordCount).post();

		//      IntVar[] lengthOccurences;
		//		lengthOccurences = m.intVarArray(prefix +"lenOccs", maxLen + 1, 0, words.length);
		//		int[] values = ArrayUtils.array(0, maxLen);
		//		m.globalCardinality(lengths, values, lengthOccurences, true).post();
	}

	public final BoolVar[] getWords() {
		return words;
	}

	public final IntVar[] getLengths() {
		return lengths;
	}

	public final IntVar getMaxLength() {
		return maxLength;
	}

	public final IntVar getWordCount() {
		return wordCount;
	}

//	public ICryptaNode recordMember(CryptaEqnMember member) {
//		ICryptaNode node = null;
//		for (int i = 0; i < words.length; i++) {
//			if(words[i].isInstantiatedTo(1)) {
//				final CryptaLeaf leaf = new CryptaLeaf(strWords[i]);
//				node = node == null ? leaf : new CryptaNode(CryptaOperator.ADD, node, leaf);
//			}
//		}
//		return node;
//	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < words.length; i++) {
			if(words[i].isInstantiatedTo(1)) b.append(strWords[i]).append(" + "); 
		}
		if(b.length() > 0) b.delete(b.length()-3, b.length());
		return b.toString();
	}



}