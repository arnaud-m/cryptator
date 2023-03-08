/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.config;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.ExplicitBooleanOptionHandler;

/**
 * A bean object that stores the common configuration. This is designed for
 * args4j command line, but it is not restricted to it.
 */
public class CryptaConfig {

    @Option(name = "-b", aliases = {"--base",
            "--radix"}, usage = "Base (or radix) of the positional numeral system (> 1).")
    private int arithmeticBase = BigInteger.TEN.intValue();

    @Option(name = "-z", aliases = {
            "--leading-zeros"}, hidden = true, handler = ExplicitBooleanOptionHandler.class, usage = "Allow leading zeros in the cryptarithm solution")
    private boolean allowLeadingZeros;

    @Option(name = "--horner", hidden = true, handler = ExplicitBooleanOptionHandler.class, usage = "Use the horner scheme to model     simple precision words.")
    private boolean hornerScheme;

    @Option(name = "-h", aliases = {"--help"}, usage = "Output a usage message and exit.")
    private boolean helpMessage;

    @Option(name = "-search", aliases = {"--search"}, hidden = true, usage = "Set the search strategy.")
    private int searchStrategy = 0;

    /**
     * Receives other command line parameters than options.
     */
    @Argument
    private List<String> arguments = new ArrayList<>();

    public final boolean getAllowLeadingZeros() {
        return allowLeadingZeros;
    }

    public final void setAllowLeadingZeros(final boolean allowLeadingZeros) {
        this.allowLeadingZeros = allowLeadingZeros;
    }

    public final int getArithmeticBase() {
        return arithmeticBase;
    }

    public final void setArithmeticBase(final int arithmeticBase) {
        this.arithmeticBase = arithmeticBase;
    }

    public final boolean getHornerScheme() {
        return hornerScheme;
    }

    public final void setHornerScheme(final boolean useHornerScheme) {
        this.hornerScheme = useHornerScheme;
    }

    public final int getSearchStrategy() {
        return searchStrategy;
    }

    public final void setSearchStrategy(final int searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public final boolean isHelpMessage() {
        return helpMessage;
    }

    public final int getMinDigitOccurence(final int n) {
        return n / getArithmeticBase();
    }

    public final int getMaxDigitOccurence(final int n) {
        return ((n + getArithmeticBase()) - 1) / getArithmeticBase();
    }

    public final List<String> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    @Override
    public String toString() {
        return "c BASE " + arithmeticBase + "\nc ALLOW_LEADING_0 " + allowLeadingZeros + "\nc HORNER_SCHEME "
                + hornerScheme;
    }

}
