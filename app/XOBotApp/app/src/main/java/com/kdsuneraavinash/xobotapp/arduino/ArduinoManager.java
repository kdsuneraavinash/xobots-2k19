package com.kdsuneraavinash.xobotapp.arduino;

import android.content.Context;

public abstract class ArduinoManager {
    ArduinoManager(Context context) {
        System.out.println("ArduinoManager initialized with context: " + context);
    }

    public abstract void onStart(final IArduinoConnection arduinoConnection);

    public abstract void onDestroy();

    public abstract void onStop();

    public abstract void send(String message);

    public abstract void onActivityResult(int requestCode, int resultCode);
}
