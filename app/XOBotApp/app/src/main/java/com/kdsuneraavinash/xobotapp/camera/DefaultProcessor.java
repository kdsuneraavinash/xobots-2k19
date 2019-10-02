package com.kdsuneraavinash.xobotapp.camera;

import org.opencv.core.Mat;

import static org.opencv.core.Core.ROTATE_90_CLOCKWISE;
import static org.opencv.core.Core.rotate;

class DefaultProcessor implements ImageProcessor{
    @Override
    public Mat process(Mat image) {
        rotate(image, image, ROTATE_90_CLOCKWISE);
        return image;
    }
}
