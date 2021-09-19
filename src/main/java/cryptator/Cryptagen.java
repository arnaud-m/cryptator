/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import cryptator.cmd.CryptaBiConsumer;
import cryptator.cmd.OptionsParser;
import cryptator.cmd.WordArray;
import cryptator.config.CryptagenConfig;
import cryptator.gen.CryptaListGenerator;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolver;

// TODO Dictionnaire FR: https://chrplr.github.io/openlexicon/datasets-info/Liste-de-mots-francais-Gutenberg/README-liste-francais-Gutenberg.html
public class Cryptagen {

	public static final Logger LOGGER = Logger.getLogger(Cryptagen.class.getName());

	private Cryptagen() {}


	public static void main(String[] args) throws CryptaModelException {
		JULogUtil.configureLoggers();

		CryptagenOptionsParser optparser = new CryptagenOptionsParser();
		if ( !optparser.parseOptions(args)) return;
		final CryptagenConfig config = optparser.getConfig();

		final WordArray words = buildWords(config.getArguments(), config);
		if(words == null) System.exit(-1);
		
		// TODO Sort words ?

		final CryptaListGenerator gen = new CryptaListGenerator(words, config, LOGGER);
		CryptaBiConsumer cons = buildBiConsumer(config);
		gen.generate(cons);

		int exitStatus = (int) gen.getErrorCount() + cons.getErrorCount();
		System.exit(exitStatus);
	}

	private static List<String> readWords(String filename) {
		try {
			final Scanner s = new Scanner(new File(filename));
			try {
				final List<String> words = new ArrayList<>();
				while (s.hasNext()) {
					words.add(s.next());
				}
				return words;
			} finally {
				s.close();
			}
		} catch (FileNotFoundException e) {
			LOGGER.log(Level.SEVERE, "cant read words in file", e);
			return null;
		}
		
	}


	private static WordArray buildWords(List<String> arguments, CryptagenConfig config) {
		final int n = arguments.size();
		assert(n > 0); // already checked
		if(n == 2) {
			try {
				final int lb = Integer.parseInt(arguments.get(0));
				try {
					final int ub = Integer.parseInt(arguments.get(1));
					return new WordArray(config.getCountryCode(), config.getLangCode(), lb, ub);
				} catch (NumberFormatException e) {
					LOGGER.log(Level.SEVERE, "fail reading the second integer argument.", e);
					return null;
				}
			} catch (NumberFormatException e) {
				// Do nothing. 
				// The first argument is not an integer
			}
		}

		if(n <= 2) {
			final List<String> words = readWords(arguments.get(0));
			if(words == null) return null;
			return n == 1 ? new WordArray(words, null) : new WordArray(words, arguments.get(1));	
		} else {
			return new WordArray(arguments, null);
		}	
	}


	private static class CryptagenOptionsParser extends OptionsParser<CryptagenConfig> {

		public CryptagenOptionsParser() {
			super(Cryptagen.class, new CryptagenConfig());
		}

		@Override
		protected void configureLoggers() {
			super.configureLoggers();
			if(config.isDryRun()) {
				getLogger().setLevel(config.isVerbose() ? Level.FINE : Level.CONFIG);	
			} else {
				if(config.isVerbose()) {
					JULogUtil.setLevel(Level.CONFIG, getLogger());		
				} else {
					JULogUtil.setLevel(Level.WARNING, CryptaSolver.LOGGER);
				}
			}
		}

		@Override
		public String getArgumentName() {
			return "WORDS...";
		}	
	}

	private static CryptaBiConsumer buildBiConsumer(final CryptagenConfig config) {
		CryptaBiConsumer consumer = new CryptaBiConsumer(LOGGER);
		consumer.withCryptarithmLog();
		if(config.isCheckSolution()) consumer.withSolutionCheck(config.getArithmeticBase());
		if(config.isExportGraphiz()) consumer.withGraphvizExport();
		return consumer;
	}


}
