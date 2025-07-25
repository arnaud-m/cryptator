/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.config;

import org.kohsuke.args4j.Option;

public class CryptagenConfig extends CryptaCmdConfig {

    public enum GenerateType {
        ADD, MUL, LMUL, CROSS,
    }

    @Option(name = "-g", aliases = {"--generate"}, usage = "Select the type of generator.")
    private GenerateType generateType = GenerateType.ADD;

    @Option(name = "-d", aliases = {"--dry-run"}, usage = "Dry run (generate but do not solve candidate cryptarithms).")
    private boolean dryRun;

    public enum RightMemberType {
        FREE, UNIQUE, FIXED
    }

    @Option(name = "--right", usage = "Select the right member type.")
    private RightMemberType rightMemberType = RightMemberType.UNIQUE;

    @Option(name = "--cross", usage = "Set the grid size of a crossword.")
    private int gridSize = 3;

    @Option(name = "--ctry", usage = "Country code for doubly true cryptarithms.")
    private String countryCode = "EN";

    @Option(name = "--lang", usage = "Language code for doubly true cryptarithms.")
    private String langCode = "en";

    @Option(name = "--min", usage = "Minimum number of words.")
    private int minWords = 3;

    @Option(name = "--max", usage = "Maximum number of words.")
    private int maxWords = -1;

    @Option(name = "--light", hidden = true, usage = "Use a light CP model.")
    private boolean lightModel;

    @Option(name = "--threads", hidden = true, usage = "Number of threads (experimental).")
    private int nthreads = 1;

    public final GenerateType getGenerateType() {
        return generateType;
    }

    public final void setGenerateType(final GenerateType generateType) {
        this.generateType = generateType;
    }

    public final RightMemberType getRightMemberType() {
        return rightMemberType;
    }

    public final void setRightMemberType(final RightMemberType rightMemberType) {
        this.rightMemberType = rightMemberType;
    }

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

    public final int getMinWords() {
        return minWords;
    }

    public final int getMaxWords() {
        return maxWords;
    }

    public final boolean isLightModel() {
        return lightModel;
    }

    public final void setLightModel(final boolean lightModel) {
        this.lightModel = lightModel;
    }

    @Override
    public String toString() {
        return super.toString() + "\nc GENERATE " + generateType + "\nc RIGHT_MEMBER " + rightMemberType + "\nc LANG "
                + langCode + "\nc THREADS " + nthreads + "\nc LIGHT_PROPAG " + lightModel;
    }

}
