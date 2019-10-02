package com.kdsuneraavinash.xobotapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.kdsuneraavinash.xobotapp.arduino.ArduinoAdapter;
import com.kdsuneraavinash.xobotapp.arduino.IArduinoConnection;
import com.kdsuneraavinash.xobotapp.camera.Camera;

import org.opencv.android.CameraBridgeViewBase;

public class MainActivity extends Activity {
    ArduinoAdapter arduinoAdapter;
    Camera camera = new Camera(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arduinoAdapter = new ArduinoAdapter(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        camera.start((CameraBridgeViewBase) findViewById(R.id.java_surface_view));
    }

    @Override
    protected void onStart() {
        super.onStart();
        arduinoAdapter.onStart(new IArduinoConnection() {
            @Override
            public void onArduinoMessage(String message) {
                System.out.println("Captured: " + message);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.pause();
        arduinoAdapter.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera.resume();
    }
}
