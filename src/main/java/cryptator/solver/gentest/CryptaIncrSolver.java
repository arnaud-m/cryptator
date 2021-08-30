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
import cryptator.tree.TreeTraversals;

import java.util.HashMap;
import java.util.function.Consumer;

public class CryptaIncrSolver extends AbstractCryptaSolver implements ICryptaSolver {
    private int[] elements;

    private long timeLimit = 0;

    private long solutionLimit = 0;

    public CryptaIncrSolver(ICryptaSolution solution) {
        super(solution);
    }

    public CryptaIncrSolver(){
        super();
    }





    public ICryptaSolution incrementalSolve(int[] elements, int base, int nbRep) throws Exception {

        if(getSolution().getSymbolToDigit().size()> base *nbRep){
            throw new Exception("to much different letter");
        }
        for(int i = (int) Math.pow(base,elements.length); i>0; i--) {
            elements[elements.length-1]= elements[elements.length-1]+1;
            for(int j=elements.length-1; j>=0;j--) {
                if (elements[j] == base && j - 1 >=0) {
                    elements[j]=0;
                    elements[j-1]= elements[j-1]+1;
                }
            }
            if(elements[0]== base){
                break;
            }
            if(checkNBrep(elements)) {
                    ICryptaSolution res = checkArray(elements);
                    if (res != null) {
                        return res;
                    }

            }
        }

        return null;
    }

    private int[] incrementalSolve2(int[] elements)  {
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
        incrementalSolve2(elements);
        return elements;
    }



    public final long getTimeLimit() {
        return timeLimit;
    }

    @Override
    public final void limitTime(long limit) {
        this.timeLimit = limit;
    }

    public final long getSolutionLimit() {
        return solutionLimit;
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

        System.out.println(solutionCount);
        return solutionCount > 0;
    }



    }