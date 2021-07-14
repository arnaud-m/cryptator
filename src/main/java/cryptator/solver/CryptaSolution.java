/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import java.util.HashMap;

import cryptator.specs.ICryptaSolution;

public class CryptaSolution implements ICryptaSolution {

	public final HashMap<Character, Integer> symbolToDigit;
	
	public CryptaSolution(HashMap<Character, Integer> digitToValue) {
		super();
		this.symbolToDigit = digitToValue;
	}
	
	public final HashMap<Character, Integer> getDigitToValue() {
		return symbolToDigit;
	}

	@Override
	public boolean hasDigit(char symbol) {
		return symbolToDigit.containsKey(Character.valueOf(symbol));
	}

	@Override
	public int getDigit(char symbol) {
		final Integer v = symbolToDigit.get(Character.valueOf(symbol));
		return v != null ? v : -1;
	}

	
}
