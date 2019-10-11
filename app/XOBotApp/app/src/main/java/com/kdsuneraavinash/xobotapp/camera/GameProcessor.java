package com.kdsuneraavinash.xobotapp.camera;

import com.kdsuneraavinash.xobotapp.minimax.MinimaxGame;
import com.kdsuneraavinash.xobotapp.minimax.SymbolType;
import com.kdsuneraavinash.xobotapp.recognizer.GameBoard;
import com.kdsuneraavinash.xobotapp.recognizer.Recognizer;
import com.kdsuneraavinash.xobotapp.recognizer.SymbolColor;

import org.opencv.core.Mat;
import org.opencv.core.Point;

public class GameProcessor implements ImageProcessor {
    final private Recognizer recognizer;
    final private PredictionListener listener;
    final private GameBoard gameBoard;
    private MinimaxGame minimaxGame;
    private SymbolColor color;

    public GameProcessor(Point leftTop, Point rightTop, Point leftBottom, Point rightBottom, PredictionListener listener) {
        this.listener = listener;
        recognizer = new Recognizer(leftTop, rightTop, leftBottom, rightBottom);
        gameBoard = new GameBoard();
        minimaxGame = new MinimaxGame(SymbolType.X_SYMBOL);
    }

    public void setColor(SymbolColor color) {
        this.color = color;
    }

    @Override
    public Mat process(Mat image) {
        SymbolColor[][] colorGrid = recognizer.identifyGrid(image);
        gameBoard.generateGameBoard(colorGrid, color);
        gameBoard.drawBoard(image);
        SymbolType[][] xoGrid = gameBoard.getGameBoard();
        minimaxGame.setCurrentBoard(xoGrid);
        onFinishedUpdate(minimaxGame.getWinState());
        predictionUpdate(minimaxGame.getBoardPrediction());
        return image;
    }

    private void predictionUpdate(int prediction){
        listener.onPredictionUpdate(prediction);
    }


    private void onFinishedUpdate(FinishedPlayer player){
        // On finished is always notified first
        listener.onFinishedUpdate(player);
    }


}
