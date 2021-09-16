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
import java.io.IOException;
import java.math.BigInteger;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import cryptator.specs.ICryptaEvaluation;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.tree.CryptaEvaluation;
import cryptator.tree.CryptaEvaluationException;
import cryptator.tree.GraphvizExport;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;


public class CryptaBiConsumer implements BiConsumer<ICryptaNode, ICryptaSolution> {

	private final Logger logger;

	private long solutionCount;

	private long errorCount;

	BiConsumer<ICryptaNode, ICryptaSolution> internal;

	public CryptaBiConsumer(Logger logger) {
		super();
		this.logger = logger;
		internal = new SolutionCounter();
	}

	public final long getSolutionCount() {
		return solutionCount;
	}


	public final long getErrorCount() {
		return errorCount;
	}

	public void withSolutionLog() {
		internal = internal.andThen(new SolutionLogger());
	}

	public void withSolutionCheck(int base) {
		internal = internal.andThen(new SolutionChecker(base));
	}

	public void withGraphvizExport() {
		internal = internal.andThen(new GraphvizConsumer());
	}

	@Override
	public void accept(ICryptaNode t, ICryptaSolution u) {
		internal.accept(t, u);
	}

	private class SolutionCounter implements BiConsumer<ICryptaNode, ICryptaSolution> {


		@Override
		public void accept(ICryptaNode t, ICryptaSolution u) {
			solutionCount++;
		}
	}

	private class SolutionLogger implements BiConsumer<ICryptaNode, ICryptaSolution> {

		@Override
		public void accept(ICryptaNode t, ICryptaSolution u) {
			logger.log(Level.INFO, "Find cryptarithm solution #{0} [OK]\n{1}", new Object[] {solutionCount, u});
		}
	}

	private class SolutionChecker implements BiConsumer<ICryptaNode, ICryptaSolution> {

		private final int base;

		private final ICryptaEvaluation eval = new CryptaEvaluation();

		public SolutionChecker(int base) {
			super();
			this.base = base;
		}

		@Override
		public void accept(ICryptaNode n, ICryptaSolution s) {
			try {
				if(eval.evaluate(n, s, base).compareTo(BigInteger.ZERO) != 0) {
					logger.info("Eval cryptarithm solution [OK]"); 
					return;
				}
				else logger.warning("Eval cryptarithm solution [KO]"); 
			} catch (CryptaEvaluationException e) {
				logger.log(Level.WARNING, "Eval cryptarithm solution [FAIL]", e);
			}
			errorCount++;
		}
	}

	private class GraphvizConsumer implements BiConsumer<ICryptaNode, ICryptaSolution> {

		@Override
		public void accept(ICryptaNode n, ICryptaSolution s) {
			try {
				final Graph graph = GraphvizExport.exportToGraphviz(n, s);
				final File file = File.createTempFile("cryptarithm-", ".svg");
				Graphviz.fromGraph(graph).width(800).render(Format.SVG).toFile(file);
				logger.log(Level.INFO, "Export cryptarithm solution [OK]\n{0}", file);
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Export cryptarithm solution [FAIL]", e);

			}
		}

	}

}

