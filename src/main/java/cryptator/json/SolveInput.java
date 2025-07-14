/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.json;

import cryptator.config.CryptatorConfig;

public final class SolveInput {

    private String cryptarithm;

    private CryptatorConfig config;

    public SolveInput() {
        super();
    }

    public SolveInput(final String cryptarithm, final CryptatorConfig config) {
        super();
        this.cryptarithm = cryptarithm;
        this.config = config;
    }

    public String getCryptarithm() {
        return cryptarithm;
    }

    public void setCryptarithm(final String cryptarithm) {
        this.cryptarithm = cryptarithm;
    }

    public CryptatorConfig getConfig() {
        return config;
    }

    public void setConfig(final CryptatorConfig config) {
        this.config = config;
    }
}
