/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import static cryptator.gen.TransformWord.removeDashes;
import static cryptator.gen.TransformWord.removeWhitespaces;
import static cryptator.gen.TransformWord.stripAccents;
import static cryptator.gen.TransformWord.toLowerCase;
import static cryptator.gen.TransformWord.toUpperCase;
import static cryptator.gen.TransformWord.translate;
import static cryptator.gen.TransformWord.translateAndNormalize;
import static org.junit.Assert.assertEquals;

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
    public void testNormalizeEN() {
        String ctryCd = "EN";
        String lang = "en";
        assertEquals("onehundredtwentyone", translateAndNormalize(ctryCd, lang, 121));
        assertEquals("twothousandonehundredtwentyone", translateAndNormalize(ctryCd, lang, 2121));
        assertEquals("twomilliontwohundredonethousandfivehundredfortyfive",
                translateAndNormalize(ctryCd, lang, 2201545));
    }

    @Test
    public void testNormalizePL() {
        String ctryCd = "PL";
        String lang = "pl";
        assertEquals("dwadziesciapiec", translateAndNormalize(ctryCd, lang, 25));
        assertEquals("trzydziesciszesc", translateAndNormalize(ctryCd, lang, 36));
        assertEquals("szescdziesiatdziewiec", translateAndNormalize(ctryCd, lang, 69));
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

    @Test
    public void testToUpper() {
        assertEquals("ABCDEF", toUpperCase("AbCdEf"));
    }

}
