/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.tree;

import java.util.ArrayList;
import java.util.List;

public class CryptaConstant extends CryptaLeaf {
	public static final int DEFAULT_BASE = 10;
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
	public boolean isConstant() {
		return true;
	}

	public int getConstant() {
		return constant;
	}

	/**
	 * @param newBase : the new base to which convert the current constant (Note the current constant is by default in base 10)
	 * @return an Integer[] of the little representation of the int in the newBase
	 *
	 * For example, the constant 178829 will be res = [13, 8, 10, 11, 2] since
	 * sum_{i from 0 to newBase - 1} res[i] * newBase^i = 178829
	 */
	public Integer[] changeBaseLittleEndian(int newBase){
		var length = getWord().length;
		List<Integer> newInt = new ArrayList<>();
		var n = this.constant;
		while (n > 0){
			var reminder = n % newBase;
			var divRes = n / newBase;
			newInt.add(reminder);
			n = divRes;
		}
		return newInt.toArray(new Integer[0]);
	}
}
