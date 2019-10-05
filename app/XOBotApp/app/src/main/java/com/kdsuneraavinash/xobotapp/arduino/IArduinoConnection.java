package com.kdsuneraavinash.xobotapp.arduino;


import android.app.Activity;

public abstract class IArduinoConnection {
    final Activity activity;

    protected IArduinoConnection(Activity activity) {
        this.activity = activity;
    }

    public abstract void onConnected();

    public abstract void onDisconnected();

    public abstract void onMessage(String message);

    public abstract void onError(String error);
}
