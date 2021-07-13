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

	ADD("+"), SUB("-"), MUL("*"), DIV("/"), POW("^"), ID(""),
	EQ("=="), NEQ("!="), LEQ("<="), GEQ(">=");
	
	public final String operator;

	private CryptaOperator(String operator) {
		this.operator = operator;
	}

	public final String getOperator() {
		return operator;
	}
	
	
	
}
