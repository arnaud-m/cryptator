/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.tree;

public class CryptaConstant extends CryptaLeaf {
	
	private final int constant;
	public CryptaConstant() {
		super();
		constant = Integer.parseInt(new String(getWord()));
	}

	public CryptaConstant(String word) {
		super(word);
		constant = Integer.parseInt(new String(getWord()));
	}

	public CryptaConstant(char[] word) {
		super(word);
		constant = Integer.parseInt(new String(getWord()));
	}

	@Override
	public boolean isConstantLeaf() {
		return true;
	}

	@Override
	public boolean isWordLeaf() {
		return false;
	}

	@Override
	public String write(){
		return "'" + constant + "'";
	}
}
