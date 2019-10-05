package com.kdsuneraavinash.xobotapp.camera;

import android.content.Context;
import android.view.SurfaceView;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.Core.ROTATE_90_CLOCKWISE;
import static org.opencv.core.Core.rotate;

public class Camera implements CameraBridgeViewBase.CvCameraViewListener2, ICamera {
    private OpenCVBaseLoader openCVBaseLoader;
    private CameraBridgeViewBase mOpenCvCameraView;
    private Context appContext;
    private ImageProcessor currentProcessor;

    public Camera(Context appContext) {
        this.appContext = appContext;
        openCVBaseLoader = new OpenCVBaseLoader(appContext);
        currentProcessor = new DefaultProcessor();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat image = inputFrame.rgba();
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGRA2BGR);
        Size size = image.size();
        rotate(image, image, ROTATE_90_CLOCKWISE);
        Imgproc.resize(image, image, size);
        return currentProcessor.process(image);
    }


    @Override
    public void start(CameraBridgeViewBase view) {
        mOpenCvCameraView = view;
        openCVBaseLoader.setCameraBridgeViewBase(mOpenCvCameraView);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void pause() {
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }

    @Override
    public void resume() {
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, appContext, openCVBaseLoader);
    }

    @Override
    public void attachImageProcessor(ImageProcessor processor) {
        currentProcessor = processor;
    }
}
