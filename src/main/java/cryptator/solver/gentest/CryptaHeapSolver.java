package cryptator.solver.gentest;

import cryptator.CryptaConfig;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolverException;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ICryptaSolver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;

public class CryptaHeapSolver extends AbstractCryptaSolver implements ICryptaSolver {

    private long timeLimit = 0;
    private long solutionLimit = 0;

    private int[]comb;


    public CryptaHeapSolver() {
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

        comb=makeArray(getSolution().getSymbolToDigit().size());
        setSolutionConsumer(solutionConsumer);
        int solutionCount = 0;

        CryptaCombination c=new CryptaCombination(comb, getSolution(), getConfig(), getMaxOccurences());
        CryptaPermutation p=new CryptaPermutation(Arrays.copyOf(comb, comb.length));

        if(!config.allowLeadingZeros()){
            findFirstLetter();
        }



        int[] perm;
        solutionCount+=check(c.getCombination());

        if (solutionLimit > 0) {
            while (solutionCount < solutionLimit && p.hasNext()){
                perm=p.next();
                solutionCount+=check(perm);
            }
            while (solutionCount < solutionLimit && c.hasNext()) {
                while (solutionCount < solutionLimit && p.hasNext()){
                    perm=p.next();
                    solutionCount+=check(perm);
                }
                comb=c.next();
                solutionCount+=check(comb);
                p.setPermutation(Arrays.copyOf(comb, comb.length));
                p.setI(0);
            }
        } else {
            while (p.hasNext()){
                perm=p.next();
                solutionCount+=check(perm);
            }
            while (c.hasNext()) {
                while (p.hasNext()){
                    perm=p.next();
                    solutionCount+=check(perm);
                }
                comb=c.next();
                solutionCount+=check(comb);
                p.setPermutation(Arrays.copyOf(comb, comb.length));
                p.setI(0);
            }
        }

        return solutionCount > 0;
    }
}
