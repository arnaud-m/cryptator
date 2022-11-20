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
import cryptator.specs.ICryptaGenModel;
import cryptator.specs.ICryptaNode;
import cryptator.tree.CryptaNode;

public class CryptaGenModel extends WordsListModel {

	private final ICryptaGenModel left;

	private final ICryptaGenModel right;

	public CryptaGenModel(String[] words) {
		super(new Model("Generate"), words);		
		left = new CryptaMemberScalar(model, words, "L_");
		right = new CryptaMemberScalar(model, words, "R_");
		this.buildModel();
	}


	@Override
	public void buildModel() {
		super.buildModel();
		left.buildModel();
		right.buildModel();
		postLeftOrRightMemberConstraints();
		postMemberMaxLenConstraint();
	}
		

	private void postLeftOrRightMemberConstraints() {
		final BoolVar[] l = left.getWordVars();
		final BoolVar[] r = right.getWordVars();
		for (int i = 0; i < vwords.length; i++) {
			l[i].add(r[i]).eq(vwords[i]).post();
		}
	}

	
	private void postMemberMaxLenConstraint() {
		right.getMaxLength().ge(left.getMaxLength()).post();
			
	}

	public void postMemberCardConstraints(int min, int max) {
		if(min > 1) left.getWordCount().ge(min).post();
		else left.getWordCount().ge(2).post();
		// FIXMW why ?
		if(max >= min) left.getWordCount().le(max).post();
		
		// TODO Option to relax the constraint (allow subtractions in the bignum model)
		// Would need to break reflexion symmetry
		right.getWordCount().eq(1).post();
	}
	
	public void postLeftMinCardConstraints(int base) {
		IntVar diff = right.getMaxLength().sub(left.getMaxLength()).intVar();
		final int n = getN();
		int prod = base;
		int i = 2;
		while(prod <= n) {
			diff.ge(i).imp(left.getWordCount().ge(prod)).post();
			prod *= base;
			i++;
		}
		diff.lt(i).post();
	}
	
	public void postRightMemberConstraint() {
		final BoolVar[] vars = right.getWordVars();
		vars[vars.length - 1].eq(1).post();
	}

	public void postDoublyTrueConstraint(int lb) {
		final int n = getN();
		final IntVar sum = model.intVar("SUM", lb, n - 1);
		
		final IntVar[] lvars = new IntVar[n+1];
		System.arraycopy(left.getWordVars(), 0, lvars, 0, n);
		lvars[n] = sum; 
		
		final IntVar[] rvars = new IntVar[n+1];
		System.arraycopy(right.getWordVars(), 0, rvars, 0, n);
		rvars[n] = sum; 
		
		final int[] coeffs = ArrayUtils.array(0, n);
		coeffs[n] = -1;
		
		model.scalar(lvars, coeffs, "=", 0).post();
		model.scalar(rvars, coeffs, "=", 0).post();
	}

	public final ICryptaNode recordCryptarithm() {		
		// TODO Check that members are non-null. 
		return new CryptaNode(CryptaOperator.EQ, recordAddition(left), recordAddition(right));
	}


	@Override
	public String toString() {
		return left.toString() + " = " + right.toString();
	}
	
	
	
	

}
