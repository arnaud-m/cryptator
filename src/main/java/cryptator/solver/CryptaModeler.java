/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Function;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.expression.discrete.arithmetic.ArExpression;
import org.chocosolver.solver.expression.discrete.relational.ReExpression;
import org.chocosolver.solver.variables.IntVar;

import cryptator.CryptaConfig;
import cryptator.specs.ICryptaModeler;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ITraversalNodeConsumer;
import cryptator.tree.TreeTraversals;

public class CryptaModeler implements ICryptaModeler {

	public CryptaModeler() {}

	@Override
	public CryptaModel model(ICryptaNode cryptarithm, CryptaConfig config) throws CryptaModelException {
		final Model model = new Model("Cryptarithm");
		final ModelerConsumer modelerNodeConsumer = new ModelerConsumer(model, config);
		TreeTraversals.postorderTraversal(cryptarithm, modelerNodeConsumer);
		modelerNodeConsumer.cryptarithmEquationConstraint().decompose().post();
		modelerNodeConsumer.globalCardinalityConstraint().post();
		return new CryptaModel(model, modelerNodeConsumer.symbolsToVariables);
	}


	private final static class ModelerConsumer implements ITraversalNodeConsumer {

		public final Model model;

		public CryptaConfig config;

		public final Map<Character, IntVar> symbolsToVariables;

		private final Stack<ArExpression> stack = new Stack<ArExpression>();

		private final Consumer<char[]>firstSymbolConstraint;

		private final Function<char[], IntVar> wordVarBuilder;

		public ModelerConsumer(Model model, CryptaConfig config) {
			super();
			this.model = model;
			this.config = config;
			symbolsToVariables = new HashMap<Character, IntVar>();
			firstSymbolConstraint = config.allowLeadingZeros() ? 
					w -> {} : w -> {getSymbolVar(w[0]).gt(0).post();};	
					wordVarBuilder = config.useHornerScheme() ? new HornerVarBuilder() : new ExponentiationVarBuilder();
		}


		private IntVar createSymbolVar(char symbol) {
			return model.intVar(String.valueOf(symbol), 0, config.getArithmeticBase()-1, false);
		}

		private IntVar getSymbolVar(char symbol) {
			if(! symbolsToVariables.containsKey(symbol)) {
				symbolsToVariables.put(symbol, createSymbolVar(symbol));
			} 
			return symbolsToVariables.get(symbol);
		}

		private IntVar createWordVar(char[] word) {
			return model.intVar(new String(word), 0, (int) Math.pow(config.getArithmeticBase(), word.length) - 1);	
		}

		private final class ExponentiationVarBuilder implements Function<char[], IntVar> {

			@Override
			public IntVar apply(char[] word) {
				final int n = word.length;
				final IntVar[] vars = new IntVar[n];
				final int[] coeffs = new int[n];
				for (int i = 0; i < n; i++) {
					coeffs[i] = (int) Math.pow(config.getArithmeticBase(), n - 1 - i);
					vars[i] = getSymbolVar(word[i]);
				}
				final IntVar wvar = createWordVar(word);
				model.scalar(vars, coeffs, "=", wvar).post();
				return wvar;	
			}			
		}

		private final class HornerVarBuilder implements Function<char[], IntVar> {

			@Override
			public IntVar apply(char[] word) {
				// TODO @Margaux https://en.wikipedia.org/wiki/Horner%27s_method
				return null;
			}			
		}	

		private final IntVar makeWordVar(char[] word) {
			firstSymbolConstraint.accept(word);
			return wordVarBuilder.apply(word);
		}

		@Override
		public void accept(ICryptaNode node, int numNode) {
			if(node.isLeaf()) {	
				stack.push(makeWordVar(node.getWord()));
			} else {
				final ArExpression b = stack.pop();
				final ArExpression a = stack.pop();
				stack.push(node.getOperator().getExpression().apply(a,  b));
			}
		}

		public ReExpression cryptarithmEquationConstraint() throws CryptaModelException {
			if(stack.size() != 1) throw new CryptaModelException("Invalid stack size at the end of modeling.");
			if (stack.peek() instanceof ReExpression) {
				return (ReExpression) stack.peek();
			} else 
			throw new CryptaModelException("Modeling error for the cryptarithm equation constraint.");
		}
		
		private IntVar[] getGCCVars() {
			final Collection<IntVar> vars = symbolsToVariables.values();
			return vars.toArray(new IntVar[vars.size()]);
		}
				
		private int[] getGCCValues() {
			int[] values = new int[config.getArithmeticBase()];
			for (int i = 0; i < values.length; i++) {
				values[i] = i;
			}
			return values;
		}
		
		private IntVar[] getGCCOccs(int lb, int ub) {
			return model.intVarArray("O", config.getArithmeticBase(), lb, ub, false);
		}
				
		public Constraint globalCardinalityConstraint() throws CryptaModelException {
			final IntVar[] vars = getGCCVars();
			final int n = vars.length;
			if(n == 0) throw new CryptaModelException("No symbol found while modeling !");
			final int b = config.getArithmeticBase();
			// FIXME Check that config int are positive ?
			final int minOcc = Math.max(0, (n/b) - config.getRelaxMinDigitOccurence() );
			final int maxOcc = Math.max(0, ((n+b-1)/b) + config.getRelaxMaxDigitOccurence() );
			if(maxOcc == 1) {
				return model.allDifferent(vars);
			} else {
				return model.globalCardinality(
						vars, 
						getGCCValues(), 
						getGCCOccs(minOcc, maxOcc), 
						true);
				
			}
		}

	}

}
