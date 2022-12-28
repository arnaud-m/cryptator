/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
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

    @Option(name = "-b", usage = "Base (or radix) of the positional numeral system used for the cryptarithm (> 1)")
    private int arithmeticBase = BigInteger.TEN.intValue();

    @Option(name = "-z", handler = ExplicitBooleanOptionHandler.class, usage = "allow leading zeros in the cryptarithm solution")
    private boolean allowLeadingZeros;

    @Option(name = "-h", handler = ExplicitBooleanOptionHandler.class, usage = "use the horner scheme to model the numbers repsented by the cryptarithm words")
    private boolean hornerScheme;

    @Option(name = "-min", usage = "relaxation of the minimum number of occurences of a digit (>=0)")
    private int relaxMinDigitOccurence = 0;

    @Option(name = "-max", usage = "relaxation of the maximum number of occurences of a digit (>=0)")
    private int relaxMaxDigitOccurence = 0;

    @Option(name = "-search", usage = "identifier of the search strategy")
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

    public final int getRelaxMinDigitOccurence() {
        return relaxMinDigitOccurence;
    }

    public final void setRelaxMinDigitOccurence(final int relaxMinDigitOccurence) {
        this.relaxMinDigitOccurence = relaxMinDigitOccurence;
    }

    public final int getRelaxMaxDigitOccurence() {
        return relaxMaxDigitOccurence;
    }

    public final void setRelaxMaxDigitOccurence(final int relaxMaxDigitOccurence) {
        this.relaxMaxDigitOccurence = relaxMaxDigitOccurence;
    }

    public final int getSearchStrategy() {
        return searchStrategy;
    }

    public final void setSearchStrategy(final int searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public final int getMinDigitOccurence(final int n) {
        int minOcc = n / getArithmeticBase();
        final int deltaMin = getRelaxMinDigitOccurence();
        if (deltaMin > 0) {
            minOcc = Math.max(0, minOcc - deltaMin);
        }
        return minOcc;
    }

    public final int getMaxDigitOccurence(final int n) {
        int maxOcc = ((n + getArithmeticBase()) - 1) / getArithmeticBase();
        final int deltaMax = getRelaxMaxDigitOccurence();
        if (deltaMax > 0) {
            maxOcc = Math.min(n, maxOcc + deltaMax);
        }
        return maxOcc;
    }

    public final List<String> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    @Override
    public String toString() {
        return "c BASE " + arithmeticBase + "\nc ALLOW_LEADING_0 " + allowLeadingZeros + "\nc HORNER_SCHEME "
                + hornerScheme + "\nc RELAX_MIN_DIGIT_OCC " + relaxMinDigitOccurence + "\nc RELAX_MAX_DIGIT_OCC "
                + relaxMaxDigitOccurence;
    }

}
