/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

public enum CryptaOperator {

	ADD("+"), SUB("-"), 
	MUL("*"), DIV("/"), 
	MOD("%"), POW("^"), ID(""),
	EQ("=="), NEQ("!="), 
	LT("<"), GT(">"), 
	LEQ("<="), GEQ(">=");
	
	public final String token;

	private CryptaOperator(String token) {
		this.token = token;
	}

	public final String getToken() {
		return token;
	}
	
	public static CryptaOperator valueOfToken(String token) {
		if(token == null) return null;
		for(CryptaOperator operator : CryptaOperator.values()) {
			if(token.equals(operator.getToken())) return operator;
			CryptaOperator.valueOf("");
		}
		throw new IllegalArgumentException("Unknown token: " + token);
	}
	
}
