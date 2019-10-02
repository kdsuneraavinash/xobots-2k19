package com.kdsuneraavinash.xobotapp.joystick;

import org.opencv.core.Point;

public interface JoystickUpdateReceiver {
    void positionUpdate(Point point);
}
