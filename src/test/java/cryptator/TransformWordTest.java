/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import static cryptator.gen.TransformWord.*;
import static org.junit.Assert.*;
import org.junit.Test;
public class TransformWordTest {

	
	@Test
	public void testTranslateFR() {
		String ctryCd = "FR";
		String lang = "fr";
		assertEquals("dix", translate(ctryCd, lang, 10));
		assertEquals("cent un", translate(ctryCd, lang, 101));
		assertEquals("deux mille cent dix", translate(ctryCd, lang, 2110));
		assertEquals("un million cent deux mille cent dix", translate(ctryCd, lang, 1102110));
	}
	
	@Test
	public void testTranslateEN() {
		String ctryCd = "EN";
		String lang = "en";
		assertEquals("eleven", translate(ctryCd, lang, 11));
		assertEquals("one hundred twenty-one", translate(ctryCd, lang, 121));
		assertEquals("two thousand one hundred twenty-one", translate(ctryCd, lang, 2121));
		assertEquals("two million two hundred one thousand five hundred forty-five", translate(ctryCd, lang, 2201545));
	}
	
	@Test
	public void testStripAccents() {
		assertEquals("aaaaeeeiiiooouuu", stripAccents("ãáàäéèëíìïóòöúùü"));
		assertEquals("cnvt", stripAccents("ćñṽẗ"));
	}
	
	@Test
	public void testRemoveWhitspaces() {
		assertEquals("abcdef", removeWhitespaces("a b  c   d\te \tf"));
	}
	
	@Test
	public void testDashes() {
		assertEquals("abcdef", removeDashes("a-b--c---d-e-f"));
	}
	
	@Test
	public void testToLowercase() {
		assertEquals("abcdef", toLowerCase("AbCdEf"));
	}
	
}
