package com.kdsuneraavinash.xobotapp.camera;

import org.opencv.core.Mat;

class DefaultProcessor implements ImageProcessor{
    @Override
    public Mat process(Mat image) {
        return image;
    }
}
