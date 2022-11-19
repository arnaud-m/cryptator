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
public interface ICryptaGenVariables {
	
	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	Model getModel();
	
	/**
	 * Gets the word variables that indicates if the word is present.
	 *
	 * @return the boolean variables
	 */
	BoolVar[] getWords();

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
	
}