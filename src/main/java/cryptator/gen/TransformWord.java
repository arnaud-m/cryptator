/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen;

import java.util.Locale;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.Normalizer2;
import com.ibm.icu.text.RuleBasedNumberFormat;

/**
 * The Class TransformWord provides helper static functions that wrap calls to the icu and java.string libraries
 */
public final class TransformWord {

	/**
	 * Instantiates a new transform word is private.
	 */
	private TransformWord() {
		super();
	}

	/**
	 * Translate an integer into words (decimal integer) in any language.
	 *
	 * @param ctryCd the country code of the java.util.Locale
	 * @param lang the language of the java.util.Locale
	 * @param value the integer value
	 * @return the integer converted to words
	 */
	public static String translate(String ctryCd, String lang, int value) {
		final Locale locale = new Locale(lang, ctryCd);
		final RuleBasedNumberFormat rule = new RuleBasedNumberFormat(locale, RuleBasedNumberFormat.SPELLOUT);
		return rule.format(value);
	}

	/**
	 * Translate and normalize an integer into words (decimal integer) in any language.
	 *
	 * @param ctryCd the country code of the java.util.Locale
	 * @param lang the language of the java.util.Locale
	 * @param value the integer value
	 * @return the integer converted to normalized words
	 */
	public static String translateAndNormalize(String ctryCd, String lang, int value) {
		final String translated = translate(ctryCd, lang, value);
		return toLowerCase(removeWhitespaces(removeDashes(stripAccents(translated))));
	}

	/**
	 * Strip accents of the input.
	 *
	 * @param input the input
	 * @return the input without accent
	 */
	public static String stripAccents(String input) {
		final String normalized = Normalizer2.getNFDInstance().normalize(input);
		return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}

	/**
	 * Removes the whitespaces of the input.
	 *
	 * @param input the input
	 * @return the string without whitespaces
	 */
	public static String removeWhitespaces(String input) {
		return input.replaceAll("\\s+", "");
	}

	/**
	 * Removes the dashes of the input.
	 *
	 * @param input the input
	 * @return the input without dashes
	 */
	public static String removeDashes(String input) {
		return input.replace("-", "");
	}

	/**
	 * Convert the input to lower case.
	 *
	 * @param input the input
	 * @return the input converted to lower case
	 */
	public static String toLowerCase(String input) {
		return UCharacter.toLowerCase(input);
	}

	/**
	 * Convert the input to upper case.
	 *
	 * @param input the input
	 * @return the input converted to upper case
	 */
	public static String toUpperCase(String input) {
		return UCharacter.toUpperCase(input);
	}
}