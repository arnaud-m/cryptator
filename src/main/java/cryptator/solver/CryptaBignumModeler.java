/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import java.util.Arrays;
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

	private CryptaOperator unsupportedOperator;

	private final Stack<ArExpression[]> stack = new Stack<ArExpression[]>();

	public ModelerBignumConsumer(Model model, CryptaConfig config) {
		super(model, config);
	}


	//	private IntVar createWordVar(char[] word) {
	//		return model.intVar(new String(word), 0, (int) Math.pow(config.getArithmeticBase(), word.length) - 1);	
	//	}


	private final ArExpression[] makeWordVars(char[] word) {
		ArExpression[] vars = new ArExpression[word.length];
		for (int i = 0; i < vars.length; i++) {
			vars[i] = getSymbolVar(word[i]);
		}
		return vars;
	}

	private final ArExpression[] applyADD(CryptaOperator op, ArExpression[] a, ArExpression[] b) {
		final ArExpression[] c = new ArExpression[b.length];
		for (int i = 0; i < a.length; i++) {
			c[i] = op.getExpression().apply(a[i], b[i]);
		}
		for (int i = a.length; i < b.length; i++) {
			c[i] = op.getExpression().apply(model.intVar(0), b[i]);
		}
		System.out.println(Arrays.toString(c));
		return c;
	} 
	
	private final ArExpression[] applyEQ(CryptaOperator op, ArExpression[] a, ArExpression[] b) {
		return null;
	}	
	
	@Override
	public void accept(ICryptaNode node, int numNode) {
		if(unsupportedOperator != null) return;
		if(node.isLeaf()) {	
			stack.push(makeWordVars(node.getWord()));
		} else {
			final CryptaOperator op = node.getOperator();
			final ArExpression[] b = stack.pop();
			final ArExpression[] a = stack.pop();
			switch (op) {
			case ADD:
				stack.push( a.length < b.length ? applyADD(op, a, b) : applyADD(op, b, a));
				break;
//			case EQ:
//				stack.push( a.length < b.length ? applyEQ(op, a, b) : applyEQ(op, b, a));
//				break;
		
			default:
				unsupportedOperator = op;
				break;
			}
			//if(op == CryptaOperator.ADD || op == CryptaOperator.SUB) {
			//} else {
			//	System.err.println("Bignum operator not supported: " + node.getOperator());
			// throw new CryptaModelException("Bignum operator not supported: " + node.getOperator());
			// }
			// TODO stack.push(node.getOperator().getExpression().apply(a,  b));
		}
	}

	@Override
	public Constraint cryptarithmEquationConstraint() throws CryptaModelException {
		if(unsupportedOperator != null) throw new CryptaModelException("Bignum operator not supported: " + unsupportedOperator);
		if(stack.size() != 1) throw new CryptaModelException("Invalid stack size at the end of modeling.");

		for (ArExpression expr : stack.peek()) {
			if (expr instanceof ReExpression) {
				System.out.println(expr);
			} else {
				throw new CryptaModelException("Modeling error for the cryptarithm equation constraint.");
			}
		}

		return model.trueConstraint();
	}

}
