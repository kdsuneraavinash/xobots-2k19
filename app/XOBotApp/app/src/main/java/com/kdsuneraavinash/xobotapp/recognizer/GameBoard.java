package com.kdsuneraavinash.xobotapp.recognizer;

import com.kdsuneraavinash.xobotapp.minimax.SymbolType;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.Core.FONT_HERSHEY_SIMPLEX;

public class GameBoard {
    final private SymbolType[][] gameBoard;
    private SymbolColor playerColor;

    public GameBoard() {
        gameBoard = new SymbolType[3][3];
    }

    public void generateGameBoard(SymbolColor[][] colorBoard, SymbolColor playerColor) {
        this.playerColor = playerColor;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gameBoard[i][j] = convert(colorBoard[i][j]);
            }
        }
    }

    private SymbolType convert(SymbolColor color) {
        if (color == playerColor) {
            return SymbolType.X_SYMBOL;
        } else if (color == SymbolColor.NO_COLOR) {
            return SymbolType.NONE;
        } else {
            return SymbolType.O_SYMBOL;
        }
    }

    private void type(Mat image, String text, Point position) {
        Scalar color = (playerColor == SymbolColor.RED_COLOR) ? Colors.COLOR_RED : Colors.COLOR_GREEN;
        Imgproc.putText(image, text, position, FONT_HERSHEY_SIMPLEX, 2, color, 2);
    }

    public void drawBoard(Mat image) {
        int charW = 64;
        int charH = 64;

        Imgproc.rectangle(image, new Point(0, 0), new Point(charW * 3 + 40, charW * 3 + 20), Colors.COLOR_WHITE, -1);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                type(image, symbolToText(gameBoard[i][j]), new Point(charW * j + 20, charH * (i + 1)));
            }
        }
    }

    private String symbolToText(SymbolType symbol) {
        switch (symbol) {
            case X_SYMBOL:
                return "X";
            case O_SYMBOL:
                return "O";
            default:
                return "_";
        }
    }

    public SymbolType[][] getGameBoard() {
        return gameBoard;
    }
}
