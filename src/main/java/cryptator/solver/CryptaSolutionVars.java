/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import java.util.HashMap;
import java.util.Map;

import org.chocosolver.solver.variables.IntVar;

import cryptator.specs.ICryptaSolution;

public class CryptaSolutionVars extends AbstractCryptaSolution<IntVar> {

    public CryptaSolutionVars(final Map<Character, IntVar> symbolsToDigits) {
        super(symbolsToDigits);
    }

    @Override
    public boolean hasDigit(final char symbol) {
        final IntVar v = symbolsToDigits.get(symbol);
        return (v != null) && v.isInstantiated();
    }

    @Override
    public int getDigit(final char symbol) throws CryptaSolutionException {
        final IntVar v = symbolsToDigits.get(symbol);
        if ((v != null) && v.isInstantiated()) {
            return v.getValue();
        } else {
            throw new CryptaSolutionException("cant find symbol: " + symbol);
        }
    }

    @Override
    public int getDigit(final char symbol, final int defaultValue) {
        final IntVar v = symbolsToDigits.get(symbol);
        if ((v != null) && v.isInstantiated()) {
            return v.getValue();
        } else {
            return defaultValue;
        }
    }

    @Override
    protected final String getDomain(final IntVar v) {
        return v.toString().replaceFirst(".*=\\s*", "");
    }

    public ICryptaSolution recordSolution() {
        final Map<Character, Integer> symbolsToDigits = new HashMap<>();
        this.symbolsToDigits.forEach((symbol, variable) -> {
            if (variable.isInstantiated()) {
                symbolsToDigits.put(symbol, variable.getValue());
            }
        });
        return new CryptaSolutionMap(symbolsToDigits);
    }

    public boolean isTotalSolution() {
        for (IntVar variable : symbolsToDigits.values()) {
            if (!variable.isInstantiated()) {
                return false;
            }
        }
        return true;
    }

}
