/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import java.util.Stack;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.expression.discrete.arithmetic.ArExpression;
import org.chocosolver.solver.expression.discrete.relational.ReExpression;
import org.chocosolver.solver.variables.IntVar;

import cryptator.CryptaConfig;
import cryptator.CryptaOperator;
import cryptator.specs.ICryptaModeler;
import cryptator.specs.ICryptaNode;
import cryptator.tree.TreeTraversals;

public class CryptaBignumModeler implements ICryptaModeler {

	public CryptaBignumModeler() {}

	@Override
	public CryptaModel model(ICryptaNode cryptarithm, CryptaConfig config) throws CryptaModelException {
		final Model model = new Model("Cryptarithm");
		final AbstractModelerNodeConsumer modelerNodeConsumer = new ModelerBignumConsumer(model, config);
		TreeTraversals.postorderTraversal(cryptarithm, modelerNodeConsumer);
		modelerNodeConsumer.cryptarithmEquationConstraint().post();
		modelerNodeConsumer.globalCardinalityConstraint().post();
		return new CryptaModel(model, modelerNodeConsumer.symbolsToVariables);
	}
}



final class ModelerBignumConsumer extends AbstractModelerNodeConsumer {

	private final Stack<ArExpression[]> stack = new Stack<ArExpression[]>();
	
	public ModelerBignumConsumer(Model model, CryptaConfig config) {
		super(model, config);
	}


//	private IntVar createWordVar(char[] word) {
//		return model.intVar(new String(word), 0, (int) Math.pow(config.getArithmeticBase(), word.length) - 1);	
//	}

	
	private final ArExpression[] makeWordVar(char[] word) {
		ArExpression[] vars = new ArExpression[word.length];
		for (int i = 0; i < vars.length; i++) {
			vars[i] = getSymbolVar(word[i]);
		}
		return null;
	}

	@Override
	public void accept(ICryptaNode node, int numNode) {
		if(node.isLeaf()) {	
			stack.push(makeWordVar(node.getWord()));
		} else {
			final ArExpression[] b = stack.pop();
			final ArExpression[] a = stack.pop();
			if(node.getOperator() == CryptaOperator.ADD) {
			} else {
				System.err.println("Bignum operator not supported: " + node.getOperator());
				// throw new CryptaModelException("Bignum operator not supported: " + node.getOperator());
			}
			// TODO stack.push(node.getOperator().getExpression().apply(a,  b));
		}
	}

	@Override
	public Constraint cryptarithmEquationConstraint() throws CryptaModelException {
		if(stack.size() != 1) throw new CryptaModelException("Invalid stack size at the end of modeling.");
		if (stack.peek() instanceof ReExpression[]) {
			return null;
		} else 
			throw new CryptaModelException("Modeling error for the cryptarithm equation constraint.");
	}

}
