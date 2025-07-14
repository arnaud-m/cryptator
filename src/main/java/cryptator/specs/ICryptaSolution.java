/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.specs;

import cryptator.solver.CryptaSolutionException;

/**
 * The Interface ICryptaSolution defines a solution of a cryptarithm. This is a
 * mapping of a set of symbols to digits.
 */
public interface ICryptaSolution {

    /**
     * The number of symbols in the cryptarithm.
     *
     * @return the number of symbols
     */
    int size();

    /**
     * Checks if there is a digit associated to the symbol.
     *
     * @param symbol any symbol
     * @return true, if the symbol appears in the cryptarithm and there is a digit
     *         associated to it.
     */
    boolean hasDigit(char symbol);

    /**
     * Gets the digit associated to a symbol.
     *
     * @param symbol any symbol
     * @return the digit associated to the symbol
     * @throws CryptaSolutionException if no digit is associated to the symbol.
     */
    int getDigit(char symbol) throws CryptaSolutionException;

    /**
     * Gets the digit associated to a symbol.
     *
     * @param symbol       any symbol
     * @param defaultValue the default value
     * @return the digit if any associated to the symbol or the default value
     */
    int getDigit(char symbol, int defaultValue);

    /**
     * Checks if the digit has a domain (set of possible values).
     *
     * @param symbol any symbol
     * @return true, if it has a domain
     */
    boolean hasDomain(char symbol);

    /**
     * Gets a string representation of the domain of a symbol.
     *
     * @param symbol any symbol
     * @return the domain no matter the symbol appears in the cryptarithm.
     */
    String getDomain(char symbol);

}
