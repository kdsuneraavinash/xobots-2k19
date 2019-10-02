package com.kdsuneraavinash.xobotapp.minimax;

public interface IMinimaxGame {
    void markCellAsPlayerSymbol(int i, int j);

    void markCellAsEnemySymbol(int i, int j);

    int getBoardPrediction();
}
