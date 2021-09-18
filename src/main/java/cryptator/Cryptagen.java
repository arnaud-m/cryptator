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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import cryptator.Cryptagen.WordArray;
import cryptator.gen.CryptaListGenerator;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolver;

// TODO Convert Numbers to words: https://stackoverflow.com/a/56395508
// TODO https://github.com/allegro/tradukisto

// TODO Dictionnaire FR: https://chrplr.github.io/openlexicon/datasets-info/Liste-de-mots-francais-Gutenberg/README-liste-francais-Gutenberg.html
public class Cryptagen {

	public static final Logger LOGGER = Logger.getLogger(Cryptagen.class.getName());

	private Cryptagen() {}


	public static void main(String[] args) throws CryptaModelException {
		JULogUtil.configureLoggers();

		CryptagenOptionsParser optparser = new CryptagenOptionsParser();
		if ( !optparser.parseOptions(args)) return;
		final CryptagenConfig config = optparser.getConfig();

		System.out.println(config.getArguments());
		final WordArray words = buildWords(config.getArguments());
		System.out.println(words);
		if(words == null) System.exit(-1);

		// TODO Sort words ?

		final CryptaListGenerator gen = new CryptaListGenerator(words.getWords(), config, LOGGER);
		CryptaBiConsumer cons = buildBiConsumer(config);
		gen.generate(cons);

		int exitStatus = (int) cons.getErrorCount();
		System.exit(exitStatus);
	}

	private static List<String> readWords(String filename) {
		try {
			Scanner s = new Scanner(new File(filename));
			List<String> words = new ArrayList<>();
			while(s.hasNext()) {
				words.add(s.next());
			}
			return words;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null; // TODO
	}


	public static class WordArray {


		private final String[] words;

		private String rightMember;

		private final int lb;

		private final int ub;


		public WordArray(List<String> words, String rightMember) {
			super();
			final int n = words.size();
			this.rightMember = rightMember;
			if(rightMember == null) {
				this.words = new String[n];
				words.toArray(this.words);
			} else {
				this.words = new String[n + 1];
				words.toArray(this.words);
				this.words[n] = rightMember;
			}
			lb = -1;
			ub = -1;
		}


		public WordArray(Locale locale, int lb, int ub) {
			super();
			this.lb = lb;
			this.ub = ub;
			this.words = new String[ub + 1];
			for (int i = 0; i <= ub; i++) {
				words[i]= String.valueOf(i);
			}
			this.rightMember = null;
		}


		public final String[] getWords() {
			return words;
		}

		public boolean hasRightMember() {
			return rightMember != null;
		}

		public boolean isDoublyTrue() {
			return ub >= 0;
		}

		public final int getLB() {
			return lb;
		}

		public final int getUB() {
			return ub;
		}


		@Override
		public String toString() {
			return "WordArray [words=" + Arrays.toString(words) + ", rightMember=" + rightMember + ", lb=" + lb
					+ ", ub=" + ub + "]";
		}		
	}

	private static WordArray buildWords(List<String> arguments) {
		final int n = arguments.size();
		assert(n > 0); // already checked
		if(n == 2) {
			try {
				final int lb = Integer.parseInt(arguments.get(0));
				try {
					final int ub = Integer.parseInt(arguments.get(1));
					return new WordArray(Locale.FRANCE, lb, ub);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			} catch (NumberFormatException e) {
				// The first 
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
			return "[WORDS...]";
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
