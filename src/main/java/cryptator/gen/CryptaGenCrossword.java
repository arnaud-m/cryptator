/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
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

class CryptaCrossPair extends CryptaMemberPair {

    private final IntVar[] indices;

    public CryptaCrossPair(final IntVar[] indices, final String[] words, final String prefix, boolean useLenModel) {
        super(indices[indices.length - 1], words, prefix, useLenModel);
        this.indices = indices;
    }

    @Override
    public void buildModel() {
        super.buildModel();
        postLeftChannelingConstraints();

    }

    private void postLeftChannelingConstraints() {
        final IntVar one = getModel().intVar(1);
        for (int j = 0; j < indices.length - 1; j++) {
            getModel().element(one, left.getWordVars(), indices[j], 0).post();
        }
        left.getWordCount().eq(indices.length - 1).post();
    }

    Stream<IntVar> maxLengthStream() {
        return Stream.of(left.getMaxLength(), right.getMaxLength());
    }
}

public class CryptaGenCrossword extends AbstractCryptaListModel {

    private final int n;

    private final CryptaGridModel grid;

    private final CryptaCrossPair[] additions;

    public CryptaGenCrossword(int n, String[] words, boolean useLenModel) {
        super(new Model("Generate-Crossword"), words);
        this.n = n;
        this.grid = new CryptaGridModel(model, n, words.length);
        this.additions = new CryptaCrossPair[2 * n];
        createMembers(useLenModel);
    }

    private void createMembers(boolean useLenModel) {
        for (int i = 0; i < n; i++) {
            final String prefix = "R" + (i + 1) + "_";
            additions[i] = new CryptaCrossPair(grid.getRow(i), words, prefix, useLenModel);
        }

        for (int i = 0; i < n; i++) {
            final String prefix = "C" + (i + 1) + "_";
            additions[n + i] = new CryptaCrossPair(grid.getCol(i), words, prefix, useLenModel);
        }
    }

    private void setSolution() {
        int[][] solgrid = new int[][] {new int[] {0, 3, 4}, new int[] {1, 5, 7}, new int[] {2, 6, 8}};
        for (int i = 0; i < solgrid.length; i++) {
            for (int j = 0; j < solgrid[i].length; j++) {
                grid.getCell(i, j).eq(solgrid[i][j]).post();
            }
        }
    }

    @Override
    public void buildModel() {
        super.buildModel();
        grid.buildModel();
        Stream.of(additions).forEach(CryptaCrossPair::buildModel);
        // setSolution();
        // TODO Set search strategy ?
        // getSolver().setSearch(Search.intVarSearch(ArrayUtils.flatten(grid.getMatrix())));

    }

    @Override
    public void postFixedRightMemberConstraints() {
        grid.getCell(n - 1, n - 1).eq(getN() - 1).post();
    }

    @Override
    public void postDoublyTrueConstraints(int lowerBound) {
        // TODO post doubly true constraints for the crossword
        System.err.println("Not yet implemented");
    }

    @Override
    public void postHeavyConstraints(int base) {
        Stream.of(additions).forEach(m -> m.postHeavyConstraints(base));
    }

    @Override
    public void postPrecisionConstraints(int base) {
        // Nothing to do.
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
        model.decreasing(vars, 0).post();
    }

    @Override
    protected void postWordCountConstraint() {
        super.postWordCountConstraint();
        wordCount.eq(n * n).post();
    }

    @Override
    protected void postMaxLengthConstraints() {
        IntVar[] vars = Stream.of(additions).flatMap(CryptaCrossPair::maxLengthStream).toArray(i -> new IntVar[i]);
        model.max(getMaxLength(), vars).post();
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
