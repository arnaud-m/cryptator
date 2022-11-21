/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class CryptaMemberCard extends CryptaMemberReif {
	
	protected final IntVar[] cardLengths;
	
	public CryptaMemberCard(Model m, String[] words, String prefix) {
		super(m, words, prefix);
		cardLengths = m.intVarArray(prefix + "cardLen", getMaxLength(words) + 1, 0, words.length);
	}

	public final IntVar[] getCardLength() {
		return cardLengths;
	}
	
	
	@Override
	protected void postWordCountConstraint() {
		super.postWordCountConstraint();
		// Remove card of empty/unused words
		IntVar[] vars = Arrays.copyOfRange(cardLengths, 1, cardLengths.length);
		model.sum(vars, "=", wordCount).post();
	}
	
	private void postGlobalCardLengthConstraint() {
		int[] values = IntStream.range(0, cardLengths.length).toArray();
		model.globalCardinality(lengths, values, cardLengths, true).post();
	}

	@Override
	public void buildModel() {
		super.buildModel();
		postGlobalCardLengthConstraint();
	}
	

	
	
}