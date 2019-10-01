package com.kdsuneraavinash.xobotapp.arduino;


public abstract class IArduinoConnection {
    public void onArduinoAttached() {
    }

    public void onArduinoDetached() {
    }

    public void onArduinoMessage(String message) {
    }

    public void onArduinoOpened() {
    }

    public void onUsbPermissionDenied() {
    }
}
