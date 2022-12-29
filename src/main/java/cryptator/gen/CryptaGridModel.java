/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;

import cryptator.specs.IChocoModel;

public final class CryptaGridModel implements IChocoModel {

    private final Model model;

    private final IntVar[][] grid;

    private final IntVar[][] tgrid;

    private final String format;

    public CryptaGridModel(final Model model, final int n, final int m) {
        super();
        this.model = model;
        this.grid = model.intVarMatrix("grid", n, n, 0, m - 1, false);
        this.tgrid = ArrayUtils.transpose(grid);
        int precision = (int) Math.floor(Math.log10(m)) + 1;
        this.format = " %" + precision + "d";
    }

    private void postAllDifferentConstraint() {
        model.allDifferent(ArrayUtils.flatten(grid)).post();
    }

    private void postSymmetryBreakingConstraints() {
        final int n = size();
        if (n > 1) {
            // Transposition
            grid[1][0].le(grid[0][1]).post();
            // Increasing first row and column with the execption of the last.
            for (int i = 1; i < n - 1; i++) {
                grid[0][i - 1].le(grid[0][i]).post();
                grid[i - 1][0].le(grid[i][0]).post();
            }
        }
    }

    public void buildModel() {
        postAllDifferentConstraint();
        postSymmetryBreakingConstraints();
    }

    @Override
    public Model getModel() {
        return model;
    }

    public int size() {
        return grid.length;
    }

    public IntVar getCell(int i, int j) {
        return grid[i][j];
    }

    public IntVar[][] getMatrix() {
        return grid;
    }

    public IntVar[][] getTranspose() {
        return tgrid;
    }

    public String toString(String[] words) {
        final int n = size();
        final StringBuilder b = new StringBuilder();
        final int maxlen = AbstractCryptaGenModel.getMaxLength(words);
        final String fmt = " %" + maxlen + "s";
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                b.append(String.format(fmt, words[grid[i][j].getValue()]));
            }
            b.append('\n');
        }
        return b.toString();
    }

    @Override
    public String toString() {
        final StringBuilder b = new StringBuilder();
        final int n = size();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                b.append(String.format(format, grid[i][j].getValue()));
            }
            b.append('\n');
        }
        return b.toString();
    }
}
