package com.kdsuneraavinash.xobotapp.minimax;

import com.kdsuneraavinash.xobotapp.camera.FinishedPlayer;

public interface IMinimaxGame {
    void markCellAsPlayerSymbol(int i, int j);

    void markCellAsEnemySymbol(int i, int j);

    int getBoardPrediction();

    FinishedPlayer getWinState();
}
