/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.chocosolver.solver.variables.IntVar;

import cryptator.specs.ICryptaSolution;

public abstract class AbstractCryptaSolution<E> implements ICryptaSolution {

	protected final Map<Character, E> symbolsToDigits;

	public AbstractCryptaSolution(Map<Character, E> symbolsToDigits) {
		super();
		this.symbolsToDigits = symbolsToDigits;
	}

	@Override
	public final int size() {
		return symbolsToDigits.size();
	}

	protected abstract String getDomain(E var);

	@Override
	public final boolean hasDomain(char symbol) {
		return symbolsToDigits.containsKey(symbol);
	}

	@Override
	public final String getDomain(char symbol) {
		final E v = symbolsToDigits.get(symbol);
		return v == null ? "?" : getDomain(v);
	}

	@Override
	public String toString() {
		final StringBuilder b1 = new StringBuilder();
		final StringBuilder b2 = new StringBuilder();
		ArrayList<Character> sortedKeys = new ArrayList<Character>(symbolsToDigits.keySet());
		Collections.sort(sortedKeys);		
		for (Character symbol : sortedKeys) {
			final String domain = getDomain(symbolsToDigits.get(symbol));
			final int length = Math.max(1, domain.length());
			final String format = " %" + length + "s|";
			b1.append(String.format(format, symbol));
			b2.append(String.format(format, domain));
		}		
		return b1.toString() + "\n" + b2.toString();
	}

}
