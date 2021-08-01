/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.util.function.IntBinaryOperator;

public enum CryptaOperator {

	ADD("+", (a, b) -> a + b),
	SUB("-", (a, b) -> a - b), 
	MUL("*", (a, b) -> a * b), 
	DIV("/", (a, b) -> a / b), 
	MOD("%", (a, b) -> a % b), 
	POW("^", (a, b) -> a ^ b), 
	ID("", (a, b) -> 0),
	// TODO use == instead of = for equality ?
	EQ("=", (a, b) -> a == b ? 1 : 0), 
	NEQ("!=", (a, b) -> a != b ? 1 : 0), 
	LT("<", (a, b) -> a < b ? 1 : 0), 
	GT(">", (a, b) -> a > b ? 1 : 0), 
	LEQ("<=", (a, b) -> a <= b ? 1 : 0), 
	GEQ(">=", (a, b) -> a >= b ? 1 : 0);
	
	public final String token;
	
	public final IntBinaryOperator function;
	
	// TODO public final BinaryOperator<ArExpression> expression;
	// TODO ReExpression extends ArExpression
	// TODO public final BiFunction<ArExpression, ArExpression, ReExpression> constraint;

	private CryptaOperator(String token, IntBinaryOperator function) {
		this.token = token;
		this.function = function;
	}

	public final String getToken() {
		return token;
	}
	
	
	public final IntBinaryOperator getFunction() {
		return function;
	}

	public static CryptaOperator valueOfToken(String token) {
		if(token == null) return null;
		for(CryptaOperator operator : CryptaOperator.values()) {
			if(token.equals(operator.getToken())) return operator;
		}
		throw new IllegalArgumentException("Unknown token: " + token);
	}
	
}
