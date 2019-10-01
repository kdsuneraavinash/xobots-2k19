package com.kdsuneraavinash.xobotapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.kdsuneraavinash.xobotapp.arduino.ArduinoAdapter;
import com.kdsuneraavinash.xobotapp.arduino.IArduinoConnection;

public class MainActivity extends AppCompatActivity {
    ArduinoAdapter arduinoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arduinoAdapter = new ArduinoAdapter(this);
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
        arduinoAdapter.onDestroy();
    }
}
