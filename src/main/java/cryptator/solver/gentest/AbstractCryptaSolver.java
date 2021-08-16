package cryptator.solver.gentest;

import cryptator.CryptaConfig;
import cryptator.specs.ICryptaEvaluation;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.tree.CryptaEvaluation;
import cryptator.tree.CryptaEvaluationException;
import cryptator.tree.TreeTraversals;

import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Consumer;

public abstract class AbstractCryptaSolver implements Iterator {
    private ICryptaNode root;
    private ICryptaSolution solution;
    private CryptaConfig config;
    private Consumer<ICryptaSolution> solutionConsumer;

    public AbstractCryptaSolver(ICryptaSolution solution) {
        this.solution = solution;
    }
    public AbstractCryptaSolver() {
    }


    public ICryptaSolution checkArray(int[] input) {
        int i=0;

        for (GTVariable var : ((HashMap<Character, GTVariable>) solution.getSymbolToDigit()).values()) {
            if(var.setValue(input[i]%config.getArithmeticBase())){
                i++;
            }
            else{
                return null;
            }
        }
        ICryptaEvaluation chk = new CryptaEvaluation();
        int v;
        try {
            v = chk.evaluate(root, solution, config.getArithmeticBase());
        } catch (CryptaEvaluationException e) {
            e.printStackTrace();
            v = -1;
        }

        return v == 1? solution: null;
    }

    public boolean checkNBrep(int[] elements) {
        for (int i=0; i<config.getArithmeticBase();i++){
            int rep = 0;
            for (int j=0; j<elements.length && rep<=getMaxOccurences(); j++){
                if (elements[j]==i) {
                    rep++;
                }
            }
            if((getMaxOccurences()>0 && rep >getMaxOccurences())
                    || (getMaxOccurences()==0 && rep > 1)
                    || (getMinOccurences()>0 && rep < getMinOccurences())) return false;
        }

        return true;
    }

    public int check(int[] elements){
        int nbSol=0;
        if (checkNBrep(elements)) {
            ICryptaSolution solution = checkArray(elements);
            if (solution != null) {
                solutionConsumer.accept(solution);

                nbSol += 1;
            }
        }
        return nbSol;
    }

    public ICryptaNode getRoot() {
        return root;
    }

    public void setRoot(ICryptaNode root) {
        this.root = root;
    }

    public ICryptaSolution getSolution() {
        return solution;
    }

    public void setSolution(ICryptaSolution solution) {
        this.solution = solution;
    }

    public CryptaConfig getConfig() {
        return config;
    }

    public void setConfig(CryptaConfig config) {
        this.config = config;
    }

    public Consumer<ICryptaSolution> getSolutionConsumer() {
        return solutionConsumer;
    }

    public void setSolutionConsumer(Consumer<ICryptaSolution> solutionConsumer) {
        this.solutionConsumer = solutionConsumer;
    }

    public int[] makeArray (int n) {

        int[] tab=new int[n];
        for (int i=0; i<n; i++){
            tab[i]=i;
        }
        return tab;
    }

    public HashMap<Character, GTVariable> makeMap(ICryptaNode root){
        HashMap<Character, GTVariable>map=new HashMap<>();
        TreeTraversals.postorderTraversal(root, (node, num) -> {
                    if(node.isLeaf()){
                        for (char c: node.getWord()) {
                            map.put(c, new GTVariable(Character.toString(c), 0, 0, getConfig().getArithmeticBase()));
                        }
                    }
                }
        );
        return map;
    }

    public int getMinOccurences(){
        int n=getSolution().getSymbolToDigit().size();
        final int b = config.getArithmeticBase();

        int minOcc = n/b;
        final int deltaMin = config.getRelaxMinDigitOccurence();
        if(deltaMin > 0) minOcc = Math.max(0, minOcc - deltaMin);
        return minOcc;
    }

    public int getMaxOccurences() {
        int n=getSolution().getSymbolToDigit().size();
        final int b = config.getArithmeticBase();

        int maxOcc = (n + b - 1) / b;
        final int deltaMax = config.getRelaxMaxDigitOccurence();
        if (deltaMax > 0) maxOcc = Math.min(n, maxOcc + deltaMax);
        return maxOcc;
    }
}
