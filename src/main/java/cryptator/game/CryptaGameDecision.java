/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.game;

import java.util.Scanner;

import cryptator.CryptaOperator;

/**
 * The Class CryptaGameDecision represents the decision of a player for solving
 * a cryptarithm. It is inspired by the branching decision of the choco solver:
 * org.chocosolver.solver.search.strategy.decision.Decision.
 *
 */
public final class CryptaGameDecision {

    /** The symbol. */
    private final char symbol;

    /** The operator. */
    private final CryptaOperator operator;

    /** The value. */
    private final int value;

    /**
     * Instantiates a new game decision.
     *
     * @param symbol   the symbol
     * @param operator the operator
     * @param value    the value
     */
    public CryptaGameDecision(final char symbol, final CryptaOperator operator, final int value) {
        super();
        this.symbol = symbol;
        this.operator = operator;
        this.value = value;
    }

    /**
     * Gets the decision symbol.
     *
     * @return the symbol
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Gets the decision relational arithmetic operator.
     *
     * @return the operator
     */
    public CryptaOperator getOperator() {
        return operator;
    }

    /**
     * Gets the decision value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return symbol + " " + operator.getToken() + " " + value;
    }

    /**
     * Parses the decision.
     *
     * @param decision the decision
     * @return the crypta game decision
     */
    public static CryptaGameDecision parseDecision(final String decision) {
        return parseDecision(new Scanner(decision));
    }

    /**
     * Parses the decision from a scanner.
     *
     * @param s the scanner to be parsed
     * @return the parsed game decision
     */
    public static CryptaGameDecision parseDecision(final Scanner s) {
        if (s.hasNext()) {
            final String symbol = s.next();
            if ((symbol.length() == 1) && s.hasNext()) {
                CryptaOperator operator;
                try {
                    operator = CryptaOperator.valueOfToken(s.next());
                    if (s.hasNextInt()) {
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
