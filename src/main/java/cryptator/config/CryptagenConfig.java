/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.config;

import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.ExplicitBooleanOptionHandler;

public class CryptagenConfig extends CryptaCmdConfig {

    @Option(name = "-d", aliases = {
            "--dry-run"}, handler = ExplicitBooleanOptionHandler.class, usage = "Dry run (generate but do not solve candidate cryptarithms).")
    private boolean dryRun;

    @Option(name = "-grid", usage = "Grid size for crossword cryptarithm.")
    private int gridSize = 0;

    @Option(name = "-mult", handler = ExplicitBooleanOptionHandler.class, usage = "Generate multiplications.")
    private boolean multModel = false;

    @Option(name = "-longMult", handler = ExplicitBooleanOptionHandler.class, usage = "Generate long multiplications")
    private boolean longMultModel = false;

    @Option(name = "-ctry", usage = "Country code for doubly true cryptarithms.)")
    private String countryCode = "EN";

    @Option(name = "-lang", usage = "Language code for doubly true cryptarithms.)")
    private String langCode = "en";

    @Option(name = "-t", usage = "Number of threads (experimental).")
    private int nthreads = 1;

    @Option(name = "-minop", usage = "Minimum number of left operands.")
    private int minLeftOperands = 2;

    @Option(name = "-maxop", usage = "Maximum number of left operands.")
    private int maxLeftOperands = -1;

    @Option(name = "-lightP", handler = ExplicitBooleanOptionHandler.class, usage = "Use weak consistency.")
    private boolean lightPropagation;

    @Option(name = "-multUnique", handler = ExplicitBooleanOptionHandler.class, usage = "Set unique right term for multiplication.")
    private boolean multUnique;

    public final boolean isDryRun() {
        return dryRun;
    }

    public final int getGridSize() {
        return gridSize;
    }

    public final void setGridSize(final int gridSize) {
        this.gridSize = gridSize;
    }

    public final String getCountryCode() {
        return countryCode;
    }

    public final String getLangCode() {
        return langCode;
    }

    public final int getNthreads() {
        return nthreads;
    }

    public final int getMinLeftOperands() {
        return minLeftOperands;
    }

    public final int getMaxLeftOperands() {
        return maxLeftOperands;
    }

    public final boolean isLightPropagation() {
        return lightPropagation;
    }

    public final boolean isMultUnique() {
        return multUnique;
    }

    public final void setLightPropagation(final boolean lightPropagation) {
        this.lightPropagation = lightPropagation;
    }

    public final boolean isMultModel() {
        return multModel;
    }

    public final void setMultModel(final boolean multModel) {
        this.multModel = multModel;
    }

    public final boolean isLongMultModel() {
        return longMultModel;
    }

    public final void setLongMultModel(final boolean longMultModel) {
        this.longMultModel = longMultModel;
    }

    @Override
    public String toString() {
        return super.toString() + "\nc LANG " + langCode + "\nc THREADS " + nthreads + "\nc LIGHT_PROPAG "
                + lightPropagation;
    }

}
