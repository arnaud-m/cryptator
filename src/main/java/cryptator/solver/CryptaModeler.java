package cryptator.solver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.chocosolver.solver.Model;
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
		final ModelerConsumer modelerNodeConsumer = new ModelerConsumer(model);
		TreeTraversals.postorderTraversal(cryptarithm, modelerNodeConsumer);
		return new CryptaModel(model, modelerNodeConsumer.symbolsToVariables);
	}

	private static class ModelerConsumer implements ITraversalNodeConsumer {

		public final Model model;
		
		public int base = 10;
	
		public final Map<Character, IntVar> symbolsToVariables;
		
		private final Stack<IntVar> stack = new Stack<IntVar>();
		
		public final Set<IntVar> firstSymbols;

		
		public ModelerConsumer(Model model) {
			super();
			this.model = model;
			symbolsToVariables = new HashMap<Character, IntVar>();
			firstSymbols = new HashSet<IntVar>();
		}

		private IntVar makeSymbolVar(char symbol) {
			if(! symbolsToVariables.containsKey(symbol)) {
				final IntVar svar = model.intVar(String.valueOf(symbol), 0, base-1);
				symbolsToVariables.put(symbol, svar);
			} 
			return symbolsToVariables.get(symbol);
		}
		
		
		private IntVar makeWordVar(char[] word) {
			firstSymbols.add(makeSymbolVar(word[0]));
			
			final int n = word.length;
			final IntVar[] vars = new IntVar[n];
			final int[] coeffs = new int[n];
			for (int i = 0; i < n; i++) {
				coeffs[i] = (int) Math.pow(base, n - 1 - i);
				vars[i] = makeSymbolVar(word[i]);
			}
			final IntVar wvar = model.intVar(new String(word), 0, (int) Math.pow(base, n) - 1);
			model.scalar(vars, coeffs, "=", wvar).post();
			return wvar;	
		}

		@Override
		public void accept(ICryptaNode node, int numNode) {
			if(node.isLeaf()) {
				stack.push(makeWordVar(node.getWord()));
			} else {
				
			}
			// TODO Auto-generated method stub
			
		}
		
		
	}
	
}
