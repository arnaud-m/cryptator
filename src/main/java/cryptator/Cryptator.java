/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

public class Cryptator {

	public Cryptator() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		CryptaParserWrapper parser = new CryptaParserWrapper();
    	parser.parse("p1+di*n2=z=z");
    	parser.parse("p1+di*n2=z=u");
    	parser.parse("p1+di*+n2=z");
    	parser.parse("p+di*n=z"); 
	}

}
