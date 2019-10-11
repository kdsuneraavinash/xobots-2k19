package com.kdsuneraavinash.xobotapp.arduino;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.util.Log;

import me.aflak.arduino.Arduino;
import me.aflak.arduino.ArduinoListener;

public class ArduinoUsbManager extends ArduinoManager {
    private static final String TAG = "ArduinoUsbManager";
    private Arduino arduino;

    public ArduinoUsbManager(Context context) {
        super(context);
        arduino = new Arduino(context);
    }

    @Override
    public void onStart(final IArduinoConnection arduinoConnection) {
        arduino.setArduinoListener(new ArduinoListener() {
            @Override
            public void onArduinoAttached(UsbDevice device) {
                Log.d(TAG, "onConnected: Device attached");
                arduino.open(device);
                arduinoConnection.onConnected();
            }

            @Override
            public void onArduinoDetached() {
                Log.d(TAG, "onDisconnected: Device detached");
                arduinoConnection.onDisconnected();
            }

            @Override
            public void onArduinoMessage(byte[] bytes) {
                String message = new String(bytes);
                Log.d(TAG, "onMessage: (Received) " + message);
                arduinoConnection.onMessage(message);
            }

            @Override
            public void onArduinoOpened() {
                Log.d(TAG, "onArduinoOpened: Connection Opened");
            }

            @Override
            public void onUsbPermissionDenied() {
                Log.d(TAG, "onError: Permission Denied");
                arduino.reopen();
                arduinoConnection.onError("Permission Denied");
            }
        });
    }

    @Override
    public void onDestroy() {
        arduino.unsetArduinoListener();
        arduino.close();
    }

    @Override
    public void onStop() {
    }

    @Override
    public void send(String message) {
        if (arduino.isOpened()) {
            Log.d(TAG, "send: (Sending) " + message);
            arduino.send(message.getBytes());
        } else {
            throw new IllegalStateException("Arduino device is closed.");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode) {
    }
}
