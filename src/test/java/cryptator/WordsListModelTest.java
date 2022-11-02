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
import org.junit.Test;

import cryptator.gen.WordsListModel;

public class WordsListModelTest {
	
	private final String[] words = new String[] {"0", "1", "10", "11", "100", "101", "110", "111", "1000"};
	
	@Test
	public void testWLModel1() {
		WordsListModel m = new WordsListModel(new Model(), words);
		m.postMaxDigitCountConstraint(1);
		assertEquals(9, m.getModel().getSolver().streamSolutions().count());
	}
	
	@Test
	public void testWLModel2() {
		WordsListModel m = new WordsListModel(new Model(), words);
		m.postMaxWordCountConstraint(2);
		assertEquals(46, m.getModel().getSolver().streamSolutions().count());
	}
	
	@Test
	public void testWLModel3() {
		WordsListModel m = new WordsListModel(new Model(), words);
		m.postMaxWordCountConstraint(3);
		assertEquals(130, m.getModel().getSolver().streamSolutions().count());
	}
	
	@Test
	public void testWLModel4() {
		WordsListModel m = new WordsListModel(new Model(), words);
		m.postMaxDigitCountConstraint(1);
		m.postMaxWordCountConstraint(2);
		assertEquals(8, m.getModel().getSolver().streamSolutions().count());
	}
	

}
