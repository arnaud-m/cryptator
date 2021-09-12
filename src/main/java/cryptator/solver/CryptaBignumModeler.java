/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import java.util.HashSet;
import java.util.Set;
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
import cryptator.specs.ITraversalNodeConsumer;
import cryptator.tree.TreeTraversals;

public class CryptaBignumModeler implements ICryptaModeler {

	public CryptaBignumModeler() {}

	/** bignum model with addition only that uses a little endian representation with a variable number of digits. */
	@Override
	public CryptaModel model(ICryptaNode cryptarithm, CryptaConfig config) throws CryptaModelException {
		UnsupportedOperatorDetector detector = new UnsupportedOperatorDetector();
		TreeTraversals.postorderTraversal(cryptarithm, detector); 
		if(detector.unsupportedOperators.size() > 0) throw new CryptaModelException("Unsupported bignum operator(s): " + detector.unsupportedOperators);

		final Model model = new Model("Cryptarithm");
		final AbstractModelerNodeConsumer modelerNodeConsumer = new ModelerBignumConsumer(model, config);
		TreeTraversals.postorderTraversal(cryptarithm, modelerNodeConsumer);
		modelerNodeConsumer.cryptarithmEquationConstraint().post();
		modelerNodeConsumer.globalCardinalityConstraint().post();
		return new CryptaModel(model, modelerNodeConsumer.symbolsToVariables);


	}
}

final class UnsupportedOperatorDetector implements ITraversalNodeConsumer {

	public Set<CryptaOperator> unsupportedOperators = new HashSet<>();

	@Override
	public void accept(ICryptaNode node, int numNode) {
		switch (node.getOperator()) {
		case ID:
		case ADD:
		case EQ: 
			break;
		default:
			unsupportedOperators.add(node.getOperator());
			break;
		}

	}
}

final class ModelerBignumConsumer extends AbstractModelerNodeConsumer {

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
		final int m = Math.min(a.length, b.length);
		final int n = Math.max(a.length, b.length);
		final ArExpression[] c = new ArExpression[n];
		for (int i = 0; i < m; i++) {
			c[i] = CryptaOperator.ADD.getExpression().apply(a[i], b[i]);
		}
		// Can only enter in one loop
		for (int i = m; i < a.length; i++) {
			c[i] = a[i];
		}
		for (int i = m; i < b.length; i++) {
			c[i] = b[i];
		}
		return c;
	} 

	class BignumArExpression {

		public final IntVar[] digits;

		public final IntVar[] carries;

		public BignumArExpression(ArExpression[] a, int n, String suffix) {
			super();
			digits = model.intVarArray("D" + suffix, n, 0, config.getArithmeticBase()-1);
			carries = model.intVarArray("C"+ suffix, n, 0, IntVar.MAX_INT_BOUND / config.getArithmeticBase());
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


	private final void applyEQ(ArExpression[] a, ArExpression[] b) {
		int n = Math.max(a.length,  b.length);
		BignumArExpression a1 = new BignumArExpression(a, n, "l");
		BignumArExpression b1 = new BignumArExpression(b, n, "r");
		for (int i = 0; i < n; i++) {
			model.post(a1.digits[i].eq(b1.digits[i]).decompose());
		}
		model.post(a1.carries[n-1].eq(b1.carries[n-1]).decompose());
	}

	
	@Override
	public void accept(ICryptaNode node, int numNode) {
		if(node.isLeaf()) {	
			stack.push(makeWordVars(node.getWord()));
		} else {
			final ArExpression[] b = stack.pop();
			final ArExpression[] a = stack.pop();
			switch (node.getOperator()) {
			case ADD:
				stack.push(applyADD(a, b));
				break;
			case EQ: 
				applyEQ(a, b);
				break;
			default:
				break;
			}		
		}
	}

	@Override
	public Constraint cryptarithmEquationConstraint() throws CryptaModelException {
		if(stack.empty()) return model.trueConstraint();
		else throw new CryptaModelException("Invalid stack size at the end of modeling.");
	}

}
