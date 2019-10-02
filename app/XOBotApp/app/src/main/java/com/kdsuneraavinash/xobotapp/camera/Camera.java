package com.kdsuneraavinash.xobotapp.camera;

import android.content.Context;
import android.view.SurfaceView;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

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
