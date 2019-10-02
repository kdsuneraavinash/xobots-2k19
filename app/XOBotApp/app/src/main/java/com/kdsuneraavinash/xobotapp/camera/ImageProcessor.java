package com.kdsuneraavinash.xobotapp.camera;

import org.opencv.core.Mat;

public interface ImageProcessor {
    Mat process(Mat image);
}
