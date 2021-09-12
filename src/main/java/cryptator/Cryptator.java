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
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import cryptator.parser.CryptaParserException;
import cryptator.parser.CryptaParserWrapper;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolver;
import cryptator.solver.CryptaSolverException;
import cryptator.specs.ICryptaEvaluation;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ICryptaSolver;
import cryptator.tree.CryptaEvaluation;
import cryptator.tree.CryptaEvaluationException;
import cryptator.tree.CryptaFeatures;
import cryptator.tree.GraphvizExport;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;

public class Cryptator {

	public static final Logger LOGGER = Logger.getLogger(Cryptator.class.getName());

	private Cryptator() {}


	public static void main(String[] args) {
		JULogUtil.configureLoggers();

		CryptatorOptionsParser optparser = new CryptatorOptionsParser();
		if ( !optparser.parseOptions(args)) return;
		final CryptatorConfig config = optparser.getConfig();

		final ICryptaSolver solver = buildSolver(config);

		final CryptaParserWrapper parser = new CryptaParserWrapper();
		
		int exitStatus = 0;
		for (String cryptarithm : config.getArguments()) {
			exitStatus += solve(cryptarithm, parser, solver, config);
		}
		System.exit(exitStatus);
	}

	static class CryptatorOptionsParser extends OptionsParser<CryptatorConfig> {

		public CryptatorOptionsParser() {
			super(Cryptator.class, new CryptatorConfig());
		}

		@Override
		protected void configureLoggers() {
			super.configureLoggers();
			if(config.isVerbose()) {
				JULogUtil.setLevel(Level.CONFIG, getLogger(), CryptaSolver.LOGGER);
			}
		}	
	}

	private final static ICryptaSolver buildSolver(CryptatorConfig config) {
		final CryptaSolver solver = new CryptaSolver(config.useBignum());
		solver.limitSolution(config.getSolutionLimit());
		solver.limitTime(config.getTimeLimit());
		return solver;
	}

	public static ICryptaNode parseCryptarithm(String cryptarithm, CryptaParserWrapper parser, Logger logger) throws CryptaParserException  {
		final ICryptaNode node = parser.parse(cryptarithm);
		logger.log(Level.INFO, "Parse cryptarithm {0} [OK]", cryptarithm);
		if(logger.isLoggable(Level.CONFIG)) {
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			writePostorder(node, os);
			logger.log(Level.CONFIG, "Display postorder internal cryptarithm:\n{0}", os);
		}
		return node;

	}

	private static int solve(String cryptarithm, CryptaParserWrapper parser, ICryptaSolver solver , CryptatorConfig config) {
		try {
			final ICryptaNode node = parseCryptarithm(cryptarithm, parser, LOGGER);

			if(LOGGER.isLoggable(Level.INFO)) {
				LOGGER.log(Level.INFO, "Cryptarithm features:\n{0}", new CryptaFeatures(node));
			}
			
			final AtomicInteger errorCount = new AtomicInteger();
			final BiConsumer<ICryptaNode, ICryptaSolution> consumer = buildBiConsumer(config, errorCount);
			final boolean solved = solver.solve(node, config, (s) -> {consumer.accept(node, s);}) ;
			final String status = errorCount.get() > 0 ? "ERROR" : (solved ? "OK" : "KO");
			LOGGER.log(Level.INFO, "Solve cryptarithm {0} [{1}]", new Object[] {cryptarithm, status});
			return errorCount.get();
		
		} catch (CryptaParserException e) {
			LOGGER.log(Level.SEVERE, "Parse cryptarithm " + cryptarithm + " [FAIL]", e);
		} catch (CryptaModelException e) {
			LOGGER.log(Level.SEVERE, "Model cryptarithm [FAIL]", e);
		} catch (CryptaSolverException e) {
			LOGGER.log(Level.SEVERE, "Solve cryptarithm [FAIL]", e);
		}
		return 1;
	}

	private static class DefaultConsumer implements BiConsumer<ICryptaNode, ICryptaSolution> {

		private int solutionCount = 0;
			
		@Override
		public void accept(ICryptaNode n, ICryptaSolution s) {
			solutionCount++;
			LOGGER.log(Level.INFO, "Find cryptarithm solution #{0} [OK]\n{1}", new Object[] {solutionCount, s});
		}
	}


	private static class CheckConsumer implements BiConsumer<ICryptaNode, ICryptaSolution> {
		
		private final AtomicInteger error;
		
		private final int base;

		private final ICryptaEvaluation eval = new CryptaEvaluation();

		public CheckConsumer(int base, AtomicInteger error) {
			super();
			this.base = base;
			this.error = error;
		}

		@Override
		public void accept(ICryptaNode n, ICryptaSolution s) {
			try {
				if(eval.evaluate(n, s, base) != 0) {
					LOGGER.info("Eval cryptarithm solution [OK]"); 
					return;
				}
				else LOGGER.warning("Eval cryptarithm solution [KO]"); 
			} catch (CryptaEvaluationException e) {
				LOGGER.log(Level.WARNING, "Eval cryptarithm solution [FAIL]", e);
			}
			error.incrementAndGet();
		}
	}

	private static class GraphvizConsumer implements BiConsumer<ICryptaNode, ICryptaSolution> {

		@Override
		public void accept(ICryptaNode n, ICryptaSolution s) {
			try {
				final Graph graph = GraphvizExport.exportToGraphviz(n, s);
				final File file = File.createTempFile("cryptarithm-", ".svg");
				Graphviz.fromGraph(graph).width(800).render(Format.SVG).toFile(file);
				LOGGER.log(Level.INFO, "Export cryptarithm solution [OK]\n{0}", file);
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Export cryptarithm solution [FAIL]", e);

			}
		}
	}

	private static BiConsumer<ICryptaNode, ICryptaSolution> buildBiConsumer(final CryptatorConfig config, AtomicInteger errorCount) {
		BiConsumer<ICryptaNode, ICryptaSolution> consumer = new DefaultConsumer();
		if(config.isCheckSolution()) {
			consumer = consumer.andThen(new CheckConsumer(config.getArithmeticBase(), errorCount));
		}
		if(config.isExportGraphiz()) {
			consumer = consumer.andThen(new GraphvizConsumer());
		}
		return consumer;
	}
	
	public static BiConsumer<ICryptaNode, ICryptaSolution> buildBiConsumer(final CryptatorConfig config) {
		BiConsumer<ICryptaNode, ICryptaSolution> consumer = new DefaultConsumer();
		AtomicInteger errorCount = new AtomicInteger();
		if(config.isCheckSolution()) {
			consumer = consumer.andThen(new CheckConsumer(config.getArithmeticBase(), errorCount));
		}
		if(config.isExportGraphiz()) {
			consumer = consumer.andThen(new GraphvizConsumer());
		}

		return consumer;
	}


}
