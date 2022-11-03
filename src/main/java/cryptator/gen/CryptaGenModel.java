/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;

import cryptator.CryptaOperator;
import cryptator.specs.ICryptaNode;
import cryptator.tree.CryptaLeaf;
import cryptator.tree.CryptaNode;

public class CryptaGenModel extends WordsListModel {

	private final CryptaEqnMember left;

	private final CryptaEqnMember right;

	public CryptaGenModel(String[] words) {
		super(new Model("Generate"), words);		
		left = new CryptaEqnMember(model, words, "L_");
		right = new CryptaEqnMember(model, words, "R_");
		buildCoreModel();
	}


	private void postLeftOrRightMemberConstraints() {
		for (int i = 0; i < words.length; i++) {
			left.words[i].add(right.words[i]).eq(wordVariables[i]).post();
		}
	}

	
	private void postMemberMaxLenConstraint() {
		right.maxLength.ge(left.maxLength).post();
			
	}

	private void buildCoreModel() {
		postLeftOrRightMemberConstraints();
		postMemberMaxLenConstraint();
	}
	
	public void postMemberCardConstraints(int min, int max) {
		if(min > 1) left.wordCount.ge(min).post();
		else left.wordCount.ge(2).post();
		if(max >= min) left.wordCount.le(max).post();
		
		// TODO Option to relax the constraint (allow subtractions in the bignum model)
		// Would need to break reflexion symmetry
		right.wordCount.eq(1).post();
	}
	
	public void postLeftMinCardConstraints(int base) {
		IntVar diff = right.maxLength.sub(left.maxLength).intVar();
		final int n = words.length;
		int prod = base;
		int i = 2;
		while(prod <= n) {
			diff.ge(i).imp(left.wordCount.ge(prod)).post();
			prod *= base;
			i++;
		}
		diff.lt(i).post();
	}
	
	public void postRightMemberConstraint() {
		BoolVar[] vars = right.getWords();
		vars[vars.length - 1].eq(1).post();
	}
	
	public void postDoublyTrueConstraint(int lb) {
		final int n = words.length;
		final IntVar sum = model.intVar("SUM", lb, n - 1);
		
		final IntVar[] lvars = new IntVar[n+1];
		System.arraycopy(left.getWords(), 0, lvars, 0, n);
		lvars[n] = sum; 
		
		final IntVar[] rvars = new IntVar[n+1];
		System.arraycopy(right.getWords(), 0, rvars, 0, n);
		rvars[n] = sum; 
		
		final int[] coeffs = ArrayUtils.array(0, n);
		coeffs[n] = -1;
		
		model.scalar(lvars, coeffs, "=", 0).post();
		model.scalar(rvars, coeffs, "=", 0).post();
	}

	private ICryptaNode recordMember(CryptaEqnMember member) {
		BoolVar[] vars = member.getWords();
		ICryptaNode node = null;
		for (int i = 0; i < vars.length; i++) {
			if(vars[i].isInstantiatedTo(1)) {
				final CryptaLeaf leaf = new CryptaLeaf(words[i]);
				node = node == null ? leaf : new CryptaNode(CryptaOperator.ADD, node, leaf);
			}
		}
		return node;
	}
	
	public final ICryptaNode recordCryptarithm() {
		return new CryptaNode(CryptaOperator.EQ, recordMember(left), recordMember(right));
	}
	

}
