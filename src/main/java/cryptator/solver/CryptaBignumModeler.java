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

	private CryptaOperator unsupportedOperator;

	private final Stack<ArExpression[]> stack = new Stack<ArExpression[]>();

	public ModelerBignumConsumer(Model model, CryptaConfig config) {
		super(model, config);
	}

	private final ArExpression[] makeWordVars(char[] word) {
		firstSymbolConstraint.accept(word);
		final int n = word.length;
		ArExpression[] vars = new ArExpression[n];
		for (int i = 0; i < n; i++) {
			vars[i] = getSymbolVar(word[n - 1 - i]);
		}
		// little endian
		return vars;
	}


	private final ArExpression[] applyADD(ArExpression[] a, ArExpression[] b) {
		final int n = Math.max(a.length, b.length);
		final ArExpression[] c = new ArExpression[b.length];
		for (int i = 0; i < a.length; i++) {
			c[i] = CryptaOperator.ADD.getExpression().apply(a[i], b[i]);
		}
		// Can only enter in one loop
		for (int i = a.length; i < n; i++) {
			c[i] = a[i];
		}
		for (int i = b.length; i < n; i++) {
			c[i] = b[i];
		}
		return c;
	} 

	private final ArExpression[] applySUB(ArExpression[] a, ArExpression[] b) {
		final ArExpression[] c = new ArExpression[b.length];
		for (int i = 0; i < a.length; i++) {
			c[i] = CryptaOperator.SUB.getExpression().apply(a[i], b[i]);
		}
		for (int i = a.length; i < b.length; i++) {
			c[i] = CryptaOperator.ADD.getExpression().apply(model.intVar(0), b[i]);
		}
		return c;
	} 

	private static int exprIndex = 1;

	class BignumArExpression {

		public final IntVar[] digits;

		public final IntVar[] carries;

		public BignumArExpression(ArExpression[] a, int n) {
			super();
			digits = model.intVarArray("D" + exprIndex, n, 0, config.getArithmeticBase()-1);
			carries = model.intVarArray("C"+ exprIndex, n, 0, IntVar.MAX_INT_BOUND / config.getArithmeticBase());
			exprIndex++;
			// TODO Is it better to use scalar or an expression ? 
			postScalar(
					new IntVar[] {carries[0], digits[0], a[0].intVar()},
					new int[] {config.getArithmeticBase(), 1, -1}
					);

			for (int i = 1; i < a.length; i++) {
				postScalar(
						new IntVar[] {carries[i], digits[i], a[i].intVar(), carries[i-1]},
						new int[] {config.getArithmeticBase(), 1, -1, -1}
						);
			}
			for (int i = a.length; i < n; i++) {
				postScalar(
						new IntVar[] {carries[i], digits[i], carries[i-1]},
						new int[] {config.getArithmeticBase(), 1, -1}
						);
			}
		}

		private void postScalar(IntVar[] vars, int[] coeffs) {
			model.post(model.scalar(vars, coeffs, "=", 0));
		}

	}


	private final ArExpression[] applyEQ(ArExpression[] a, ArExpression[] b) {
		int n = Math.max(a.length,  b.length);
		BignumArExpression a1 = new BignumArExpression(a, n);
		BignumArExpression b1 = new BignumArExpression(b, n);
		for (int i = 0; i < n; i++) {
			model.post(a1.digits[i].eq(b1.digits[i]).decompose());
		}
		model.post(a1.carries[n-1].eq(b1.carries[n-1]).decompose());
		return new ArExpression[0];
	}	

	private ArExpression[] applyOperator(CryptaOperator op, ArExpression[] a, ArExpression[] b) {
		switch (op) {
		case ADD: return applyADD(a, b);
		case EQ: return applyEQ(a, b);
		//case EQ: return new ArExpression[0];
		default:
			unsupportedOperator = op;
			return null;
		}

	}
	@Override
	public void accept(ICryptaNode node, int numNode) {
		if(unsupportedOperator != null) return;
		if(node.isLeaf()) {	
			stack.push(makeWordVars(node.getWord()));
		} else {
			final ArExpression[] b = stack.pop();
			final ArExpression[] a = stack.pop();
			ArExpression[] expr = applyOperator(node.getOperator(), a, b);
			if( expr != null) stack.push(expr);
		}
	}

	@Override
	public Constraint cryptarithmEquationConstraint() throws CryptaModelException {
		//if(unsupportedOperator != null) throw new CryptaModelException("Bignum operator not supported: " + unsupportedOperator);
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
