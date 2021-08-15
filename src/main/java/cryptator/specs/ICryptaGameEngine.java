/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.specs;

import java.util.function.BiConsumer;

import cryptator.game.CryptaGameDecision;
import cryptator.game.CryptaGameException;
import cryptator.solver.CryptaModel;

public interface ICryptaGameEngine {

	void setUp(CryptaModel model) throws CryptaGameException;
	
	boolean takeDecision(CryptaGameDecision decision) throws CryptaGameException;

	void forEachSymbolDomain(BiConsumer<Character, String> consumer);
	
	boolean isSolved();
	
	void tearDown() throws CryptaGameException;
	
}
