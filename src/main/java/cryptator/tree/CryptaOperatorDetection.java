/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.tree;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import cryptator.CryptaOperator;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ITraversalNodeConsumer;

public final class CryptaOperatorDetection implements ITraversalNodeConsumer {

	private Set<CryptaOperator> supportedOperators;

	private final Set<CryptaOperator> unsupportedOperators = new HashSet<>();

	public CryptaOperatorDetection(CryptaOperator ...supportedOperators) {
		this.supportedOperators = new HashSet<>(Arrays.asList(supportedOperators));		
	}
	
	@Override
	public void accept(ICryptaNode node, int numNode) {
		final CryptaOperator op = node.getOperator();
		if(! supportedOperators.contains(op)) {
			unsupportedOperators.add(op);
		}
	}
	
	public boolean hasUnsupportedOperator() {
		return ! unsupportedOperators.isEmpty();
	}
	
	public Set<CryptaOperator> getUnsupportedOperator() {
		return Collections.unmodifiableSet(unsupportedOperators);
	}
	
}
