/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen;

import java.util.Locale;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.Normalizer2;
import com.ibm.icu.text.RuleBasedNumberFormat;

public final class TransformWord {
	
	private TransformWord() {
		super();
	}

	public static String translate(String ctryCd, String lang, int value) {
		final Locale locale = new Locale(lang, ctryCd);
		final RuleBasedNumberFormat rule = new RuleBasedNumberFormat(locale, RuleBasedNumberFormat.SPELLOUT);
		return rule.format(value);
	}
	
	public static String stripAccents(String input) {
		final String normalized = Normalizer2.getNFDInstance().normalize(input);
		return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}
	
	public static String removeWhitespaces(String input) {
		return input.replaceAll("\\s+", "");
	}
	
	public static String removeDashes(String input) {
		return input.replace("-", "");
	}
	
	public static String toLowerCase(String input) {
		return UCharacter.toLowerCase(input);
	}

}