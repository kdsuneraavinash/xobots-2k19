package com.kdsuneraavinash.xobotapp.camera;

public interface PredictionListener {
    void onPredictionUpdate(int prediction);

    void onFinishedUpdate(FinishedPlayer player);
}
