/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.solver.gentest;


import java.util.ArrayList;

public class GTVariable {
    private String name;
    private int value=-1;
    private int valMin;
    private int valMax;

    public GTVariable(String name, int value, int valMin, int valMax) {
        this.name = name;
        this.value = value;
        this.valMin = valMin;
        this.valMax = valMax;
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
