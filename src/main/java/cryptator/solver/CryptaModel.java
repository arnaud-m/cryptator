package cryptator.solver;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;

public class CryptaModel {
    private Model model;
    private ArrayList<IntVar> map=new ArrayList<>();

    public CryptaModel(String s) {
        this.model = new Model(s);
    }


    public void addVar(IntVar var) {
        for (IntVar v: map){
            if (v.getName().equals(var.getName())){
                try {
                    v.updateBounds(Math.max(v.getLB(), var.getLB()), Math.min(v.getUB(), var.getUB()), v);
                }
                catch(Exception e){
                    System.out.println(e);
                }
                return;
            }
        }
        map.add(var);
    }

    IntVar getVar(String s) {
        for (IntVar var: map){
            if(var.getName().equals(s)){
                return var;
            }
        }
        return null;
    }



    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public ArrayList<IntVar> getMap() {
        return map;
    }

    public void setMap(ArrayList<IntVar> map) {
        this.map = map;
    }
}
