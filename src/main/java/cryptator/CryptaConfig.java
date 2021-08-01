/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

public class CryptaConfig {

	public boolean isCaseSensitive = true;
	
	public boolean allowLeadingZeros = false;
	
	/**
	 * Base, or radix, of the positional Numeral System
    */
	public int arithmeticBase = 10;
	
	public boolean hornerScheme = false;
	
	public int relaxMinDigitOccurence = 0;
	
	public int relaxMaxDigitOccurence = 0;
	
	public CryptaConfig() {}

	public final boolean isCaseSensitive() {
		return isCaseSensitive;
	}

	public final void setCaseSensitive(boolean isCaseSensitive) {
		this.isCaseSensitive = isCaseSensitive;
	}

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

}
