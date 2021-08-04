/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.ExplicitBooleanOptionHandler;


public class CryptaConfig {
	/**
	 * Base, or radix, of the positional Numeral System
	 */
	@Option(name="--b", usage="Base (or radix) of the positional numeral system used for the cryptarithm (> 1)")
	private int arithmeticBase = 10;

	@Option(name="-z",handler=ExplicitBooleanOptionHandler.class,usage="allow leading zeros in the cryptarithm solution")
	private boolean allowLeadingZeros;

	@Option(name="-h",handler=ExplicitBooleanOptionHandler.class,usage="use the horner scheme to model the numbers repsented by the cryptarithm words")
	private boolean hornerScheme;

	@Option(name="-min", usage="relaxation of the minimum number of occurences of a digit (>=0)")
	private int relaxMinDigitOccurence = 0;

	@Option(name="-max", usage="relaxation of the maximum number of occurences of a digit (>=0)")
	private int relaxMaxDigitOccurence = 0;

	// receives other command line parameters than options
	@Argument
	private List<String> arguments = new ArrayList<String>();

	public CryptaConfig() {}
	
	public final boolean allowLeadingZeros() {
		return allowLeadingZeros;
	}

	public final void allowLeadingZeros(boolean allowLeadingZeros) {
		this.allowLeadingZeros = allowLeadingZeros;
	}

	public final int getArithmeticBase() {
		return arithmeticBase;
	}

	public final void setArithmeticBase(int arithmeticBase) {
		this.arithmeticBase = arithmeticBase;
	}

	public final boolean useHornerScheme() {
		return hornerScheme;
	}

	public final void useHornerScheme(boolean used) {
		this.hornerScheme = used;
	}

	public final int getRelaxMinDigitOccurence() {
		return relaxMinDigitOccurence;
	}

	public final void setRelaxMinDigitOccurence(int relaxMinDigitOccurence) {
		this.relaxMinDigitOccurence = relaxMinDigitOccurence;
	}

	public final int getRelaxMaxDigitOccurence() {
		return relaxMaxDigitOccurence;
	}

	public final void setRelaxMaxDigitOccurence(int relaxMaxDigitOccurence) {
		this.relaxMaxDigitOccurence = relaxMaxDigitOccurence;
	}

	public final List<String> getArguments() {
		return Collections.unmodifiableList(arguments);
	}
}
