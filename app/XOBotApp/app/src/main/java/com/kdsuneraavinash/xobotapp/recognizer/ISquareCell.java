package com.kdsuneraavinash.xobotapp.recognizer;

public interface ISquareCell {
    void findColors();

    void drawSquareWithDetectedColors();

    SymbolColor detectColor();
}
