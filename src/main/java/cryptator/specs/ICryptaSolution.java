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

public interface ICryptaSolution {
	
	int size();
	
	boolean hasDigit(char symbol); 
	
	int getDigit(char symbol) throws CryptaSolutionException;
	
	int getDigit(char symbol, int defaultValue);
	
	boolean hasDomain(char symbol);
	
	String getDomain(char symbol);
	
}
