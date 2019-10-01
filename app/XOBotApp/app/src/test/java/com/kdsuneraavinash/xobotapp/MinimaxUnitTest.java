package com.kdsuneraavinash.xobotapp;

import com.kdsuneraavinash.xobotapp.minimax.IMinimaxGame;
import com.kdsuneraavinash.xobotapp.minimax.MinimaxGame;
import com.kdsuneraavinash.xobotapp.minimax.SymbolType;

import org.junit.Test;

import static org.junit.Assert.*;

public class MinimaxUnitTest {
    @Test
    public void minimaxCreation_arrayIsCorrect() {
        MinimaxGame minimaxGame = new MinimaxGame(SymbolType.X_SYMBOL);
        SymbolType[][] emptyBoard = new SymbolType[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                emptyBoard[i][j] = SymbolType.NONE;
            }
        }
        assertArrayEquals(emptyBoard, minimaxGame.getCurrentBoard());
    }

    @Test
    public void minimaxCreation_playerSymbolIsCorrect() {
        MinimaxGame minimaxGame = new MinimaxGame(SymbolType.X_SYMBOL);
        assertEquals(SymbolType.X_SYMBOL, minimaxGame.getPlayerSymbol());
    }

    @Test
    public void minimaxCreation_enemySymbolIsCorrect() {
        MinimaxGame minimaxGame = new MinimaxGame(SymbolType.X_SYMBOL);
        assertEquals(SymbolType.O_SYMBOL, minimaxGame.getEnemySymbol());
    }

    @Test
    public void minimaxPrediction_onlyOneMoveInitially() {
        MinimaxGame minimaxGame = new MinimaxGame(SymbolType.X_SYMBOL);
        int  position = minimaxGame.getBoardPrediction();
        assertTrue(0<=position && position<=8);
    }

    @Test
    public void minimaxPrediction_markingIsCorrect() {
        MinimaxGame minimaxGame = new MinimaxGame(SymbolType.X_SYMBOL);
        minimaxGame.markCellAsEnemySymbol(1, 0);
        minimaxGame.markCellAsPlayerSymbol(0, 2);
        SymbolType[][] board = minimaxGame.getCurrentBoard();
        assertEquals(SymbolType.O_SYMBOL, board[1][0]);
        assertEquals(SymbolType.X_SYMBOL, board[0][2]);
    }

    @Test
    public void minimaxPrediction_testGame1() {
        IMinimaxGame minimaxGame = new MinimaxGame(SymbolType.X_SYMBOL);
        minimaxGame.markCellAsPlayerSymbol(0, 0);
        minimaxGame.markCellAsPlayerSymbol(1, 0);
        minimaxGame.markCellAsEnemySymbol(1, 1);
        minimaxGame.markCellAsEnemySymbol(2, 2);
        minimaxGame.markCellAsEnemySymbol(1, 2);
        int prediction = minimaxGame.getBoardPrediction();
        assertEquals(2, prediction);
    }
}
