package com.kdsuneraavinash.xobotapp.camera;

import org.opencv.android.CameraBridgeViewBase;

public interface ICamera {
    void start(CameraBridgeViewBase view);

    void pause();

    void resume();

    void attachImageProcessor(ImageProcessor processor);
}
