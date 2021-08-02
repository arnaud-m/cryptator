/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver;

import cryptator.specs.ICryptaNode;
import cryptator.tree.TreeTraversals;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.expression.discrete.arithmetic.ArExpression;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;

public class SolverUtils {

    public static CryptaModel2 contraint(ICryptaNode root, CryptaModel2 model) {
        mapPostorder(root, model);

        contraintWordPostorder(root, model);


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
        System.out.println(arrayIntVarToString(model.getMap()));
        model.getModel().allDifferent(array).post();
        //new Constraint("global cardinality", new GlobalCardinality(array, rep, ocurences));


        //model.globalCardinality(array, rep, ocurences, false);
        comparator(root, model);
        return model;
    }

    public static void mapPostorder(ICryptaNode root, CryptaModel2 model) {
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

    public static void contraintWordPostorder(ICryptaNode root, CryptaModel2 model) {
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

    public static void comparator(ICryptaNode root, CryptaModel2 model) {
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


    public static ArExpression calcul(ICryptaNode root, CryptaModel2 model) {
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



    public static String arrayIntVarToString(ArrayList<IntVar> map) {
        StringBuilder sb=new StringBuilder();
        if(map!=null) {
            for (IntVar var : map) {
                sb.append(var);
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}