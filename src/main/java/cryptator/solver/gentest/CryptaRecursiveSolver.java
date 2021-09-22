package cryptator.solver.gentest;

import cryptator.CryptaConfig;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolverException;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ICryptaSolver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;

public class CryptaRecursiveSolver extends AbstractCryptaSolver implements ICryptaSolver {
    private long timeLimit = 0;
    private long solutionLimit = 0;
    int nb=0;

    public boolean checkNBrep(int[] elements, int e){
        int nbrep=0;
        for(int i=0; i<elements.length; i++){
            if(elements[i]==e){
                nbrep+=1;
            }
        }
        return nbrep <= getMaxOccurences();
    }


    public void generateSol(int[] elements){
        if(nb > solutionLimit){
            return;
        }
        if(elements.length>0 && !checkNBrep(elements, elements[elements.length-1])){
            return;
        }
        if(elements.length==getSolution().getSymbolToDigit().size()){
            nb += check(elements);
        }

        else{
            elements=Arrays.copyOf(elements, elements.length+1);
            for(int i=0; i<getConfig().getArithmeticBase(); i++){
                elements[elements.length-1]=i;
                generateSol(elements);
            }
        }

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
        int solutionCount = 0;

        if(!config.allowLeadingZeros()){
            findFirstLetter();
        }

        generateSol(new int[0]);
        solutionCount=nb;




        return solutionCount > 0 ;
    }


    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Object next() {
        return null;
    }
}
