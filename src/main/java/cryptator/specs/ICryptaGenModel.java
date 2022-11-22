/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.specs;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

/**
 * The Interface ICryptaGenVariables to access variables used for generating cryptarithms.
 */
/**
 * @author nono
 *
 */
public interface ICryptaGenModel {
	
	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	Model getModel();

	/**
	 * Gets the number of words or word variables.
	 *
	 * @return the number of words
	 */
	 default int getN() {
		return getWords().length; 
	 }

	/**
	 * Gets the words of the model.
	 *
	 * @return the word strings
	 */
	String[] getWords();

	/**
	 * Gets the word variables that indicates if the word is present.
	 *
	 * @return the boolean variables
	 */
	BoolVar[] getWordVars();

	/**
	 * Gets variable that represents the maximum length of a present word.
	 *
	 * @return the max. length integer variable 
	 */
	IntVar getMaxLength();

	/**
	 * Gets the the variable that represents the word count, i.e. the number of present word.
	 *
	 * @return the word count integer variable
	 */
	IntVar getWordCount();
	
	
	/**
	 * Post the constraints of the model
	 */
	void buildModel();
	
}