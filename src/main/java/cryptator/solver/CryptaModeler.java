package cryptator.solver;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Function;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.expression.discrete.arithmetic.ArExpression;
import org.chocosolver.solver.expression.discrete.relational.ReExpression;
import org.chocosolver.solver.variables.IntVar;

import cryptator.CryptaConfig;
import cryptator.specs.ICryptaModeler;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ITraversalNodeConsumer;
import cryptator.tree.CryptaEvaluationException;
import cryptator.tree.TreeTraversals;

public class CryptaModeler implements ICryptaModeler {

	public CryptaModeler() {}

	@Override
	public CryptaModel model(ICryptaNode cryptarithm, CryptaConfig config) throws CryptaEvaluationException {
		final Model model = new Model("Cryptarithm");

		final ModelerConsumer modelerNodeConsumer = new ModelerConsumer(model, config);
		TreeTraversals.postorderTraversal(cryptarithm, modelerNodeConsumer);
		modelerNodeConsumer.constraint().decompose().post();

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
			return model.intVar(String.valueOf(symbol), 0, config.getArithmeticBase()-1);
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
				// TODO @Margaux
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

		public ReExpression constraint() {
			if(stack.size() != 1) throw new IllegalStateException("Invalid stack size at the end of modeling.");
			return (ReExpression) stack.peek();
		}


	}

}
