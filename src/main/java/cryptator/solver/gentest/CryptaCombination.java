package cryptator.solver.gentest;

import cryptator.CryptaConfig;
import cryptator.specs.ICryptaSolution;

import java.util.Arrays;

public class CryptaCombination extends CryptaHeapSolver{
    private int[] combination;
    private ICryptaSolution solution;
    private CryptaConfig config;
    private int maxOcc;


    public CryptaCombination(int[] combination, ICryptaSolution solution, CryptaConfig config, int maxOcc) {
        this.combination=combination;
        this.solution=solution;
        this.config=config;
        this.maxOcc=maxOcc;
    }

    @Override
    public boolean hasNext() {
//        for (int i=0; i<combination.length; i++){
//            if(combination[i]!=config.getArithmeticBase()-combination.length+i){
//                System.out.println("true");
//                return true;
//            }
//        }
//        System.out.println("false");
//        System.out.println(Arrays.toString(combination));
//        return false;
        int size=solution.getSymbolToDigit().size();
        int last = size - 1;
        int nbRep=maxOcc==0? 1: maxOcc;

        while (last >= 0 && combination[last] >= config.getArithmeticBase() * nbRep - size + last) {
            last--;
        }
        return last >= 0;
    }

    @Override
    public int[] next() {
        int size=solution.getSymbolToDigit().size();
        int last = size - 1;

        int nbRep=maxOcc==0? 1: maxOcc;
        while (last >= 0 && combination[last] >= config.getArithmeticBase() * nbRep - size + last) {
            last--;
        }

        if (last < 0) return null;

        combination[last]= combination[last]+1;

        for (int i = last + 1; i < size; i++) {
            combination[i]= combination[last] - last + i;
        }
        return combination;
    }


}
