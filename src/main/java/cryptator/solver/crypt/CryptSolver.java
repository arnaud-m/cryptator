package cryptator.solver.crypt;

import java.io.IOException;
import java.util.function.Consumer;

import cryptator.config.CryptaConfig;
import cryptator.solver.AbstractCryptaSolver;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolverException;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.tree.TreeUtils;

public class CryptSolver extends AbstractCryptaSolver {

    @Override
    public boolean solve(ICryptaNode cryptarithm, CryptaConfig config, Consumer<ICryptaSolution> consumer)
            throws CryptaModelException, CryptaSolverException {
        logOnCryptarithm(cryptarithm);
        logOnConfiguration(config);
        StringBuilder b = new StringBuilder();

        if (solutionLimit > 0) {
            b.append("limit ").append(solutionLimit).append("\n");
        }
        b.append(TreeUtils.writeInorder(cryptarithm)).append("\n");

        CryptExec crypt = new CryptExec("/home/nono/workspace/cryptator/src/main/benchmarks/crypt/crypt");
        try {
            crypt.exec(b.toString().getBytes(), System.out::println);
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

}
