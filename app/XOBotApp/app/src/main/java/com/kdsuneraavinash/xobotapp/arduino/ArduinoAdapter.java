package com.kdsuneraavinash.xobotapp.arduino;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.util.Log;

import me.aflak.arduino.Arduino;
import me.aflak.arduino.ArduinoListener;

public class ArduinoAdapter {
    private static final String TAG = "";
    private Arduino arduino;

    public ArduinoAdapter(Context context) {
        arduino = new Arduino(context);
    }

    public void onStart(final IArduinoConnection arduinoConnection) {
        arduino.setArduinoListener(new ArduinoListener() {
            @Override
            public void onArduinoAttached(UsbDevice device) {
                Log.d(TAG, "onArduinoAttached: Device attached");
                arduino.open(device);
                arduinoConnection.onArduinoAttached();
            }

            @Override
            public void onArduinoDetached() {
                Log.d(TAG, "onArduinoDetached: Device detached");
                arduinoConnection.onArduinoDetached();
            }

            @Override
            public void onArduinoMessage(byte[] bytes) {
                String message = new String(bytes);
                Log.d(TAG, "onArduinoMessage: (Received) " + message);
                arduinoConnection.onArduinoMessage(message);
            }

            @Override
            public void onArduinoOpened() {
                Log.d(TAG, "onArduinoOpened: Connection Opened");
                arduinoConnection.onArduinoOpened();
            }

            @Override
            public void onUsbPermissionDenied() {
                Log.d(TAG, "onUsbPermissionDenied: Permission Denied");
                arduino.reopen();
                arduinoConnection.onUsbPermissionDenied();
            }
        });
    }

    public void onDestroy() {
        arduino.unsetArduinoListener();
        arduino.close();
    }

    public void send(String message) {
        if (arduino.isOpened()) {
            Log.d(TAG, "send: (Sending) " + message);
            arduino.send(message.getBytes());
        } else {
            throw new IllegalStateException("Arduino device is closed.");
        }
    }
}
