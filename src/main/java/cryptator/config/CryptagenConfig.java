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

    @Option(name = "-d", handler = ExplicitBooleanOptionHandler.class, usage = "dry run (generate but do not solve candidate cryptarithms)")
    private boolean dryRun;

    @Option(name = "-grid", usage = "grid size for crossword cryptarithm)")
    private int gridSize = 0;

    @Option(name = "-mult", handler = ExplicitBooleanOptionHandler.class, usage = "generate multiplication cryptarithms")
    private boolean multModel = false;

    @Option(name = "-longMult", handler = ExplicitBooleanOptionHandler.class, usage = "generate long multiplication cryptarithms")
    private boolean longMultModel = false;

    @Option(name = "-ctry", usage = "country code for doubly true cryptarithms)")
    private String countryCode = "EN";

    @Option(name = "-lang", usage = "language code for doubly true cryptarithms)")
    private String langCode = "en";

    @Option(name = "-t", usage = "number of threads (experimental)")
    private int nthreads = 1;

    @Option(name = "-minop", usage = "minimum number of left operands")
    private int minLeftOperands = 2;

    @Option(name = "-maxop", usage = "maximum number of left operands")
    private int maxLeftOperands = -1;

    @Option(name = "-lightM", handler = ExplicitBooleanOptionHandler.class, usage = "use less auxiliary variables")
    private boolean lightModel = true;

    @Option(name = "-lightP", handler = ExplicitBooleanOptionHandler.class, usage = "use weak consistency")
    private boolean lightPropagation;

    @Option(name = "-newHeavyP", handler = ExplicitBooleanOptionHandler.class, usage = "use new heavy constraints")
    public static boolean newLightPropagation = false;

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

    public final boolean isLightModel() {
        return lightModel;
    }

    public final void setLightModel(final boolean lightModel) {
        this.lightModel = lightModel;
    }

    public final boolean isLightPropagation() {
        return lightPropagation;
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
        return super.toString() + "\nc LANG " + langCode + "\nc THREADS " + nthreads + "\nc LIGHT_MOD " + lightModel
                + "\nc LIGHT_PROPAG " + lightPropagation;
    }

}
