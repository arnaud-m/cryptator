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
    private int[] elements;
    int nb=0;



    //    Extend <- function(x, min = 0, max =1, len = 4) {
//        print(x)
//        if(sum(tail(x, 2)) > 1) {
//            print("FAIL")
//            return(invisible(NULL))
//        }
//        if(length(x) == len) {
//        ## check solution
//
//            print(x)
//        }
//        else {
//            for(i in min:max) {
//                Extend( c(x, i), min, max, len)
//            }
//        }
//
//    }
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
        int solutionCount=0;
        if(nb > solutionLimit){
            return;
        }
        if(elements.length>0 && !checkNBrep(elements, elements[elements.length-1])){
            return;
        }
        if(elements.length==getSolution().getSymbolToDigit().size()){
            nb += check(elements);
//            if (a == 1) {
//                System.out.println("toto");
//            }
            //return a;
        }

        else{
            elements=Arrays.copyOf(elements, elements.length+1);
            for(int i=0; i<getConfig().getArithmeticBase(); i++){
                elements[elements.length-1]=i;
                generateSol(elements);
            }
        }
        //System.out.println(solutionCount);
        //return solutionCount;
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
        int solutionCount = 0;


        generateSol(new int[0]);
        solutionCount=nb;
        System.out.println(solutionCount);



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
