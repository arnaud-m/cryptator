/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import cryptator.config.CryptaConfig;
import cryptator.specs.ICryptaModeler;
import cryptator.specs.ICryptaNode;
import cryptator.tree.TreeTraversals;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.exception.SolverException;
import org.chocosolver.solver.expression.discrete.arithmetic.ArExpression;
import org.chocosolver.solver.expression.discrete.relational.ReExpression;
import org.chocosolver.solver.variables.IntVar;

import java.util.*;
import java.util.function.Function;

public class CryptaModelerPreuveParNeuf implements ICryptaModeler {

    @Override
    public CryptaModel model(final ICryptaNode cryptarithm, final CryptaConfig config) throws CryptaModelException {
        try {
            final Model model = new Model("Cryptarithm");
            final ModelerConsumerPreuveParNeuf modelerNodeConsumer = new ModelerConsumerPreuveParNeuf(model, config);
//            TreeTraversals.inorderTraversal(cryptarithm, modelerNodeConsumer);
            TreeTraversals.postorderTraversal(cryptarithm, modelerNodeConsumer);
            modelerNodeConsumer.postConstraints();
            modelerNodeConsumer.configureSearch();
            return modelerNodeConsumer.buildCryptaModel();
        } catch (SolverException e) {
            throw new CryptaModelException("Internal choco exception");
        }
    }

}

final class ModelerConsumerPreuveParNeuf extends AbstractModelerNodeConsumer {

    private final Deque<ArExpression> stack = new ArrayDeque<>();

    private final Function<char[], IntVar> wordVarBuilder;
    private List<Map<Character, Integer>> countVar=new ArrayList<>();
    private int iteration=-1;
    private int nbCryptarithme=-1;

    ModelerConsumerPreuveParNeuf(final Model model, final CryptaConfig config) {
        super(model, config);
        wordVarBuilder = config.getHornerScheme() ? new HornerVarBuilder() : new ExponentiationVarBuilder();
    }

    private IntVar makeWordVar(final ICryptaNode node) {
        return wordVarBuilder.apply(node.getWord());
    }

    private IntVar makeWordConst(final ICryptaNode node) {
        return model.intVar(Integer.parseInt(new String(node.getWord())));
    }

    @Override
    public void accept(final ICryptaNode node, final int numNode) {
        int acc=-1;

        if(iteration%2==0){
            acc=1;
        }

        if (!node.isInternalNode()) {
            for (int i=0; i<node.getWord().length; i++){
                countVar.get(nbCryptarithme).putIfAbsent(node.getWord()[i], 0);
                countVar.get(nbCryptarithme).put(node.getWord()[i], countVar.get(nbCryptarithme).get(node.getWord()[i])+acc);
            }
        }
    }

    @Override
    public void postCryptarithmEquationConstraint() throws CryptaModelException {
        if (stack.size() != 1) {
            throw new CryptaModelException("Invalid stack size at the end of modeling.");
        }
        if (stack.peek() instanceof ReExpression) {
            ((ReExpression) stack.peek()).decompose().post();
        } else {
            throw new CryptaModelException("Modeling error for the cryptarithm equation constraint.");
        }
    }

    private final class ExponentiationVarBuilder implements Function<char[], IntVar> {

        @Override
        public IntVar apply(final char[] word) {
            if (word.length == 0) {
                return model.intVar(0);
            }
            if (word.length == 1) {
                return getSymbolVar(word[0]);
            }

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

        private IntVar createWordVar(final char[] word) {
            return model.intVar(new String(word), 0, (int) Math.pow(config.getArithmeticBase(), word.length) - 1);
        }

    }

    private final class HornerVarBuilder implements Function<char[], IntVar> {

        @Override
        public IntVar apply(final char[] word) {
            if (word.length == 0) {
                return model.intVar(0);
            }
            if (word.length == 1) {
                return getSymbolVar(word[0]);
            }
            ArExpression tmp = getSymbolVar(word[0]);
            for (int i = 1; i < word.length; i++) {
                tmp = tmp.mul(config.getArithmeticBase()).add(getSymbolVar(word[i]));
            }
            return tmp.intVar();
        }
    }

    public void incrIteration(){
        iteration+=1;
    }

    public void incrNbCryptarithme(){
        nbCryptarithme+=1;
        countVar.add(new HashMap<>());
    }

    public List<Map<Character, Integer>> getCountVar() {
        return countVar;
    }
}

