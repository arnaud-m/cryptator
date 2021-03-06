/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import org.junit.Test;

import cryptator.parser.CryptaParserException;
import cryptator.parser.CryptaParserWrapper;
import cryptator.specs.ICryptaNode;
import cryptator.tree.CryptaFeatures;
import cryptator.tree.TreeUtils;

import static org.junit.Assert.*;
public class FeaturesTest {

	public final CryptaParserWrapper parser = new CryptaParserWrapper();
	
	public FeaturesTest() {
	}

	@Test
	public void testFeatures1() throws CryptaParserException {
		final ICryptaNode node = parser.parse("send+more=money");
		final CryptaFeatures feat = TreeUtils.computeFeatures(node);
		assertEquals(3, feat.getWordCount());
		assertEquals(13, feat.getCharCount());
		assertEquals(4, feat.getMinWordLength());
		assertEquals(5, feat.getMaxWordLength());		
		assertEquals(8, feat.getSymbols().size());
		assertEquals(2, feat.getOperators().size());	
	}
	
	@Test
	public void testFeatures2() throws CryptaParserException {
		final ICryptaNode node = parser.parse("iowa+nevada+indiana=georgia");
		final CryptaFeatures feat = TreeUtils.computeFeatures(node);
		assertEquals(4, feat.getWordCount());
		assertEquals(24, feat.getCharCount());
		assertEquals(4, feat.getMinWordLength());
		assertEquals(7, feat.getMaxWordLength());		
		assertEquals(10, feat.getSymbols().size());
		assertEquals(2, feat.getOperators().size());	
	}
	
}
