/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.tools.ArrayUtils;

public class CryptaMemberUse extends CryptaGenBaseModel {

	protected final BoolVar[] useLength;

	protected SetVar setLength;

	public CryptaMemberUse(Model m, String[] words, String prefix) {
		super(m, words, prefix, true);
		final int maxLen = getMaxLength(words);
		useLength = m.boolVarArray(prefix + "useLen", maxLen + 1);
		setLength = m.setVar(prefix + "useLen", new int[] {}, ArrayUtils.linspace(0, maxLen + 1));

	}

	protected void postUseLengthConstraints() {
		Map<Integer, BoolVar[]> map = buildWordsByLength();
		// Always use the empty word
		useLength[0].eq(1).post();
		// Process other lengths > 0
		for (int i = 1; i < useLength.length; i++) {
			if(map.containsKey(i)) {
				model.max(useLength[i], map.get(i)).post();
			} else {
				useLength[i].eq(0).post();
			}
		}	
	}

	@Override
	protected void postMaxLengthConstraints() {
		postUseLengthConstraints();
		model.setBoolsChanneling(useLength, setLength).post();
		model.max(setLength, maxLength, false).post();		
	}

	public static <T> Map<T, BoolVar[]> copy(Map<T, List<BoolVar>> map) {
		Map<T, BoolVar[]> dest = new HashMap<>(map.size());
		for (Map.Entry<T, List<BoolVar>> entry : map.entrySet()) {
			dest.put(entry.getKey(), toArray(entry.getValue()));
		}
		return dest;
	}

	public Map<Integer, BoolVar[]> buildWordsByLength() {
		final Map<Integer, List<BoolVar>> map = new HashMap<>();
		for (int i = 0; i < words.length; i++) {
			final Collection<BoolVar> list = map.computeIfAbsent(words[i].length(), s -> new ArrayList<>());
			list.add(this.vwords[i]);
		}

		return copy(map);
	}			



}