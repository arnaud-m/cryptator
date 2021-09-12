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

public class FeaturesTest {

	public final CryptaParserWrapper parser = new CryptaParserWrapper();
	
	public FeaturesTest() {
		// TODO Auto-generated constructor stub
	}

	@Test
	public void testStats1() throws CryptaParserException {
		final ICryptaNode node = parser.parse("send+more=money");
		final CryptaFeatures feat = new CryptaFeatures(node);
		System.out.println(feat);
	}
	
}
