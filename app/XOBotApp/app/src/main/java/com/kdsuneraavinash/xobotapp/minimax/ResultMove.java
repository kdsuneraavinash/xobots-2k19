package com.kdsuneraavinash.xobotapp.minimax;

class ResultMove {
    private int iPos;
    private int jPos;
    private int minMaxValue;

    ResultMove(int iPos, int jPos, int minMaxValue) {
        this.iPos = iPos;
        this.jPos = jPos;
        this.minMaxValue = minMaxValue;
    }

    int getiPos() {
        return iPos;
    }

    int getjPos() {
        return jPos;
    }

    int getMinMaxValue() {
        return minMaxValue;
    }
}
