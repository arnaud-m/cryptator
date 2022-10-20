/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Function;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.expression.discrete.arithmetic.ArExpression;
import org.chocosolver.solver.expression.discrete.relational.ReExpression;
import org.chocosolver.solver.variables.IntVar;

import cryptator.config.CryptaConfig;
import cryptator.specs.ICryptaModeler;
import cryptator.specs.ICryptaNode;
import cryptator.tree.TreeTraversals;

public class CryptaModeler implements ICryptaModeler {

	@Override
	public CryptaModel model(ICryptaNode cryptarithm, CryptaConfig config) throws CryptaModelException {
		final Model model = new Model("Cryptarithm");
		final ModelerConsumer modelerNodeConsumer = new ModelerConsumer(model, config);
		TreeTraversals.postorderTraversal(cryptarithm, modelerNodeConsumer);
		modelerNodeConsumer.postConstraints();
		return modelerNodeConsumer.buildCryptaModel();
	}

}

final class ModelerConsumer extends AbstractModelerNodeConsumer {

	private final Deque<ArExpression> stack = new ArrayDeque<>();

	private final Function<char[], IntVar> wordVarBuilder;

	public ModelerConsumer(Model model, CryptaConfig config) {
		super(model, config);
		wordVarBuilder = config.getHornerScheme() ? new HornerVarBuilder() : new ExponentiationVarBuilder();
	}


	
	private final class ExponentiationVarBuilder implements Function<char[], IntVar> {

		@Override
		public IntVar apply(char[] word) {
			if(word.length == 0) return model.intVar(0);
			if(word.length == 1) return getSymbolVar(word[0]);
			
			final int n = word.length;
			final IntVar[] vars = new IntVar[n];
			final int[] coeffs = new int[n];
			for (int i = 0; i < n; i++) {
				coeffs[i] = (int) Math.pow(config.getArithmeticBase(), n - 1. - i);
				vars[i] = getSymbolVar(word[i]);
			}
			final IntVar wvar = createWordVar(word);
			model.scalar(vars, coeffs, "=", wvar).post();
			return wvar;	
		}			
		
		private IntVar createWordVar(char[] word) {
			return model.intVar(new String(word), 0, (int) Math.pow(config.getArithmeticBase(), word.length) - 1);	
		}

	}

	private final class HornerVarBuilder implements Function<char[], IntVar> {

		@Override
		public IntVar apply(char[] word) {
			if(word.length == 0) return model.intVar(0);
			if(word.length == 1) return getSymbolVar(word[0]);		
			ArExpression tmp = getSymbolVar(word[0]);
			for (int i = 1; i < word.length; i++) {
				tmp= tmp.mul(config.getArithmeticBase())
						.add(getSymbolVar(word[i]));
			}
			return tmp.intVar();
		}			
	}	

	private final IntVar makeWordVar(char[] word) {
		return wordVarBuilder.apply(word);
	}

	@Override
	public void accept(ICryptaNode node, int numNode) {
		super.accept(node, numNode);
		if(node.isLeaf()) {	
			stack.push(makeWordVar(node.getWord()));
		} else {
			final ArExpression b = stack.pop();
			final ArExpression a = stack.pop();
			stack.push(node.getOperator().getExpression().apply(a,  b));
		}
	}

	@Override
	public void postCryptarithmEquationConstraint() throws CryptaModelException {
		if(stack.size() != 1) throw new CryptaModelException("Invalid stack size at the end of modeling.");
		if (stack.peek() instanceof ReExpression) {
			((ReExpression) stack.peek()).decompose().post();
		} else 
			throw new CryptaModelException("Modeling error for the cryptarithm equation constraint.");
	}

}
