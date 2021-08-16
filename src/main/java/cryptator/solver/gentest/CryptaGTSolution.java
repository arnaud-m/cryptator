/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver.gentest;

import java.util.HashMap;

import cryptator.solver.CryptaSolutionException;
import cryptator.specs.ICryptaSolution;



public class CryptaGTSolution implements ICryptaSolution {


	public final HashMap<Character, GTVariable> symbolToDigit;


	public CryptaGTSolution(HashMap<Character, GTVariable> digitToValue) {
		super();
		this.symbolToDigit = digitToValue;
	}

	@Override
	public int size() {
		return symbolToDigit.size();
	}


	public final HashMap<Character, GTVariable> getDigitToValue() {
		return symbolToDigit;
	}

	@Override
	public boolean hasDigit(char symbol) {
		return symbolToDigit.get(symbol) != null;
	}


	@Override
	public int getDigit(char symbol) throws CryptaSolutionException {
		int v=symbolToDigit.get(symbol).getValue();
		if(v!=-1){
			return v;
		}
		// FIXME Throws an exception while hasDigit returns true !
		throw new CryptaSolutionException("cant find symbol: " + symbol);

	}

	@Override
	public int getDigit(char symbol, int defaultValue) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasDomain(char symbol) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getDomain(char symbol) {
		// TODO Auto-generated method stub
		return null;
	}


	
}
