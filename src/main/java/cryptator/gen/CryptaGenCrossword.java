/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen;

import java.util.stream.Stream;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import cryptator.CryptaOperator;
import cryptator.specs.ICryptaGenSolver;
import cryptator.specs.ICryptaNode;

class CryptaMemberPair implements ICryptaGenSolver {

    private final Model model;

    private final IntVar[] indices;

    private final CryptaMemberLen left;

    private final CryptaMemberElt right;

    public CryptaMemberPair(final IntVar[] indices, final String[] words, final String prefix) {
        super();
        this.model = indices[0].getModel();
        this.indices = indices;
        left = new CryptaMemberLen(model, words, prefix + "L_");
        right = new CryptaMemberElt(indices[indices.length - 1], words, prefix + "R_");
    }

    @Override
    public Model getModel() {
        return model;
    }

    public void buildModel() {
        left.buildModel();
        right.buildModel();
        postSymBreakLengthConstraint();
        postLeftChannelingConstraints();
    }

    private void postLeftChannelingConstraints() {
        final IntVar one = model.intVar(1);
        for (int j = 0; j < indices.length - 1; j++) {
            model.element(one, left.getWordVars(), indices[j], 0).post();
        }
        left.getWordCount().eq(indices.length - 1).post();
    }

    private void postSymBreakLengthConstraint() {
        left.getMaxLength().le(right.getMaxLength()).post();
    }

    public void postHeavyConstraints(final int base) {
        left.postLentghSumConstraints(right.getMaxLength(), base);
    }

    @Override
    public ICryptaNode recordCryptarithm() {
        return GenerateUtil.recordAddition(left, right);
    }

    Stream<IntVar> maxLengthStream() {
        return Stream.of(left.getMaxLength(), right.getMaxLength());
    }
}

public class CryptaGenCrossword extends AbstractCryptaListModel implements ICryptaGenSolver {

    private final int n;

    private final CryptaGridModel grid;

    private final CryptaMemberPair[] additions;

    public CryptaGenCrossword(int n, String[] words) {
        super(new Model(), words);
        this.n = n;
        this.grid = new CryptaGridModel(model, n, words.length);
        this.additions = new CryptaMemberPair[2 * n];
        createMembers();
    }

    private void createMembers() {
        for (int i = 0; i < n; i++) {
            final String prefix = "R" + (i + 1) + "_";
            additions[i] = new CryptaMemberPair(grid.getRow(i), words, prefix);
        }

        for (int i = 0; i < n; i++) {
            final String prefix = "C" + (i + 1) + "_";
            additions[n + i] = new CryptaMemberPair(grid.getCol(i), words, prefix);
        }
    }

    @Override
    public void buildModel() {
        super.buildModel();
        grid.buildModel();
        Stream.of(additions).forEach(CryptaMemberPair::buildModel);
        // TODO Set search strategy ?
        // getSolver().setSearch(Search.intVarSearch(ArrayUtils.flatten(grid.getMatrix())));

    }

    @Override
    protected void postWordConstraints() {
        final IntVar one = model.intVar(1);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                model.element(one, vwords, grid.getCell(i, j), 0).post();
            }
        }
    }

    public void postSymbolSymmetryBreakingConstraints() {

        IntVar[] vars = symbolsToVariables.values().toArray(new IntVar[symbolsToVariables.size()]);

        for (int i = 1; i < vars.length; i++) {
            vars[i - 1].ge(vars[i]).post();
        }
        // System.out.println(Arrays.toString(vars));
        // FIXME model.decreasing(vars, 0).post(); // raise a choco internal error

    }

    @Override
    protected void postWordCountConstraint() {
        super.postWordCountConstraint();
        wordCount.eq(n * n).post();
    }

    @Override
    protected void postMaxLengthConstraints() {
        IntVar[] vars = Stream.of(additions).flatMap(CryptaMemberPair::maxLengthStream).toArray(i -> new IntVar[i]);
        model.max(getMaxLength(), vars).post();
    }

    public void postHeavyConstraints(final int base) {
        Stream.of(additions).forEach(m -> m.postHeavyConstraints(base));
    }

    @Override
    public final ICryptaNode recordCryptarithm() {
        final Stream<ICryptaNode> additionNodes = Stream.of(additions).map(ICryptaGenSolver::recordCryptarithm);
        return GenerateUtil.reduceOperation(CryptaOperator.AND, additionNodes);
    }

    @Override
    public String toString() {
        return grid.toString(words);
    }

}
