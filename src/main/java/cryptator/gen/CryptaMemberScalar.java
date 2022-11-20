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

public class CryptaMemberScalar extends CryptaGenBaseModel {

	public final IntVar[] lengths;

	public CryptaMemberScalar(Model m, String[] words, String prefix) {
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

}