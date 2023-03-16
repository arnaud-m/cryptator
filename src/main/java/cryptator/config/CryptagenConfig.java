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

    public enum GenerateType {
        ADD, MUL, LMUL, CROSS,
    }

    @Option(name = "-gen", aliases = {"--generate"}, usage = "Select the type of generator.")
    private GenerateType generateType;

    public enum RightMemberType {
        FREE, UNIQUE, FIXED
    }

    @Option(name = "-r", aliases = {"--right"}, usage = "Select the right member type.")
    private RightMemberType rightMemberType = RightMemberType.UNIQUE;

    @Option(name = "-d", aliases = {"--dry-run"}, usage = "Dry run (generate but do not solve candidate cryptarithms).")
    private boolean dryRun;

    @Option(name = "--cross", usage = "Generate crosswords with the given size")
    private int gridSize;

    @Option(name = "-ctry", usage = "Country code for doubly true cryptarithms.)")
    private String countryCode = "EN";

    @Option(name = "-lang", usage = "Language code for doubly true cryptarithms.)")
    private String langCode = "en";

    @Option(name = "--min", usage = "Minimum number of left operands.")
    private int minLeftOperands = 2;

    @Option(name = "--max", usage = "Maximum number of left operands.")
    private int maxLeftOperands = -1;

    @Option(name = "--light", hidden = true, handler = ExplicitBooleanOptionHandler.class, usage = "Use a light CP model.")
    private boolean lightModel;

    @Option(name = "--threads", hidden = true, usage = "Number of threads (experimental).")
    private int nthreads = 1;

    public final GenerateType getGenerateType() {
        return generateType;
    }

    public final void setGenerateType(GenerateType generateType) {
        this.generateType = generateType;
    }

    public final RightMemberType getRightMemberType() {
        return rightMemberType;
    }

    public final void setRightMemberType(RightMemberType rightMemberType) {
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

    public final int getMinLeftOperands() {
        return minLeftOperands;
    }

    public final int getMaxLeftOperands() {
        return maxLeftOperands;
    }

    public final boolean isLightModel() {
        return lightModel;
    }

    @Deprecated(forRemoval = true)
    public final boolean isMultUnique() {
        return rightMemberType == RightMemberType.UNIQUE;
    }

    public final void setLightModel(final boolean lightModel) {
        this.lightModel = lightModel;
    }

    @Deprecated(forRemoval = true)
    public final boolean isMultModel() {
        return generateType == GenerateType.MUL;
    }

    @Deprecated(forRemoval = true)
    public final void setMultModel(final boolean multModel) {
        this.generateType = multModel ? GenerateType.MUL : GenerateType.ADD;
    }

    @Deprecated(forRemoval = true)
    public final boolean isLongMultModel() {
        return generateType == GenerateType.LMUL;
    }

    @Deprecated(forRemoval = true)
    public final void setLongMultModel(final boolean longMultModel) {
        this.generateType = longMultModel ? GenerateType.LMUL : GenerateType.ADD;
    }

    @Override
    public String toString() {
        return super.toString() + "\nc GENERATE " + generateType + "\nc LANG " + langCode + "\nc THREADS " + nthreads
                + "\nc LIGHT_PROPAG " + lightModel;
    }

}
