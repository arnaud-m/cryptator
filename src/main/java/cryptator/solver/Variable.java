package cryptator.solver;

public class Variable {
    private char name;
    private int value=-1;
    private int valMin;
    private int valMax;

    public Variable(char name, int value, int valMin, int valMax) {
        this.name = name;
        this.value = value;
        this.valMin = valMin;
        this.valMax = valMax;
    }

    public char getName() {
        return name;
    }

    public void setName(char name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if(value<=valMax && value>=valMin) {
            this.value = value;
        }
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
