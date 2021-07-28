package cryptator.solver;




public class Variable {
    private String name;
    private int value=-1;
    private int valMin;
    private int valMax;

    public Variable(String name, int value, int valMin, int valMax) {
        this.name = name;
        try {
            int i = Integer.parseInt(String.valueOf(name));
            this.value = i;
            this.valMin = i;
            this.valMax = i;
        }catch (Exception e){
            this.value = value;
            this.valMin = valMin;
            this.valMax = valMax;
        }

    }

    public String getName() {
        return String.valueOf(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public boolean setValue(int value) {
        if(value<=valMax && value>=valMin) {
            this.value = value;
            return true;
        }
        return false;
    }

    public int getValMin() {
        return valMin;
    }

    public void setValMin(int valMin) {
        this.valMin = valMin;
    }

    public int getValMax() {
        return valMax;
    }

    public void setValMax(int valMax) {
        this.valMax = valMax;
    }
}