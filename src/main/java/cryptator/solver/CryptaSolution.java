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

import cryptator.specs.ICryptaSolution;



public class CryptaSolution implements ICryptaSolution {
	
	// TODO Margaux: Tu devais créer une AUTRE classe, et pas supprimer celle utilisant la map.
	// De plus, tu devrais quand même utiliser une map ! Utiliser la généricité ?
	public final ArrayList<Variable> symbolToDigit;
	
	public CryptaSolution(ArrayList<Variable> digitToValue) {
		super();
		this.symbolToDigit = digitToValue;
	}
	
	public final ArrayList<Variable> getDigitToValue() {
		return symbolToDigit;
	}

	@Override
	public boolean hasDigit(char symbol) {
		for(Variable var: symbolToDigit){
			if(var.getName().equals(String.valueOf(symbol))){
				return true;
			}
		}
		return false;
	}

	@Override
	public int getDigit(char symbol) throws Exception {
		int v = -1;
		for(Variable var: symbolToDigit) {
			if(String.valueOf(symbol).equals(var.getName())) {
				v = var.getValue();
				break;
			}
		}
		if(v == -1){
			// FIXME Always specialize exception. 
			throw new Exception("Unrecognized symbol: " + symbol);
		}
		return v;
	}


}
