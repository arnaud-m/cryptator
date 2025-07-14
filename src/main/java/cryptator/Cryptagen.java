/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import cryptator.cmd.CryptaBiConsumer;
import cryptator.cmd.OptionsParserWithLog;
import cryptator.cmd.WordArray;
import cryptator.config.CryptagenConfig;
import cryptator.gen.CryptaListGenerator;
import cryptator.solver.CryptaModelException;

public final class Cryptagen {

	public static final Logger LOGGER = Logger.getLogger(Cryptagen.class.getName());

	private Cryptagen() {
	}

	public static void main(final String[] args) {
		JULogUtil.configureDefaultLoggers();
		final int exitCode = doMain(args);
		System.exit(exitCode);
	}

	public static int doMain(final String[] args) {
		try {
			CryptagenOptionsParser optparser = new CryptagenOptionsParser();
			final OptionalInt parserExitCode = optparser.parseOptions(args);
			if (parserExitCode.isPresent()) {
				return parserExitCode.getAsInt();
			}
			final CryptagenConfig config = optparser.getConfig();
			final WordArray words = buildWords(config.getArguments(), config);
			if (words != null) {
				return generate(words, config);
			} else {
				LOGGER.log(Level.WARNING, "Invalid word list.");
			}
			return -1;
		} finally {
			JULogUtil.flushLogs();
		}
	}

	private static void readWords(final List<String> words, final String argument) {
		final File file = new File(argument);
		if (file.canRead()) {
			try (Scanner s = new Scanner(file)) {
				while (s.hasNext()) {
					words.add(s.next());
				}
			} catch (FileNotFoundException e) {
				LOGGER.log(Level.SEVERE, "cant read words in file", e);
			}
		} else {
			words.add(argument);
		}
	}

	private static WordArray buildWords(final List<String> arguments, final CryptagenConfig config) {
		// Read words from arguments and files
		final List<String> words = new ArrayList<>();
		for (String argument : arguments) {
			readWords(words, argument);
		}
		// Handle doubly true word list
		if (words.size() == 2) {
			try {
				final int lb = Integer.parseInt(words.get(0));
				final int ub = Integer.parseInt(words.get(1));
				return new WordArray(config.getCountryCode(), config.getLangCode(), lb, ub);
			} catch (NumberFormatException e) {
				// Cannot build a number list.
				return null;
			}
		}
		// Handle word list
		return words.size() <= 2 ? null : new WordArray(words);
	}

	private static final class CryptagenOptionsParser extends OptionsParserWithLog<CryptagenConfig> {

		private static final String ARG_NAME = "WORDS...";

		protected CryptagenOptionsParser() {
			super(Cryptagen.class, new CryptagenConfig(), ARG_NAME, JULogUtil.getDefaultLogManager());
		}

	}

	private static CryptaBiConsumer buildBiConsumer(final CryptagenConfig config) {
		CryptaBiConsumer consumer = new CryptaBiConsumer(LOGGER);
		consumer.withCryptarithmLog();
		if (config.isExportGraphiz()) {
			consumer.withGraphvizExport();
		}
		return consumer;
	}

	private static int generate(final WordArray words, final CryptagenConfig config) {
		LOGGER.log(Level.CONFIG, () -> "Word List Features:\n" + words.toDimacs());
		final CryptaListGenerator gen = new CryptaListGenerator(words, config, LOGGER);
		final CryptaBiConsumer cons = buildBiConsumer(config);
		try {
			gen.generate(cons);
			LOGGER.log(Level.INFO, "Found {0} cryptarithm(s).", cons.getSolutionCount());

		} catch (CryptaModelException e) {
			LOGGER.log(Level.SEVERE, "Fail to build the model.", e);
			return -1;
		}
		return gen.getErrorCount() + cons.getErrorCount();
	}

}
