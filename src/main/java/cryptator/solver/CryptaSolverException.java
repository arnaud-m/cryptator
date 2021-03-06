/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

public class CryptaSolverException extends Exception {

	private static final long serialVersionUID = -5737530448191051676L;

	public CryptaSolverException(String message) {
		super(message);
	}

	public CryptaSolverException(String message, Throwable cause) {
		super(message, cause);
	}

}
