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

		final BiConsumer<ICryptaNode, ICryptaSolution> consumer = buildBiConsumer(config);

		for (String cryptarithm : config.getArguments()) {
			solve(cryptarithm, parser, solver, config, consumer);
		}
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
		final ICryptaSolver solver = new CryptaSolver();
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
			logger.log(Level.CONFIG, "Display postorder internal cryptarithm\n{0}", os);
		}
		return node;
		
	}
	
	private static void solve(String cryptarithm, CryptaParserWrapper parser, ICryptaSolver solver , CryptatorConfig config, BiConsumer<ICryptaNode, ICryptaSolution> consumer) {
		try {
			final ICryptaNode node = parseCryptarithm(cryptarithm, parser, LOGGER);
			//TODO Log statistics for the cryptarithm
			// FIXME The checker failure must change the status !
			// TODO Use also exit code ?
			final String status = solver.solve(node, config, (s) -> {consumer.accept(node, s);}) ? "OK" : "KO";
			LOGGER.log(Level.INFO, "Solve cryptarithm {0} [{1}]", new Object[] {cryptarithm, status});
		} catch (CryptaParserException e) {
			LOGGER.log(Level.SEVERE, "Parse cryptarithm " + cryptarithm + " [FAIL]", e);
		} catch (CryptaModelException e) {
			LOGGER.log(Level.SEVERE, "Model cryptarithm [FAIL]", e);
		} catch (CryptaSolverException e) {
			LOGGER.log(Level.SEVERE, "Solve cryptarithm [FAIL]", e);
		}
	}

	private static class DefaultConsumer implements BiConsumer<ICryptaNode, ICryptaSolution> {
		
		private int solutionCount = 0;
		@Override
		public void accept(ICryptaNode n, ICryptaSolution s) {
			solutionCount++;
			LOGGER.log(Level.INFO, "Solution #{0}.\n{1}", new Object[] {solutionCount, s});
		}
	}

	private static class CheckConsumer implements BiConsumer<ICryptaNode, ICryptaSolution> {

		public final int base;

		public final ICryptaEvaluation eval = new CryptaEvaluation();

		public CheckConsumer(int base) {
			super();
			this.base = base;
		}

		@Override
		public void accept(ICryptaNode n, ICryptaSolution s) {
			try {
				if(eval.evaluate(n, s, base) != 0) LOGGER.config("Eval cryptarithm solution [OK]"); 
				else  LOGGER.warning("Eval cryptarithm solution [KO]"); 
			} catch (CryptaEvaluationException e) {
				LOGGER.log(Level.WARNING, "Eval cryptarithm solution [FAIL]", e);
			}		
		}
	}

	private static class GraphvizConsumer implements BiConsumer<ICryptaNode, ICryptaSolution> {

		@Override
		public void accept(ICryptaNode n, ICryptaSolution s) {
			try {
				Graph graph = GraphvizExport.exportToGraphviz(n, s);
				File file = File.createTempFile("cryptarithm-", ".svg");
				Graphviz.fromGraph(graph).width(800).render(Format.SVG).toFile(file);
				LOGGER.log(Level.INFO, "Export cryptarithm solution to {0} [OK]", file);
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Export cryptarithm solution [FAIL]", e);
				
			}
		}
	}

	public static BiConsumer<ICryptaNode, ICryptaSolution> buildBiConsumer(final CryptatorConfig config) {
		BiConsumer<ICryptaNode, ICryptaSolution> consumer = new DefaultConsumer();

		if(config.isCheckSolution()) {
			consumer = consumer.andThen( new CheckConsumer(config.getArithmeticBase()));
		}
		if(config.isExportGraphiz()) {
			consumer = consumer.andThen(new GraphvizConsumer());
		}
		
		return consumer;
	}


}
