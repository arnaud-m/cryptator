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

import cryptator.gen.CryptaEqnMember;
import cryptator.gen.CryptaEqnMember2;
import cryptator.gen.CryptaEqnMember3;
import cryptator.gen.CryptaOneMember;
import cryptator.gen.WordsListModel;
import cryptator.specs.ICryptaGenVariables;

public class WordsListModelTest {

	private final String[] words = new String[] {"a", "b", "ba", "bb", "baa", "bab", "bba", "bbb", "baaa"};

	private ICryptaGenVariables[] models;
	
	@Before
	public void buildGenModels() {
		models = new ICryptaGenVariables[] {
			new CryptaEqnMember(new Model(), words, ""),
			new CryptaEqnMember2(new Model(), words, ""),
			new CryptaEqnMember3(new Model(), words, ""),
			new WordsListModel(new Model(), words)
		};
		for (ICryptaGenVariables m : models) {
			m.buildModel();
		}
	}
	
	private void postMaxWordCountConstraint(ICryptaGenVariables model, int maxWordCount) {
		model.getWordCount().le(maxWordCount).post();		
	}
	
	private void testGenModels(int expectedSolutionCount, int maxWordCount) {
		for (ICryptaGenVariables m : models) {
			postMaxWordCountConstraint(m, maxWordCount);
			testGenModel(m, expectedSolutionCount);
		}
	}
	
	private void testGenModel(ICryptaGenVariables model, int expectedSolutionCount) {
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
	public void testWLModel1() {
		WordsListModel m = new WordsListModel(new Model(), words);
		m.buildModel();
		m.postMaxSymbolCountConstraint(1);
		postMaxWordCountConstraint(m, 2);
		testGenModel(m, 8);
	}
	
	@Test
	public void testOneModel() {
		CryptaOneMember m = new CryptaOneMember(new Model(), words, "");
		m.buildModel();
		testGenModel(m, 9);
	}


}
