/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.tree;

public class CryptaEvaluationException extends Exception {

	private static final long serialVersionUID = 3504706627293932594L;

	public CryptaEvaluationException(String message, Throwable cause) {
		super(message, cause);
	}

	public CryptaEvaluationException(String message) {
		super(message);
	}
	
	
	
}
