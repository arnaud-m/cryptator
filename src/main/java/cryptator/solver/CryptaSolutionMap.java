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

public class CryptaSolutionMap implements ICryptaSolution {

	public final HashMap<Character, Integer> symbolToDigit;
	
	public final static ICryptaSolution parseSolution(String solution) throws CryptaSolutionException {
		final HashMap<Character, Integer> symbolToDigit = new HashMap<Character, Integer>();
		final String[] split = solution.split("\\s*[\\s=]\\s*");
		//System.out.println(Arrays.toString(split));
		if( split.length % 2 != 0) throw new CryptaSolutionException("Invalid number of splits: " + split.length);
		for (int i = 0; i < split.length; i+=2) {
			if( split[i].length() != 1) throw new CryptaSolutionException("Invalid symbol: " + split[i]);
			final char symbol = split[i].charAt(0);
			try {
				final int digit = Integer.parseInt(split[i+1]);
				symbolToDigit.put(symbol, digit);
			} catch (NumberFormatException e) {
				throw new CryptaSolutionException("Invalid digit: " + split[i+1]);
			}
		}
		return new CryptaSolutionMap(symbolToDigit);
	}
	
	public CryptaSolutionMap(HashMap<Character, Integer> symbolToDigit) {
		super();
		this.symbolToDigit = symbolToDigit;
	}
	
	public final HashMap<Character, Integer> getSymbolToDigit() {
		return symbolToDigit;
	}
	
	@Override
	public int size() {
		return symbolToDigit.size();
	}

	@Override
	public boolean hasDigit(char symbol) {
		return symbolToDigit.containsKey(Character.valueOf(symbol));
	}

	@Override
	public int getDigit(char symbol) throws CryptaSolutionException {
		final Integer v = symbolToDigit.get(Character.valueOf(symbol));
		if(v == null) throw new CryptaSolutionException("cant find symbol: " + symbol);
		else return v.intValue();
	}

	@Override
	public String toString() {
		return symbolToDigit.toString();
	}
	
	public static void main(String[] args) throws CryptaSolutionException {
		String solution = "A=  2 B    3 C   =4 D=5 E  =  6";
		ICryptaSolution s = CryptaSolutionMap.parseSolution(solution);
		System.out.println(s);
	}
}
