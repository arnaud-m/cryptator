package cryptator;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProductTest {

    private Model model;
    private IntVar copper, neon, iron, silver, result;

    @Before
    /***
     * Tests if choco can solve "copper*neon=iron*silver" via various way of writing constraints
     */
    public void setup(){
        model = new Model("copper*neon=iron*silver");
        copper = model.intVar("copper",100000,999999);
        neon = model.intVar("neon",1000,9999);
        iron = model.intVar("iron",1000,9999);
        silver = model.intVar("silver",100000,999999);
        model.allDifferent(new IntVar[]{copper,neon,iron,silver}).post();
    }
    
    public void setupResult(int factor) {
      //result is an intermediary to compare copper*neon and iron*silver
      //all test would fail if factor = 1.
        result = model.intVar("result",0, factor * IntVar.MAX_INT_BOUND);
    }
    
    public void solve(int solutionCount) {
        Solver solver = model.getSolver();
        System.out.println(model);
        solver.printVersion();
        solver.showStatistics();
        solver.showSolutions();
        solver.findSolution();
        Assert.assertEquals(solutionCount, solver.getSolutionCount());
    }
        

    @Test
    /***
     * Using times directly to make our constraint
     * will fail to find any solution because value needed is greater than the result's upper bound.
     */
    public void model1NoSol(){
        setupResult(1);
        model.times(copper, neon, result).post();
        model.times(silver, iron, result).post();
        solve(0);
    }
    
    @Test
    /***
     * Working version of the previous test.
     */
    public void model1(){
        setupResult(10);        
        model.times(copper, neon, result).post();
        model.times(silver, iron, result).post();
        solve(1);
    }
    @Test
    /***
     * Creating a model using arithm instead of times directly.
     */
    public void model2(){
        setupResult(10);        
        model.arithm(copper, "*",neon,"=",result).post();
        model.arithm(iron, "*",silver,"=",result).post();
        solve(1);
    }
    @Test
    /***
     * Creating a model using the operators function given by IntVar
     */
    public void model3(){
        setupResult(10);        
        copper.mul(neon).eq(result).post();
        iron.mul(silver).eq(result).post();
        solve(1);
    }
    @Test
    /***
     * Creating a model using the operators function given by IntVar without using result as an intermediary
     */
    public void model4(){
        copper.mul(neon).eq(iron.mul(silver)).post();
        solve(1);
    }
}