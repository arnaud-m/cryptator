/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import java.util.HashMap;
import java.util.Map;

import cryptator.specs.ICryptaSolution;

public class CryptaSolutionMap extends AbstractCryptaSolution<Integer> {

    protected CryptaSolutionMap(final Map<Character, Integer> symbolsToDigits) {
        super(symbolsToDigits);
    }

    public static final ICryptaSolution parseSolution(final String solution) throws CryptaSolutionException {
        final HashMap<Character, Integer> symbolToDigit = new HashMap<>();
        final String[] split = solution.split("\\s*[\\s=]\\s*");
        if ((split.length % 2) != 0) {
            throw new CryptaSolutionException("Invalid number of splits: " + split.length);
        }
        for (int i = 0; i < split.length; i += 2) {
            if (split[i].length() != 1) {
                throw new CryptaSolutionException("Invalid symbol: " + split[i]);
            }
            final char symbol = split[i].charAt(0);
            try {
                final int digit = Integer.parseInt(split[i + 1]);
                symbolToDigit.put(symbol, digit);
            } catch (NumberFormatException e) {
                throw new CryptaSolutionException("Invalid digit: " + split[i + 1]);
            }
        }
        return new CryptaSolutionMap(symbolToDigit);
    }

    @Override
    public boolean hasDigit(final char symbol) {
        return symbolsToDigits.containsKey(symbol);
    }

    @Override
    public int getDigit(final char symbol) throws CryptaSolutionException {
        final Integer v = symbolsToDigits.get(symbol);
        if (v == null) {
            throw new CryptaSolutionException("cant find symbol: " + symbol);
        } else {
            return v;
        }
    }

    @Override
    public int getDigit(final char symbol, final int defaultValue) {
        return symbolsToDigits.getOrDefault(symbol, defaultValue);
    }

    @Override
    protected String getDomain(final Integer v) {
        return v.toString();
    }

}
