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
import java.util.HashMap;

import cryptator.specs.ICryptaSolution;



public class CryptaSolution implements ICryptaSolution {


	public final HashMap<Character, Variable> symbolToDigit;


	public CryptaSolution(HashMap<Character, Variable> digitToValue) {
		super();
		this.symbolToDigit = digitToValue;
	}
	
	public final HashMap<Character, Variable> getDigitToValue() {
		return symbolToDigit;
	}

	@Override
	public boolean hasDigit(char symbol) {
		return symbolToDigit.get(symbol) != null;
//		for(Variable var: symbolToDigit){
//			if(var.getName().equals(String.valueOf(symbol))){
//				return true;
//			}
//		}
//		return false;
	}
	

	@Override
	public int getDigit(char symbol) throws CryptaSolutionException {
		int v=symbolToDigit.get(symbol).getValue();
////		for(Variable var: symbolToDigit) {
////			if(String.valueOf(symbol).equals(var.getName())) {
////				v = var.getValue();
////				break;
////			}
////		}
//		if(v == -1){
//			throw new CryptaSolutionException("cant find symbol: " + symbol);
//		}
		if(v!=-1){
			return v;
		}
		throw new CryptaSolutionException("cant find symbol: " + symbol);

	}


}
