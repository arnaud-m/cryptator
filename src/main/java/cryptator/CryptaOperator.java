/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.math.BigInteger;
import java.util.function.BinaryOperator;

import org.chocosolver.solver.expression.discrete.arithmetic.ArExpression;

public enum CryptaOperator {
	ADD("+", (a, b) -> a.add(b), (a, b) -> a.add(b)),
	SUB("-", (a, b) -> a.subtract(b), (a, b) -> a.sub(b)), 
	MUL("*", (a, b) -> a.multiply(b), (a, b) -> a.mul(b)), 
	DIV("/", (a, b) -> a.divide(b), (a, b) -> a.div(b)), 
	MOD("%", (a, b) -> a.mod(b), (a, b) -> a.mod(b)), 
	POW("^", (a, b) -> a.pow(b.intValue()), (a, b) -> a.pow(b)), 
	ID("", (a, b) -> BigInteger.ZERO, (a, b) -> null),
	EQ("=", (a, b) -> toBigInt(a.compareTo(b) == 0), (a, b) -> a.eq(b)), 
	NE("!=", (a, b) -> toBigInt(a.compareTo(b) != 0), (a, b) -> a.ne(b)), 
	LT("<", (a, b) -> toBigInt(a.compareTo(b) < 0), (a, b) -> a.lt(b)), 
	GT(">", (a, b) -> toBigInt(a.compareTo(b) > 0), (a, b) -> a.gt(b)), 
	LE("<=", (a, b) -> toBigInt(a.compareTo(b) <= 0), (a, b) -> a.le(b)), 
	GE(">=", (a, b) -> toBigInt(a.compareTo(b) >= 0), (a, b) -> a.ge(b));

	public final String token;

	public final BinaryOperator<BigInteger> function;

	public final BinaryOperator<ArExpression> expression;

	private CryptaOperator(String token, BinaryOperator<BigInteger> function, BinaryOperator<ArExpression> expression) {
		this.token = token;
		this.function = function;
		this.expression = expression;
	}

	public final String getToken() {
		return token;
	}

	public final BinaryOperator<BigInteger> getFunction() {
		return function;
	}

	public final BinaryOperator<ArExpression> getExpression() {
		return expression;
	}

	public static CryptaOperator valueOfToken(String token) {
		if(token == null) return null;
		for(CryptaOperator operator : CryptaOperator.values()) {
			if(token.equals(operator.getToken())) return operator;
		}
		throw new IllegalArgumentException("Unknown token: " + token);
	}
	
	private static final BigInteger toBigInt(boolean b) {
		return b ? BigInteger.ONE : BigInteger.ZERO;
	}

}
