/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

public class CryptaEqnMember extends CryptaGenVariables {

	@Deprecated(forRemoval = true)
	public final String[] strWords;
	
	public final IntVar[] lengths;

	public CryptaEqnMember(Model m, String[] words, String prefix) {
		super(m, words, prefix, true);
		this.strWords = words;
		
		lengths = new IntVar[words.length];
		int maxLen = 0;
		for (int i = 0; i < words.length; i++) {
			if(maxLen < words[i].length()) maxLen = words[i].length();
			lengths[i] = this.words[i].mul(words[i].length()).intVar();
		}
		m.max(maxLength, lengths).post();
		
		postWordCountBoolConstraint();

	}


	public final IntVar[] getLengths() {
		return lengths;
	}
	
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