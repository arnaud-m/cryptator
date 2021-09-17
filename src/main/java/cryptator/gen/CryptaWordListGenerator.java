/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;

import cryptator.CryptagenConfig;
import cryptator.solver.AdaptiveSolver;
import cryptator.solver.CryptaModelException;
import cryptator.specs.ICryptaGenerator;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.tree.TreeUtils;

public class CryptaWordListGenerator implements ICryptaGenerator {

	private String[] words;

	private CryptagenConfig config;

	private Logger logger;


	public CryptaWordListGenerator(List<String> arguments, CryptagenConfig config, Logger logger) {
		this(arguments.toArray(new String[arguments.size()]), config, logger);
	}

	public CryptaWordListGenerator(String[] words, CryptagenConfig config, Logger logger) {
		super();
		this.words = words;
		this.config = config;
		this.logger = logger;
	}

	@Override
	public void generate(BiConsumer<ICryptaNode, ICryptaSolution> consumer) throws CryptaModelException {
		if(words == null || words.length == 0) return;
		final CryptaGenModel gen = new CryptaGenModel(words);
		gen.postMemberCardConstraints();
		gen.postMemberMaxLenConstraint();
		gen.postMaxDigitCountConstraint(10);
		Solver s = gen.getModel().getSolver();
		
		Consumer<ICryptaNode> cons = new LogConsumer(gen);
		
		if(! config.isDryRun()) {
			GenerateConsumer genConsumer= new GenerateConsumer(
					new AdaptiveSolver(), 
					config, 
					consumer
					);
			cons = cons.andThen(genConsumer);
		}
		while(s.solve()) {
			cons.accept(gen.recordCryptarithm());
		}
	}

	private class LogConsumer implements Consumer<ICryptaNode> {
		
		private final Solution solution;
		
				
		public LogConsumer(CryptaGenModel gen) {
			super();
			solution = new Solution(gen.getModel());
		}

		@Override
		public void accept(ICryptaNode t) {
			if(logger.isLoggable(Level.CONFIG)) {
				logger.log(Level.CONFIG, "candidate: {0}", TreeUtils.writeInorder(t));
				if(logger.isLoggable(Level.FINE)) {
					solution.record();
					logger.log(Level.FINE, "candidate from solver:\n{0}", solution);
				}
			}
		}

	}


}
