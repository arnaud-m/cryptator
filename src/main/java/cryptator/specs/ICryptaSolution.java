/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.specs;

import cryptator.solver.CryptaSolutionException;
import cryptator.solver.gentest.GTVariable;

import java.util.HashMap;
import java.util.Map;

public interface ICryptaSolution {
	
	int size();
	
	boolean hasDigit(char symbol); 
	
	int getDigit(char symbol) throws CryptaSolutionException;
	
	default int getDigit(char symbol, int defaultValue) {
		try {
			return getDigit(symbol);
		} catch (CryptaSolutionException e) {
			return defaultValue;
		}
	}

	Map<Character, ?> getSymbolToDigit();
}
