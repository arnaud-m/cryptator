/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.tree;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;

import java.util.ArrayList;

public class MyPropagator  extends Propagator<IntVar> {
    ArrayList<IntVar> a;
    ArrayList<IntVar> b;
    int nbrep;

    public MyPropagator(ArrayList<IntVar> a, ArrayList<IntVar> b, int nbrep) {
        this.a = a;
        this.b = b;
        this.nbrep = nbrep;
    }


    @Override
    public void propagate(int evtmask) throws ContradictionException {

    }

    @Override
    public ESat isEntailed() {
        int i=0;
        for(IntVar var: a){
            for (IntVar var2: b){
                if(var!=var2 && var.getValue()==var2.getValue()){
                    i+=1;
                }
            }
        }
        return i<=nbrep? ESat.TRUE: ESat.FALSE;
    }
}
