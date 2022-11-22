/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import static org.junit.Assert.assertEquals;

import org.chocosolver.solver.Model;
import org.junit.Before;
import org.junit.Test;

import cryptator.gen.CryptaMemberCard;
import cryptator.gen.CryptaMemberElt;
import cryptator.gen.CryptaMemberLen;
import cryptator.gen.WordsListModel;
import cryptator.specs.ICryptaGenModel;

public class GenModelTest {

	private final String[] words = new String[] {"a", "b", "ba", "bb", "baa", "bab", "bba", "bbb", "baaa"};

	private ICryptaGenModel[] models;
	
	@Before
	public void buildGenModels() {
		models = new ICryptaGenModel[] {
			new CryptaMemberLen(new Model(), words, ""),
			new CryptaMemberCard(new Model(), words, ""),
			new WordsListModel(new Model(), words)
		};
		for (ICryptaGenModel m : models) {
			m.buildModel();
		}
	}
	
	private void postMaxWordCountConstraint(ICryptaGenModel model, int maxWordCount) {
		model.getWordCount().le(maxWordCount).post();		
	}
	
	private void testGenModels(int expectedSolutionCount, int maxWordCount) {
		for (ICryptaGenModel m : models) {
			postMaxWordCountConstraint(m, maxWordCount);
			testGenModel(m, expectedSolutionCount);
		}
	}
	
	private void testGenModel(ICryptaGenModel model, int expectedSolutionCount) {
		assertEquals(expectedSolutionCount, model.getModel().getSolver().streamSolutions().count());	
	}
	
	@Test
	public void testGenModels1() {
		testGenModels(10, 1);
	}
	
	@Test
	public void testGenModels2() {
		testGenModels(46, 2);
	}
	
	@Test
	public void testGenModels3() {
		testGenModels(130, 3);
	}
	
	@Test
	public void testWLModel() {
		WordsListModel m = new WordsListModel(new Model(), words);
		m.buildModel();
		m.postMaxSymbolCountConstraint(1);
		postMaxWordCountConstraint(m, 2);
		testGenModel(m, 8);
	}
	
	@Test
	public void testOneModel() {
		CryptaMemberElt m = new CryptaMemberElt(new Model(), words, "");
		m.buildModel();
		testGenModel(m, 9);
	}


}