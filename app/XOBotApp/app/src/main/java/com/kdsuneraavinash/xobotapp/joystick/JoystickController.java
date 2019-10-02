package com.kdsuneraavinash.xobotapp.joystick;

import com.jmedeisis.bugstick.Joystick;
import com.jmedeisis.bugstick.JoystickListener;

import org.opencv.core.Point;

public class JoystickController implements JoystickListener {
    private static final float MAX_SPEED_DP_PER_S = 3f;
    volatile private double xVelocity = 0;
    volatile private double yVelocity = 0;
    private final Point position;

    public JoystickController(Point point) {
        position = point;

        Thread positionUpdater = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    double newPositionX = position.x + xVelocity;
                    double newPositionY = position.y - yVelocity;
                    newPositionX = newPositionX < 0 ? 0 : newPositionX;
                    newPositionY = newPositionY < 0 ? 0 : newPositionY;
                    position.x = newPositionX;
                    position.y = newPositionY;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        });
        positionUpdater.start();
    }

    @Override
    public void onDown() {
    }

    @Override
    public void onDrag(float degrees, float offset) {
        xVelocity = (float) Math.cos(degrees * Math.PI / 180f) * offset * MAX_SPEED_DP_PER_S;
        yVelocity = (float) Math.sin(degrees * Math.PI / 180f) * offset * MAX_SPEED_DP_PER_S;
    }

    @Override
    public void onUp() {
        xVelocity = 0;
        yVelocity = 0;
    }

    public void attach(Joystick joystick) {
        joystick.setJoystickListener(this);
    }
}
