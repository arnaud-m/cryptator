/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.parser;

import java.util.concurrent.CancellationException;

public class CryptaParserException extends CancellationException {

	// TODO Define useful constructors
	
	private static final long serialVersionUID = 6706871076287552877L;

	public CryptaParserException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CryptaParserException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
}
