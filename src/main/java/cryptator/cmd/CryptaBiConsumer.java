/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.cmd;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.slf4j.Logger;

import cryptator.JULogUtil.LoggerType;
import cryptator.specs.ICryptaEvaluation;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ICryptaSolutionStore;
import cryptator.tree.CryptaEvaluation;
import cryptator.tree.CryptaEvaluationException;
import cryptator.tree.GraphvizExport;
import cryptator.tree.TreeUtils;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;

public class CryptaBiConsumer implements BiConsumer<ICryptaNode, ICryptaSolution>, ICryptaSolutionStore {

	private final Logger logger;

	private int solutionCount;

	private Optional<ICryptaSolution> lastSolution;

	private int errorCount;

	private BiConsumer<ICryptaNode, ICryptaSolution> internal;

	public CryptaBiConsumer(final LoggerType loggerType) {
		super();
		this.logger = loggerType.getLogger(CryptaBiConsumer.class);
		lastSolution = Optional.empty();
		internal = new SolutionCounter();
	}

	@Override
	public final int getSolutionCount() {
		return solutionCount;
	}

	public final Optional<ICryptaSolution> getLastSolution() {
		return lastSolution;
	}

	public final int getErrorCount() {
		return errorCount;
	}

	public void withSolutionLog() {
		internal = internal.andThen(new SolutionLogger());
	}

	public void withCryptarithmLog() {
		internal = internal.andThen(new CryptarithmLogger());
	}

	public void withSolutionCheck(final int base) {
		internal = internal.andThen(new SolutionChecker(base));
	}

	public void withGraphvizExport() {
		internal = internal.andThen(new GraphvizConsumer());
	}

	@Override
	public void accept(final ICryptaNode t, final ICryptaSolution u) {
		internal.accept(t, u);
	}

	public void logOnLastSolution() {
		if (lastSolution.isPresent()) {
			logger.info("Last cryptarithm solution #{}:\n{}", solutionCount, lastSolution.get());
		}
	}

	private class SolutionCounter implements BiConsumer<ICryptaNode, ICryptaSolution> {

		@Override
		public void accept(final ICryptaNode t, final ICryptaSolution u) {
			solutionCount++;
			lastSolution = Optional.of(u);
		}
	}

	private class SolutionLogger implements BiConsumer<ICryptaNode, ICryptaSolution> {

		@Override
		public void accept(final ICryptaNode t, final ICryptaSolution u) {
			logger.info("Find cryptarithm solution #{} [OK]\n{}", solutionCount, u);
		}
	}

	private class CryptarithmLogger implements BiConsumer<ICryptaNode, ICryptaSolution> {

		@Override
		public void accept(final ICryptaNode t, final ICryptaSolution u) {
			logger.atInfo().setMessage("Find cryptarithm #{} [OK]\n{}\n{}").addArgument(solutionCount).addArgument(() -> TreeUtils.writeInorder(t)).addArgument(u).log();
		}
	}

	private class SolutionChecker implements BiConsumer<ICryptaNode, ICryptaSolution> {

		private final int base;

		private final ICryptaEvaluation eval = new CryptaEvaluation();

		SolutionChecker(final int base) {
			super();
			this.base = base;
		}

		@Override
		public void accept(final ICryptaNode n, final ICryptaSolution s) {
			try {
				if (eval.evaluate(n, s, base).compareTo(BigInteger.ZERO) != 0) {
					logger.trace("Eval cryptarithm solution #{} [OK]", solutionCount);
				} else {
					errorCount++;
					logger.error("Eval cryptarithm solution #{} [KO]", solutionCount);
				}
			} catch (CryptaEvaluationException e) {
				errorCount++;
				logger.error("Eval cryptarithm solution #{} [FAIL]", solutionCount, e);
			}
		}
	}

	private class GraphvizConsumer implements BiConsumer<ICryptaNode, ICryptaSolution> {
		private static final int WIDTH = 800;

		@Override
		public void accept(final ICryptaNode n, final ICryptaSolution s) {
			try {
				final Graph graph = GraphvizExport.exportToGraphviz(n, s);
				final File file = File.createTempFile("cryptarithm-", ".svg");
				Graphviz.fromGraph(graph).width(WIDTH).render(Format.SVG).toFile(file);
				logger.info("Export cryptarithm solution #{} [OK]\n{}", solutionCount, file );
			} catch (IOException e) {
				logger.error("Export cryptarithm solution #{} [FAIL]\n", solutionCount, e);
			}
		}

	}

}
