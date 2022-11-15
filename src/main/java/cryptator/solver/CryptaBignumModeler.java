/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.expression.discrete.arithmetic.ArExpression;
import org.chocosolver.solver.variables.IntVar;

import cryptator.CryptaOperator;
import cryptator.config.CryptaConfig;
import cryptator.specs.ICryptaModeler;
import cryptator.specs.ICryptaNode;
import cryptator.tree.CryptaOperatorDetection;
import cryptator.tree.TreeTraversals;
import cryptator.tree.TreeUtils;

public class CryptaBignumModeler implements ICryptaModeler {

    /**
     * bignum model with addition only that uses a little endian representation with a variable number of digits.
     */
    @Override
    public CryptaModel model(ICryptaNode cryptarithm, CryptaConfig config) throws CryptaModelException {
        final CryptaOperatorDetection detect = TreeUtils.computeUnsupportedBignumOperator(cryptarithm);
        if (detect.hasUnsupportedOperator())
            throw new CryptaModelException("Unsupported bignum operator(s): " + detect.getUnsupportedOperator());

        final Model model = new Model("Cryptarithm-bignum");
        final AbstractModelerNodeConsumer modelerNodeConsumer = new ModelerBignumConsumer(model, config);
        TreeTraversals.postorderTraversal(cryptarithm, modelerNodeConsumer);
        modelerNodeConsumer.postConstraints();
        return modelerNodeConsumer.buildCryptaModel();
    }
}

final class ModelerBignumConsumer extends AbstractModelerNodeConsumer {

    private final Deque<ArExpression[]> stack = new ArrayDeque<>();

    public ModelerBignumConsumer(Model model, CryptaConfig config) {
        super(model, config);
    }

    private ArExpression[] makeWordVars(char[] word) {
        final int n = word.length;
        // little endian
        ArExpression[] vars = new ArExpression[n];
        for (int i = 0; i < n; i++) {
            vars[i] = getSymbolVar(word[n - 1 - i]);
        }
        return vars;
    }

    private ArExpression[] makeConstVars(char[] word) {
    	List<ArExpression> vars = new ArrayList<>();
        BigInteger n = new BigInteger(new String(word));
        BigInteger b = BigInteger.valueOf(config.getArithmeticBase());	
        while (n.compareTo(BigInteger.ZERO) > 0) {
        	BigInteger[] r = n.divideAndRemainder(b);
        	n = r[0];
        	vars.add(model.intVar(r[1].intValueExact()));
        }	
		ArExpression[] res = new ArExpression[vars.size()];
		return vars.toArray(res);
    }


    private ArExpression[] applyADD(ArExpression[] a, ArExpression[] b) {
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

    private void applyEQ(ArExpression[] a, ArExpression[] b) {
        int n = Math.max(a.length, b.length);
        BignumArExpression a1 = new BignumArExpression(a, n, "l");
        BignumArExpression b1 = new BignumArExpression(b, n, "r");
        for (int i = 0; i < n; i++) {
            a1.digits[i].eq(b1.digits[i]).decompose().post();
        }
        a1.carries[n - 1].eq(b1.carries[n - 1]).decompose().post();
    }

    private void apply(CryptaOperator op, ArExpression[] a, ArExpression[] b) {
    	  switch (op) {
          case ADD: {
          	stack.push(applyADD(a, b));
          	break;
          }
          case EQ: {
              applyEQ(a, b);
              if (!stack.isEmpty()) // TODO Raise CryptaModelException instead !
                  throw new IllegalStateException("Stack is not empty after accepting a relational operator.");
              else break;
          }
          default :
              //	Should never be in the default case, this exception is
              //  a program break in order to recall to modify the switch if
              //  a new operator in BigNum is added.
              //  Example case : we remove the MUL operator in computeUnsupportedBignumOperator
                  throw new IllegalStateException("Bignum operator is not yet implemented");
      }
    }
    @Override
    public void accept(ICryptaNode node, int numNode) {
        super.accept(node, numNode);
        if(node.isInternalNode()) {
        	if(! node.getOperator().equals(CryptaOperator.AND)) {
        	  final ArExpression[] b = stack.pop();
              final ArExpression[] a = stack.pop();
              apply(node.getOperator(), a, b);
        	} // else do nothing ; constraint are posted when the relational operator is popped.
        } else {
        	if (node.isConstant()){
                stack.push(makeConstVars(node.getWord()));
            } else {
                stack.push(makeWordVars(node.getWord()));
            }
        }
    }

    @Override
    public void postCryptarithmEquationConstraint() throws CryptaModelException {
        if (!stack.isEmpty()) throw new CryptaModelException("Invalid stack size at the end of modeling.");
    }

    class BignumArExpression {

        public final IntVar[] digits;

        public final IntVar[] carries;

        public BignumArExpression(ArExpression[] a, int n, String suffix) {
            super();
            digits = model.intVarArray("D" + suffix, n, 0, config.getArithmeticBase() - 1);
            // TODO improve the bound ?
            carries = model.intVarArray("C" + suffix, n, 0, IntVar.MAX_INT_BOUND / config.getArithmeticBase());
            postScalar(
                    new IntVar[]{carries[0], digits[0], a[0].intVar()},
                    new int[]{config.getArithmeticBase(), 1, -1}
            );

            for (int i = 1; i < a.length; i++) {
                postScalar(
                        new IntVar[]{carries[i], digits[i], a[i].intVar(), carries[i - 1]},
                        new int[]{config.getArithmeticBase(), 1, -1, -1}
                );
            }
            for (int i = a.length; i < n; i++) {
                postScalar(
                        new IntVar[]{carries[i], digits[i], carries[i - 1]},
                        new int[]{config.getArithmeticBase(), 1, -1}
                );
            }
        }

        private void postScalar(IntVar[] vars, int[] coeffs) {
            model.scalar(vars, coeffs, "=", 0).post();
        }

    }

}
