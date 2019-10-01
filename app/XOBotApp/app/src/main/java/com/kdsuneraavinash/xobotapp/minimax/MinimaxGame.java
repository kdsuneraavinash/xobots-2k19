package com.kdsuneraavinash.xobotapp.minimax;

public class MinimaxGame implements IMinimaxGame {
    private SymbolType[][] currentBoard = new SymbolType[3][3];
    private SymbolType playerSymbol;
    private SymbolType enemySymbol;

    public MinimaxGame(SymbolType playerSymbol) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                currentBoard[i][j] = SymbolType.NONE;
            }
        }

        if (playerSymbol == SymbolType.NONE) {
            throw new IllegalArgumentException("Player symbol must be X or O");
        }

        this.playerSymbol = playerSymbol;
        this.enemySymbol = (playerSymbol == SymbolType.O_SYMBOL) ? SymbolType.X_SYMBOL : SymbolType.O_SYMBOL;
    }

    private boolean isCellEmpty(int i, int j) {
        return currentBoard[i][j] == SymbolType.NONE;
    }

    private SymbolType getWinningSymbolOfThreeCells(int ai, int aj, int bi, int bj, int ci, int cj) {
        if (!isCellEmpty(ai, aj) && currentBoard[ai][aj] == currentBoard[bi][bj] && currentBoard[ai][aj] == currentBoard[ci][cj]) {
            return currentBoard[ai][aj];
        } else {
            return SymbolType.NONE;
        }
    }

    private SymbolType isEnd() {
        SymbolType winner;

        // Horizontal and vertical lines
        for (int i = 0; i < 3; i++) {
            winner = getWinningSymbolOfThreeCells(0, i, 1, i, 2, i);
            if (winner != SymbolType.NONE) {
                return winner;
            }
            winner = getWinningSymbolOfThreeCells(i, 0, i, 1, i, 2);
            if (winner != SymbolType.NONE) {
                return winner;
            }
        }

        // Diagonal Lines
        winner = getWinningSymbolOfThreeCells(0, 0, 1, 1, 2, 2);
        if (winner != SymbolType.NONE) {
            return winner;
        }
        winner = getWinningSymbolOfThreeCells(0, 2, 1, 1, 2, 0);
        if (winner != SymbolType.NONE) {
            return winner;
        }

        // Is board full
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (isCellEmpty(i, j)) {
                    return null;
                }
            }
        }
        return SymbolType.NONE;
    }

    private ResultMove maximize(int alpha, int beta) {
        int maxValue = -2;
        int pi = 0;
        int pj = 0;

        SymbolType winner = isEnd();
        if (winner == playerSymbol) {
            return new ResultMove(0, 0, 1);
        } else if (winner == enemySymbol) {
            return new ResultMove(0, 0, -1);
        } else if (winner == SymbolType.NONE) {
            return new ResultMove(0, 0, 0);
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (isCellEmpty(i, j)) {
                    currentBoard[i][j] = playerSymbol;
                    ResultMove resultMove = minimize(alpha, beta);
                    if (resultMove.getMinMaxValue() > maxValue) {
                        maxValue = resultMove.getMinMaxValue();
                        pi = i;
                        pj = j;
                    }
                    currentBoard[i][j] = SymbolType.NONE;

                    if (maxValue >= beta) {
                        return new ResultMove(pi, pj, maxValue);
                    }
                    if (maxValue > alpha) {
                        alpha = maxValue;
                    }
                }
            }
        }

        return new ResultMove(pi, pj, maxValue);
    }

    private ResultMove minimize(int alpha, int beta) {
        int minValue = 2;
        int pi = 0;
        int pj = 0;

        SymbolType winner = isEnd();
        if (winner == playerSymbol) {
            return new ResultMove(0, 0, 1);
        } else if (winner == enemySymbol) {
            return new ResultMove(0, 0, -1);
        } else if (winner == SymbolType.NONE) {
            return new ResultMove(0, 0, 0);
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (isCellEmpty(i, j)) {
                    currentBoard[i][j] = enemySymbol;
                    ResultMove resultMove = maximize(alpha, beta);
                    if (resultMove.getMinMaxValue() < minValue) {
                        minValue = resultMove.getMinMaxValue();
                        pi = i;
                        pj = j;
                    }
                    currentBoard[i][j] = SymbolType.NONE;

                    if (minValue <= alpha) {
                        return new ResultMove(pi, pj, minValue);
                    }
                    if (minValue < beta) {
                        beta = minValue;
                    }
                }
            }
        }

        return new ResultMove(pi, pj, minValue);
    }

    @Override
    public void markCellAsPlayerSymbol(int i, int j) {
        currentBoard[i][j] = playerSymbol;
    }

    @Override
    public void markCellAsEnemySymbol(int i, int j) {
        currentBoard[i][j] = enemySymbol;
    }

    @Override
    public int getBoardPrediction() {
        ResultMove resultMove = maximize(-2, 2);
        return resultMove.getiPos() * 3 + resultMove.getjPos();
    }

    public SymbolType[][] getCurrentBoard() {
        return currentBoard;
    }

    public SymbolType getPlayerSymbol() {
        return playerSymbol;
    }

    public SymbolType getEnemySymbol() {
        return enemySymbol;
    }
}
