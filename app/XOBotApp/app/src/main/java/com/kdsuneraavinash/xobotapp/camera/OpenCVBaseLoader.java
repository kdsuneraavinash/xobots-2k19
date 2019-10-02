package com.kdsuneraavinash.xobotapp.camera;

import android.content.Context;
import android.util.Log;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;

class OpenCVBaseLoader extends BaseLoaderCallback {
    private static final String TAG = "OpenCVBaseLoader";
    private CameraBridgeViewBase cameraBridgeViewBase;

    OpenCVBaseLoader(Context appContext) {
        super(appContext);
    }

    void setCameraBridgeViewBase(CameraBridgeViewBase cameraBridgeViewBase) {
        this.cameraBridgeViewBase = cameraBridgeViewBase;
    }

    @Override
    public void onManagerConnected(int status) {
        if (status == LoaderCallbackInterface.SUCCESS) {
            Log.i(TAG, "OpenCV loaded successfully");
            cameraBridgeViewBase.enableView();
        } else {
            super.onManagerConnected(status);
        }
    }
}
