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
import org.chocosolver.solver.variables.IntVar;

import cryptator.CryptaOperator;
import cryptator.specs.ICryptaNode;
import cryptator.tree.CryptaLeaf;
import cryptator.tree.CryptaNode;

public class CryptaEqnMember extends CryptaGenVariables {

	public final IntVar[] lengths;

	public CryptaEqnMember(Model m, String[] words, String prefix) {
		super(m, words, prefix, true);
		lengths = m.intVarArray(prefix + "len", words.length, 0, getMaxLength(words));
	}

	@Override
	protected void postMaxLengthConstraints() {
		for (int i = 0; i < words.length; i++) {	
			lengths[i].eq(this.vwords[i].mul(words[i].length())).post();
		}
		model.max(maxLength, lengths).post();
	}

	private ICryptaNode recordMember() {
		ICryptaNode node = null;
		for (int i = 0; i < vwords.length; i++) {
			if(vwords[i].isInstantiatedTo(1)) {
				final CryptaLeaf leaf = new CryptaLeaf(words[i]);
				node = node == null ? leaf : new CryptaNode(CryptaOperator.ADD, node, leaf);
			}
		}
		return node;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < vwords.length; i++) {
			if(vwords[i].isInstantiatedTo(1)) b.append(words[i]).append(" + "); 
		}
		if(b.length() > 0) b.delete(b.length()-3, b.length());
		return b.toString();
	}

	


}