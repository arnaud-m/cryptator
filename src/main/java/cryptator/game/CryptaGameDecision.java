/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.game;

import java.util.Scanner;

import cryptator.CryptaOperator;

public final class CryptaGameDecision {

	public final char symbol;

	public final CryptaOperator operator;
	
	public final int value;

	public CryptaGameDecision(char symbol, CryptaOperator operator, int value) {
		super();
		this.symbol = symbol;
		this.operator = operator;
		this.value = value;
	}

	public final char getSymbol() {
		return symbol;
	}

	public final CryptaOperator getOperator() {
		return operator;
	}

	public final int getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return symbol + " " + operator.getToken() + " " + value;
	}
	
	public static final CryptaGameDecision parseDecision(String decision) {
		return parseDecision(new Scanner(decision));
	}
	
	public static final CryptaGameDecision parseDecision(Scanner s) {
		if(s.hasNext()) {
			final String symbol = s.next();
			if(symbol.length() == 1 && s.hasNext()) {
				CryptaOperator operator;
				try {
					operator = CryptaOperator.valueOfToken(s.next());
					if(s.hasNextInt()) {
						return new CryptaGameDecision(symbol.charAt(0), operator, s.nextInt());
					}
				} catch (IllegalArgumentException e) {
					// Do nothing
				}	
			}
		}
		return null;
	}
}
