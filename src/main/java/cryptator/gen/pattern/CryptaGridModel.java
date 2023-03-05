/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen.pattern;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;

import cryptator.gen.AbstractCryptaGenModel;
import cryptator.specs.IChocoModel;

/**
 * The Class CryptaGridModel assigns distinct words to a square grid.
 */
public final class CryptaGridModel implements IChocoModel {

    /** The model. */
    private final Model model;

    /** The grid of word indices. */
    private final IntVar[][] grid;

    /** The transposed grid of word indices. */
    private final IntVar[][] tgrid;

    /** The format for printing the grid. */
    private final String format;

    /**
     * Instantiates a new grid model.
     *
     * @param model the model
     * @param n     the grid dimension is 'n x n'
     * @param m     the number of words
     */
    public CryptaGridModel(final Model model, final int n, final int m) {
        super();
        this.model = model;
        this.grid = model.intVarMatrix("grid", n, n, 0, m - 1, false);
        this.tgrid = ArrayUtils.transpose(grid);
        int precision = (int) Math.floor(Math.log10(m)) + 1;
        this.format = " %" + precision + "d";
    }

    /**
     * Post all different constraint for the word indices.
     */
    private void postAllDifferentConstraint() {
        model.allDifferent(ArrayUtils.flatten(grid)).post();
    }

    /**
     * Post symmetry breaking constraints over the word indices.
     */
    private void postSymmetryBreakingConstraints() {
        final int n = size();
        if (n > 1) {
            // Transposition
            grid[1][0].le(grid[0][1]).post();
            // Increasing first row and column with the exception of the last.
            for (int i = 1; i < n - 1; i++) {
                grid[0][i - 1].le(grid[0][i]).post();
                grid[i - 1][0].le(grid[i][0]).post();
            }
        }
    }

    /**
     * Builds the grid model.
     */
    public void buildModel() {
        postAllDifferentConstraint();
        postSymmetryBreakingConstraints();
    }

    @Override
    public Model getModel() {
        return model;
    }

    /**
     * Gets the grid size.
     *
     * @return the grid size
     */
    public int size() {
        return grid.length;
    }

    /**
     * Gets the variable of the grid.
     *
     * @param i the row
     * @param j the column
     * @return the grid variable
     */
    public IntVar getCell(final int i, final int j) {
        return grid[i][j];
    }

    /**
     * Gets the i-th row of the grid.
     *
     * @param i the row index
     * @return the row array of variables
     */
    public IntVar[] getRow(final int i) {
        return grid[i];
    }

    /**
     * Gets the i-th column of the grid.
     *
     * @param i the column index
     * @return the row array of variables
     */
    public IntVar[] getCol(final int i) {
        return tgrid[i];
    }

    /**
     * Gets the matrix of grid variables.
     *
     * @return the matrix of word indices
     */
    public IntVar[][] getMatrix() {
        return grid;
    }

    /**
     * Gets the transposed matrix.
     *
     * @return the transpose
     */
    public IntVar[][] getTranspose() {
        return tgrid;
    }

    /**
     * To string with word mapping.
     *
     * @param words the words mapped to the indices
     * @return the grid of words
     */
    public String toString(final String[] words) {
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

    /**
     * To string with indices.
     *
     * @return the grid of indices
     */
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
