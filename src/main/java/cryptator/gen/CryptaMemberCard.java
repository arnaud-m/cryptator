/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen;

import java.util.Map;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

public class CryptaMemberCard extends CryptaMemberUse {
	
	protected final IntVar[] cardLength;
	
	public CryptaMemberCard(Model m, String[] words, String prefix) {
		super(m, words, prefix);
		cardLength = m.intVarArray(prefix + "cardLen", useLength.length, 0, words.length);
	}

	@Override
	public void postWordCountConstraint() {
		super.postWordCountConstraint();
		model.sum(cardLength, "=", wordCount).post();
	}
	
	@Override
	public void postUseLengthConstraints() {
		Map<Integer, BoolVar[]> map = buildWordsByLength();
		// Always use the empty word, but do not count it.
		useLength[0].eq(1).post();
		cardLength[0].eq(0).post();
		// Process other lengths > 0
		for (int i = 1; i < useLength.length; i++) {
			if(map.containsKey(i)) {				
				model.sum(map.get(i), "=", cardLength[i]).post();
				model.reifyXneC(cardLength[i], 0, useLength[i]);
			} else {
				useLength[i].eq(0).post();
				cardLength[i].eq(0).post();
			}
			
		}	
	}
	
}