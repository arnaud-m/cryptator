/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

public class CryptaSolutionException extends Exception {

    private static final long serialVersionUID = -2484196043007677114L;

    public CryptaSolutionException(final String message) {
        super(message);
    }

}
