/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.BiConsumer;

import cryptator.specs.ICryptaSolution;

public abstract class AbstractCryptaSolution<E> implements ICryptaSolution {

    protected final Map<Character, E> symbolsToDigits;

    protected AbstractCryptaSolution(final Map<Character, E> symbolsToDigits) {
        super();
        this.symbolsToDigits = new TreeMap<>(symbolsToDigits);
    }

    @Override
    public final int size() {
        return symbolsToDigits.size();
    }

    public E getVar(final char symbol) {
        return symbolsToDigits.get(symbol);
    }

    protected abstract String getDomain(E variable);

    @Override
    public final boolean hasDomain(final char symbol) {
        return symbolsToDigits.containsKey(symbol);
    }

    @Override
    public final String getDomain(final char symbol) {
        final E v = symbolsToDigits.get(symbol);
        return v == null ? "?" : getDomain(v);
    }

    public void forEach(final BiConsumer<? super Character, ? super E> action) {
        symbolsToDigits.forEach(action);
    }

    @Override
    public String toString() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        for (Entry<Character, E> e : symbolsToDigits.entrySet()) {
            final String domain = getDomain(e.getValue());
            final int length = Math.max(1, domain.length());
            final String format = " %" + length + "s|";
            b1.append(String.format(format, e.getKey()));
            b2.append(String.format(format, domain));
        }
        return b1.toString() + "\n" + b2.toString();
    }

}
