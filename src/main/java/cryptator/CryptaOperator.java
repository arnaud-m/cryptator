/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import org.chocosolver.solver.expression.discrete.arithmetic.ArExpression;
import org.chocosolver.solver.expression.discrete.relational.ReExpression;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.VariableUtils;

import java.math.BigInteger;
import java.util.function.BinaryOperator;

/**
 * @see https://en.wikipedia.org/wiki/Relational_operator
 */
public enum CryptaOperator {
    ADD("+", 2, true, (a, b) -> a.add(b), (a, b) -> a.add(b)),
    SUB("-", 2, false, (a, b) -> a.subtract(b), (a, b) -> a.sub(b)),
    MUL("*", 3, true, (a, b) -> a.multiply(b), (a, b) -> a.mul(b)),
    DIV("//", 3, false, (a, b) -> a.divide(b), (a, b) -> a.div(b)),
    FDIV("/", 3, false, (a, b) -> fdiv(a, b), (a, b) -> fdiv(a, b)),
    MOD("%", 4, false, (a, b) -> a.mod(b), (a, b) -> a.mod(b)),
    POW("^", 4,  false, (a, b) -> a.pow(b.intValue()), (a, b) -> a.pow(b)),
    ID("", 0, true, (a, b) -> BigInteger.ZERO, (a, b) -> null),
    EQ("=", 1, true, (a, b) -> toBigInt(a.compareTo(b) == 0), (a, b) -> a.eq(b)),
    NE("!=", 1, true, (a, b) -> toBigInt(a.compareTo(b) != 0), (a, b) -> a.ne(b)),
    LT("<", 1, false, (a, b) -> toBigInt(a.compareTo(b) < 0), (a, b) -> a.lt(b)),
    GT(">", 1, false, (a, b) -> toBigInt(a.compareTo(b) > 0), (a, b) -> a.gt(b)),
    LE("<=", 1, false, (a, b) -> toBigInt(a.compareTo(b) <= 0), (a, b) -> a.le(b)),
    GE(">=", 1, false, (a, b) -> toBigInt(a.compareTo(b) >= 0), (a, b) -> a.ge(b)),

    AND("&&", 0, true, (a, b) -> toBigInt(!a.equals(BigInteger.ZERO) && !b.equals(BigInteger.ZERO)),
            (a, b) -> ((ReExpression) a).and((ReExpression) b));

    private final String token;

    private final BinaryOperator<BigInteger> function;

    private final BinaryOperator<ArExpression> expression;

    private final int priority;
    private final boolean isCommutative;

    CryptaOperator(final String token, int priority, boolean isCommutative, final BinaryOperator<BigInteger> function,
            final BinaryOperator<ArExpression> expression) {
        this.token = token;
        this.function = function;
        this.expression = expression;
        this.priority = priority;
        this.isCommutative = isCommutative;
    }

    public static CryptaOperator valueOfToken(final String token) {
        if (token == null) {
            return null;
        }
        for (CryptaOperator operator : CryptaOperator.values()) {
            if (token.equals(operator.getToken())) {
                return operator;
            }
        }
        throw new IllegalArgumentException("Unknown token: " + token);
    }

    private static BigInteger toBigInt(final boolean b) {
        return b ? BigInteger.ONE : BigInteger.ZERO;
    }

    private static BigInteger fdiv(final BigInteger a, final BigInteger b) {
        final BigInteger[] r = a.divideAndRemainder(b);
        if (r[1].equals(BigInteger.ZERO)) {
            return r[0];
        } else {
            throw new ArithmeticException("The remainder of the division is non-zero.");
        }
    }

    private static ArExpression fdiv(final ArExpression a, final ArExpression b) {
        final IntVar va = a.intVar();
        final IntVar vb = b.intVar();
        final int[] bounds = VariableUtils.boundsForDivision(va, vb);
        final IntVar q = a.getModel().intVar(bounds[0], bounds[1]);
        // Post auxiliary constraint to emulate floor division.
        q.mul(b).eq(a).post();
        return q;
    }

    public String getToken() {
        return token;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isCommutative() {
        return isCommutative;
    }

    public BinaryOperator<BigInteger> getFunction() {
        return function;
    }

    public BinaryOperator<ArExpression> getExpression() {
        return expression;
    }
}
