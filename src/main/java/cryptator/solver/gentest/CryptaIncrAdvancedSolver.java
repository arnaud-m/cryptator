/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver.gentest;

import cryptator.CryptaConfig;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolverException;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ICryptaSolver;

import java.util.HashMap;
import java.util.function.Consumer;

public class CryptaIncrAdvancedSolver extends AbstractCryptaSolver implements ICryptaSolver {
    private int[] elements;

    private long timeLimit = 0;

    private long solutionLimit = 0;

    public CryptaIncrAdvancedSolver(ICryptaSolution solution) {
        super(solution);
    }

    public CryptaIncrAdvancedSolver(){
        super();
    }


    private int[] incrementalSolve(int[] elements)  {
        int base=getConfig().getArithmeticBase();

        elements[elements.length-1]= elements[elements.length-1]+1;
        int i=elements.length-1;
        while(elements[i] >= base){
            elements[i]= 0;
            if(i!=0) {
                elements[i - 1] = elements[i - 1] + 1;
                i-=1;
            }
        }

        return elements;
    }


    @Override
    public boolean hasNext() {
        for (int el: elements){
            if(el!=getConfig().getArithmeticBase()-1){
                return true;
            }
        }
        return false;
    }

    @Override
    public int[] next() {
        incrementalSolve(elements);
        return elements;
    }

    @Override
    public final void limitTime(long limit) {
        this.timeLimit = limit;
    }

    public final void limitSolution(long limit) {
        this.solutionLimit = limit;
    }

    @Override
    public boolean solve(ICryptaNode cryptarithm, CryptaConfig config, Consumer<ICryptaSolution> solutionConsumer) throws CryptaModelException, CryptaSolverException {
        setRoot(cryptarithm);
        setConfig(config);
        HashMap<Character, GTVariable> map = makeMap(cryptarithm);

        setSolution(new CryptaGTSolution(map));
        setSolutionConsumer(solutionConsumer);

        elements=makeArray(getSolution().getSymbolToDigit().size());
        int solutionCount = 0;

        if(hasNext()) {
            if (solutionLimit > 0) {
                while (solutionCount < solutionLimit && hasNext()) {
                    solutionCount+=check(next());

                }
            } else {
                while (hasNext()) {
                    solutionCount+=check(next());
                }
            }
        }

        return solutionCount > 0;
    }



}