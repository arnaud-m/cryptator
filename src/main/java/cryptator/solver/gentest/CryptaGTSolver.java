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
import cryptator.specs.ICryptaEvaluation;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ICryptaSolver;
import cryptator.tree.CryptaEvaluation;
import cryptator.tree.CryptaEvaluationException;
import cryptator.tree.TreeTraversals;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.expression.discrete.arithmetic.ArExpression;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class CryptaGTSolver {
    private ICryptaNode root;


    public CryptaGTSolver(ICryptaNode root) {
        this.root = root;
    }

    public CryptaGTModel contraint(CryptaGTModel model) {
        mapPostorder(model);

        contraintWordPostorder(model);


        ArrayList<IntVar> allDif = new ArrayList<>();
        for(IntVar var: model.getMap()){
            if(var.getLB()!=var.getUB() && var.getName().length()==1){
                allDif.add(var);
            }
        }

        IntVar[] array = model.getMap().toArray(new IntVar[0]);
        int[] rep=new int[10];
        IntVar[] ocurences=new IntVar[10];

//        for (int i=0; i<10; i++){
//            rep[i]=i;
//            ocurences[i]= model.getModel().intVar("var_" + i, 0, 2);
//        }
        model.getModel().allDifferent(array).post();
        //new Constraint("global cardinality", new GlobalCardinality(array, rep, ocurences));


        //model.globalCardinality(array, rep, ocurences, false);
        comparator(model);
        return model;
    }

    public void mapPostorder(CryptaGTModel model) {
        Model m=model.getModel();
        TreeTraversals.postorderTraversal(root, (node, num) -> {
                    if (node.isLeaf()) {
                        char[] word = node.getWord();
                        model.addVar(m.intVar(String.valueOf(word[0]), 1, 9));
                        for (int i=1; i<word.length;i++) {
                            model.addVar(m.intVar(String.valueOf(word[i]), 0, 9));
                        }
                        StringBuilder s = new StringBuilder();
                        for (char c: word){
                            s.append(c);
                        }

                        model.addVar(m.intVar(s.toString(), 1, (int) Math.pow(10,word.length)-1));
                    }
                }
        );
    }

    public void contraintWordPostorder(CryptaGTModel model) {
        Model m=model.getModel();
        TreeTraversals.postorderTraversal(root, (node, num) -> {
                    if (node.isLeaf()) {
                        char[] word = node.getWord();
                        IntVar[] vars= new IntVar[word.length];
                        int[] coeffs= new int[word.length];

                        for(int i=0; i<word.length; i++){
                            vars[i]=model.getVar(String.valueOf(word[i]));
                            coeffs[i]= (int) Math.pow(10, word.length-i-1);
                        }

                        StringBuilder s = new StringBuilder();
                        for (char c: word){
                            s.append(c);
                        }
                        IntVar v=model.getVar(s.toString());
                        m.scalar(vars, coeffs, "=", v).post();
                    }
                }
        );
    }

    public void comparator(CryptaGTModel model) {
        switch (root.getOperator()){
            case EQ:
                calcul(root.getLeftChild(), model).eq(calcul(root.getRightChild(), model)).decompose().post();
                break;
            case NEQ:
                calcul(root.getLeftChild(), model).ne(calcul(root.getRightChild(), model)).decompose().post();
                break;
            case LT:
                calcul(root.getLeftChild(), model).lt(calcul(root.getRightChild(), model)).decompose().post();
                break;
            case GT:
                calcul(root.getLeftChild(), model).gt(calcul(root.getRightChild(), model)).decompose().post();
                break;
            case LEQ:
                calcul(root.getLeftChild(), model).le(calcul(root.getRightChild(), model)).decompose().post();
                break;
            case GEQ:
                calcul(root.getLeftChild(), model).ge(calcul(root.getRightChild(), model)).decompose().post();
                break;
        }

    }


    public ArExpression calcul(ICryptaNode root, CryptaGTModel model) {
        switch (root.getOperator()){
            case ADD:
                return calcul(root.getLeftChild(), model).add(calcul(root.getRightChild(), model));

            case SUB:
                return calcul(root.getLeftChild(), model).sub(calcul(root.getRightChild(), model));

            case MUL:
                return calcul(root.getLeftChild(), model).mul(calcul(root.getRightChild(), model));

            case DIV:
                return calcul(root.getLeftChild(), model).div(calcul(root.getRightChild(), model));

            case MOD:
                return calcul(root.getLeftChild(), model).mod(calcul(root.getRightChild(), model));

            case POW:
                return calcul(root.getLeftChild(), model).pow(calcul(root.getRightChild(), model));

            default:
                IntVar v=model.getVar(String.valueOf(root.getWord()));
                return v!=null? v: model.getModel().intVar("0", 0, 0, false);
        }
    }



    //checker

    private HashMap<Character, GTVariable> checkArray(ArrayList<Integer> input, HashMap<Character, GTVariable> map, int max) {
        int i=0;
        for (GTVariable var : map.values()) {
            if(var.setValue(input.get(i)%max)){
                i++;
            }
            else{
                return null;
            }
        }
        ICryptaEvaluation chk = new CryptaEvaluation();
        long v;
        try {
            v = chk.evaluate(root, new CryptaGTSolution(map), 10);
        } catch (CryptaEvaluationException e) {
            e.printStackTrace();
            v = -1;
        }
        return v == 1? map: null;
    }

    public HashMap<Character, GTVariable> explorationRecursive(ArrayList<Integer> elements,
                                                             HashMap<Character, GTVariable> map, int max, int nbRep) throws Exception {
        if(map.size()>max*nbRep){
            throw new Exception("to much different letter");
        }
        for(int i = (int) Math.pow(max,elements.size()); i>0; i--) {
            elements.set(elements.size()-1, elements.get(elements.size()-1)+1);
            for(int j=elements.size()-1; j>=0;j--) {
                if (elements.get(j) == max && j - 1 >=0) {
                    elements.set(j, 0);
                    elements.set(j-1, elements.get(j-1)+1);
                }
            }
            if(elements.get(0)==max){
                break;
            }
            int rep = 0;
            for (int k=0; k<elements.size(); k++) {
                for(int l=0; l<elements.size(); l++) {
                    if (elements.get(k).equals(elements.get(l)) && k != l) {
                        rep +=1;
                    }
                }
            }

            if(rep<=nbRep) {
                HashMap<Character, GTVariable> res = checkArray(elements, map, max);
                if (res != null) {
                    return res;
                }
            }
        }

        return null;
    }


    public ArrayList<Integer> nextCombination(ArrayList<Integer> combination, int length, int max) {
        int last = length - 1;

        while (last >= 0 && combination.get(last) == max - length + last) {
            last--;
        }

        if (last < 0) return null;

        combination.set(last, combination.get(last)+1);

        for (int i = last + 1; i < length; i++) {
            combination.set(i, combination.get(last) - last + i);
        }

        return combination;
    }

    // Generating permutation using Heap Algorithm
    public HashMap<Character, GTVariable> heapPermutation(ArrayList<Integer> a, int size, int n, HashMap<Character, GTVariable> map, int max) {
        // if size becomes 1 then prints the obtained
        // permutation
        HashMap<Character, GTVariable> m = null;
        if (size == 1) {
            m=checkArray(a, map, max);
            return m;
        }

        for (int i = 0; i < size; i++) {
            if(m==null) {
                m = heapPermutation(a, size - 1, n, map, max);
            }

            // if size is odd, swap 0th i.e (first) and
            // (size-1)th i.e (last) element
            if (size % 2 == 1) {
                int temp = a.get(0);
                a.set(0, a.get(size - 1));
                a.set(size - 1, temp);
            }

            // If size is even, swap ith
            // and (size-1)th i.e last element
            else {
                int temp = a.get(i);
                a.set(i, a.get(size - 1));
                a.set(size - 1, temp);
            }
        }
        return m;
    }

    public HashMap<Character, GTVariable> findSolCrypta(ArrayList<Integer> comb, int size, int max, HashMap<Character, GTVariable> map, int nbRep) throws Exception {
        if(map.size()>max*nbRep){
            throw new Exception("to much different letter");
        }
        while (comb!=null) {
            HashMap<Character, GTVariable> m=checkArray(comb, map, max);
            if(m!=null){

                return m;
            }
            ArrayList<Integer> perm=new ArrayList<>(comb);
            m=heapPermutation(perm, size, size, map, max);
            if(m!=null){
                return m;
            }
            comb=nextCombination(comb, size, max*nbRep);
        }
        return null;
    }


    public void setRoot(ICryptaNode root) {
        this.root = root;
    }

}