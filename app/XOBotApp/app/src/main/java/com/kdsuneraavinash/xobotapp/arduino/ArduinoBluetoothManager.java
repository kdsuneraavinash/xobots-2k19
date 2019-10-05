package com.kdsuneraavinash.xobotapp.arduino;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import java.util.List;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.interfaces.BluetoothCallback;
import me.aflak.bluetooth.interfaces.DeviceCallback;
import me.aflak.bluetooth.interfaces.DiscoveryCallback;

public class ArduinoBluetoothManager extends ArduinoManager {
    private static final String TAG = "ArduinoBluetoothManager";
    private Bluetooth arduino;

    public ArduinoBluetoothManager(Context context) {
        super(context);
        arduino = new Bluetooth(context);
        arduino.setBluetoothCallback(new BluetoothCallback() {
            @Override
            public void onBluetoothTurningOn() {
                Log.i(TAG, "onBluetoothTurningOn: Bluetooth turning on");
            }

            @Override
            public void onBluetoothOn() {
                Log.i(TAG, "onBluetoothOn: Bluetooth turned on");
            }

            @Override
            public void onBluetoothTurningOff() {
                Log.i(TAG, "onBluetoothTurningOff: Bluetooth turning off");
            }

            @Override
            public void onBluetoothOff() {
                Log.i(TAG, "onBluetoothOff: Bluetooth turned off");
            }

            @Override
            public void onUserDeniedActivation() {
                Log.i(TAG, "onUserDeniedActivation: User denied activation");
            }
        });

        arduino.setDiscoveryCallback(new DiscoveryCallback() {
            @Override
            public void onDiscoveryStarted() {
                Log.i(TAG, "onDiscoveryStarted: Discovery Started");
            }

            @Override
            public void onDiscoveryFinished() {
                Log.i(TAG, "onDiscoveryFinished: Discovery Finished");
            }

            @Override
            public void onDeviceFound(BluetoothDevice device) {
                Log.i(TAG, "onDeviceFound: " + describeDevice(device));
            }

            @Override
            public void onDevicePaired(BluetoothDevice device) {
                Log.i(TAG, "onDevicePaired: " + describeDevice(device));
            }

            @Override
            public void onDeviceUnpaired(BluetoothDevice device) {
                Log.i(TAG, "onDeviceUnpaired: " + describeDevice(device));
            }

            @Override
            public void onError(int errorCode) {
                Log.i(TAG, "onError: errorCode=" + errorCode);
            }
        });
    }

    @Override
    public void onStart(final IArduinoConnection arduinoConnection) {
        arduino.onStart();
        if (arduino.isEnabled()) {
            Log.i(TAG, "onStart: Bluetooth enabled");
            arduino.setDeviceCallback(new DeviceCallback() {
                @Override
                public void onDeviceConnected(BluetoothDevice device) {
                    Log.i(TAG, "onDeviceConnected: " + describeDevice(device));
                    arduinoConnection.onConnected();
                }

                @Override
                public void onDeviceDisconnected(BluetoothDevice device, String message) {
                    Log.i(TAG, "onDeviceDisconnected: " + describeDevice(device) + " MESSAGE: " + message);
                    arduinoConnection.onDisconnected();
                }

                @Override
                public void onMessage(byte[] bytes) {
                    String message = new String(bytes);
                    Log.i(TAG, "onMessage: " + message);
                    arduinoConnection.onMessage(message);
                }

                @Override
                public void onError(int errorCode) {
                    Log.i(TAG, "onError: errorCode=" + errorCode);
                    arduinoConnection.onError("Error: " + errorCode);
                }

                @Override
                public void onConnectError(BluetoothDevice device, String message) {
                    Log.i(TAG, "onConnectError: " + describeDevice(device) + " MESSAGE: " + message);
                    arduinoConnection.onError("ConnectError: " + describeDevice(device) + " MESSAGE: " + message);
                }
            });
            List<BluetoothDevice> devices = arduino.getPairedDevices();
            Log.i(TAG, "onStart: Paired Devices List Length=" + devices.size());
            for (int i = 0; i < devices.size(); i++) {
                Log.i(TAG, "onStart: Paired Devices List[" + i + "]=" + describeDevice(devices.get(i)));
            }
        } else {
            Log.i(TAG, "onStart: Bluetooth disabled... Asking permission.");
            arduino.showEnableDialog(arduinoConnection.activity);
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: Disconnecting");
        if (arduino.isConnected()){
            arduino.disconnect();
        }
    }

    @Override
    public void send(String message) {
        if (arduino.isConnected()) {
            Log.d(TAG, "send: (Sending) " + message);
            arduino.send(message);
        } else {
            Log.i(TAG, "send: Arduino device is not connected.");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode) {
        arduino.onActivityResult(requestCode, resultCode);
    }


    @Override
    public void onStop() {
        arduino.onStop();
    }

    private String describeDevice(BluetoothDevice device) {
        return "address=" + device.getAddress() +
                " name=" + device.getName() + " class=" + device.getBluetoothClass().toString();
    }

    public void connectToDevice(String address){
        arduino.connectToAddress(address);
    }

    public List<BluetoothDevice> getPairedDevices(){
        return arduino.getPairedDevices();
    }
}


