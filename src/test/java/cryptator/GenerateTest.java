/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import cryptator.cmd.CryptaBiConsumer;
import cryptator.cmd.WordArray;
import cryptator.config.CryptagenConfig;
import cryptator.gen.CryptaListGenerator;
import cryptator.solver.CryptaModelException;

public class GenerateTest {

	@BeforeClass
	public static void configureTestLoggers() {
		JULogUtil.configureTestLoggers();
	}
	
	public long testGenerate(WordArray wordArray) throws CryptaModelException {
		final CryptagenConfig config = new CryptagenConfig();
		final CryptaListGenerator gen = new CryptaListGenerator(wordArray, config, Cryptagen.LOGGER);
		CryptaBiConsumer cons = new CryptaBiConsumer(Cryptagen.LOGGER);
		cons.withSolutionCheck(config.getArithmeticBase());
		gen.generate(cons);
		assertEquals(0, cons.getErrorCount());
		return cons.getSolutionCount();
	}
	
	public long testGenerate(String rightMember, String... words) throws CryptaModelException {
		return testGenerate(new WordArray(Arrays.asList(words), rightMember));
	}
	
	@Test
	public void testSendMoreMoney() throws CryptaModelException {
	assertEquals(1, testGenerate(null, "send", "more", "money"));
	}
	
	@Test
	public void testPlanets1() throws CryptaModelException {
		assertEquals(2, testGenerate(null, "venus", "earth", "uranus", "saturn"));
	}
	
	@Test
	public void testPlanets2() throws CryptaModelException {
		assertEquals(1, testGenerate("planets", "venus", "earth", "uranus", "saturn"));
	}
	
	@Test
	public void testDoublyTrue1() throws CryptaModelException {
		assertEquals(0, testGenerate(new WordArray("FR", "fr", 0, 10)));
	}
	
	@Test
	public void testDoublyTrue2() throws CryptaModelException {
		assertEquals(1, testGenerate(new WordArray("FR", "fr", 30, 30)));
	}
	
	@Test
	public void testDoublyTrue3() throws CryptaModelException {
		assertEquals(3, testGenerate(new WordArray("IT", "it", 20, 30)));
	}
	
	
}
