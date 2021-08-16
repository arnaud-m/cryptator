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

public class CryptaHeapSolver extends AbstractCryptaSolver implements ICryptaSolver {

    private long timeLimit = 0;
    private long solutionLimit = 0;

    private int[] comb;



    public CryptaHeapSolver() {
    }

    public ICryptaSolution findSolCrypta() throws Exception {
        HashMap<Character, GTVariable> map= (HashMap<Character, GTVariable>) getSolution().getSymbolToDigit();
//        if(map.size()> getConfig().getArithmeticBase() * getMaxOccurence()){
//            throw new Exception("too much different letter");
//        }
        while (comb!=null) {
            ICryptaSolution m=checkArray(comb);
            if(m!=null){

                return m;
            }
            int[] perm= Arrays.copyOf(comb, comb.length);
            m=heapPermutation(perm, map.size());
            if(m!=null){
                return m;
            }
            comb=nextCombination(comb);
        }
        return null;
    }

    // Generating permutation using Heap Algorithm
    public ICryptaSolution heapPermutation(int[] a, int size) {
        // if size becomes 1 then prints the obtained
        // permutation

        ICryptaSolution m = null;
        if (size == 1) {
            m=checkArray(a);
            return m;
        }

        for (int i = 0; i < size; i++) {
            if(m==null) {
                m = heapPermutation(a, size - 1);
            }

            // if size is odd, swap 0th i.e (first) and
            // (size-1)th i.e (last) element
            if (size % 2 == 1) {
                int temp = a[0];
                a[0]= a[size - 1];
                a[size - 1]= temp;
            }

            // If size is even, swap ith
            // and (size-1)th i.e last element
            else {
                int temp = a[i];
                a[i]= a[size - 1];
                a[size - 1]= temp;
            }
        }
        return m;
    }



    public int[] nextCombination(int[] combination) {
        int size=getSolution().getSymbolToDigit().size();
        int last = size - 1;

        int nbRep=getMaxOccurences()==0? 1: getMaxOccurences();
        while (last >= 0 && combination[last] == getConfig().getArithmeticBase() * nbRep - size + last) {
            last--;
        }

        if (last < 0) return null;

        combination[last]= combination[last]+1;

        for (int i = last + 1; i < size; i++) {
            combination[i]= combination[last] - last + i;
        }

        return combination;
    }


    @Override
    public boolean hasNext() {
        return comb!=null;
    }

    @Override
    public Integer next() {
        int nbSol=0;
        nbSol+=check(comb);

        int[] perm= Arrays.copyOf(comb, comb.length);
        nbSol+=heapPermutation3(perm, getSolution().getSymbolToDigit().size());

        comb=nextCombination(comb);
        return nbSol;
    }

    private int heapPermutation3(int[] a, int size) {
        int m = 0;
        if (size == 1) {
            m+=check(a);

            return m;
        }

        for (int i = 0; i < size; i++) {
            if(m==0) {
                m += heapPermutation3(a, size - 1);
            }

            // if size is odd, swap 0th i.e (first) and
            // (size-1)th i.e (last) element
            if (size % 2 == 1) {
                int temp = a[0];
                a[0]= a[size - 1];
                a[size - 1]= temp;
            }

            // If size is even, swap ith
            // and (size-1)th i.e last element
            else {
                int temp = a[i];
                a[i]= a[size - 1];
                a[size - 1]= temp;
            }
        }
        return m;
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

        comb=makeArray(getSolution().getSymbolToDigit().size());
        setSolutionConsumer(solutionConsumer);
        int solutionCount = 0;


        if(hasNext()) {
            if (solutionLimit > 0) {
                while (solutionCount < solutionLimit && hasNext()) {
                    solutionCount+=next();
                    System.out.println(solutionCount);

                }
            } else {
                while (hasNext()) {
                    solutionCount+=next();
                    System.out.println(solutionCount);

                }
            }
        }
        return solutionCount > 0;
    }
}
