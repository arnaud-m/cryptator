/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import static cryptator.tree.TreeUtils.writePostorder;

import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import cryptator.gen.CryptaWordListGenerator;
import cryptator.parser.CryptaParserException;
import cryptator.parser.CryptaParserWrapper;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolver;
import cryptator.solver.CryptaSolverException;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolver;
import cryptator.tree.CryptaFeatures;
import cryptator.tree.TreeUtils;

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

		
		final CryptaWordListGenerator gen = new CryptaWordListGenerator(config.getArguments());
		// TODO Must collect errors !
		gen.generate(buildBiConsumer(config));
		
				
		int exitStatus = 0;
		System.exit(exitStatus);
	}

	private static class CryptagenOptionsParser extends OptionsParser<CryptagenConfig> {

		public CryptagenOptionsParser() {
			super(Cryptagen.class, new CryptagenConfig());
		}

		@Override
		protected void configureLoggers() {
			super.configureLoggers();
			if(config.isVerbose()) {
				// TODO JULogUtil.setLevel(Level.CONFIG, getLogger(), CryptaSolver.LOGGER);
				
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
