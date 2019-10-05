package com.kdsuneraavinash.xobotapp;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.jmedeisis.bugstick.Joystick;
import com.kdsuneraavinash.xobotapp.arduino.ArduinoBluetoothManager;
import com.kdsuneraavinash.xobotapp.arduino.ArduinoManager;
import com.kdsuneraavinash.xobotapp.arduino.IArduinoConnection;
import com.kdsuneraavinash.xobotapp.camera.Camera;
import com.kdsuneraavinash.xobotapp.camera.GameProcessor;
import com.kdsuneraavinash.xobotapp.camera.PointSelectionProcessor;
import com.kdsuneraavinash.xobotapp.camera.PredictionListener;
import com.kdsuneraavinash.xobotapp.joystick.JoystickController;
import com.kdsuneraavinash.xobotapp.recognizer.SymbolColor;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Point;

import java.util.List;

public class MainActivity extends Activity implements PredictionListener {
    ArduinoManager arduinoManager;
    Camera camera = new Camera(this);
    private PointSelectionProcessor pointSelectionProcessor;
    private GameProcessor gameProcessor;
    private JoystickController joystickRightBottom;

    private Joystick joystickTL;
    private Joystick joystickTR;
    private Joystick joystickBL;
    private Joystick joystickBR;
    private Button startButton;
    private Switch redSwitch;
    private TextView predictionText;
    private TextView arduinoText;

    State currentState;

    private void initView() {
        joystickTL = findViewById(R.id.joystick_tl);
        joystickTR = findViewById(R.id.joystick_tr);
        joystickBL = findViewById(R.id.joystick_bl);
        joystickBR = findViewById(R.id.joystick_br);
        startButton = findViewById(R.id.button_start);
        redSwitch = findViewById(R.id.switch_is_red);
        predictionText = findViewById(R.id.prediction_text);
        arduinoText = findViewById(R.id.text_arduino);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        arduinoManager = new ArduinoBluetoothManager(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        camera.start((CameraBridgeViewBase) findViewById(R.id.java_surface_view));

        initView();


        currentState = State.INIT;
        Point leftTop = new Point(514, 189);
        Point rightTop = new Point(931, 189);
        Point leftBottom = new Point(499, 525);
        Point rightBottom = new Point(1000, 520);
        JoystickController joystickLeftTop = new JoystickController(leftTop);
        JoystickController joystickLeftBottom = new JoystickController(leftBottom);
        JoystickController joystickRightTop = new JoystickController(rightTop);
        joystickRightBottom = new JoystickController(rightBottom);
        pointSelectionProcessor = new PointSelectionProcessor();
        pointSelectionProcessor.setLeftTop(leftTop);
        pointSelectionProcessor.setRightTop(rightTop);
        pointSelectionProcessor.setLeftBottom(leftBottom);
        pointSelectionProcessor.setRightBottom(rightBottom);
        gameProcessor = new GameProcessor(leftTop, rightTop, leftBottom, rightBottom, this);

        joystickLeftTop.attach(joystickTL);
        joystickRightTop.attach(joystickTR);
        joystickLeftBottom.attach(joystickBL);
        joystickRightBottom.attach(joystickBR);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleStartButtonOnPressed();
            }
        });
        arduinoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arduinoManager instanceof ArduinoBluetoothManager) {
                    final ArduinoBluetoothManager arduinoBluetoothManager = (ArduinoBluetoothManager) arduinoManager;
                    List<BluetoothDevice> devices = arduinoBluetoothManager.getPairedDevices();
                    String[] deviceAddresses = new String[devices.size()];
                    for (int i = 0; i < devices.size(); i++) {
                        deviceAddresses[i] = devices.get(i).getAddress() + " [" + devices.get(i).getName() + "]";
                    }
                    Alerts.showOptionDialog(MainActivity.this, "Select Bluetooth Device",
                            deviceAddresses, new OptionDialogResultListener() {
                                @Override
                                public void onOption(int option, String optionText) {
                                    arduinoText.setBackgroundColor(Color.YELLOW);
                                    arduinoText.setTextColor(Color.BLACK);
                                    arduinoText.setText(R.string.arduino_connection_status);
                                    arduinoBluetoothManager.connectToDevice(optionText.split(" ")[0]);
                                }
                            });
                }
            }
        });

        camera.attachImageProcessor(pointSelectionProcessor);
    }

    @Override
    protected void onStart() {
        super.onStart();
        arduinoManager.onStart(new ArduinoConnection());
    }

    @Override
    protected void onStop() {
        super.onStop();
        arduinoManager.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        arduinoManager.onActivityResult(requestCode, resultCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.pause();
        arduinoManager.onDestroy();
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

    private void handleStartButtonOnPressed() {
        switch (currentState) {
            case INIT:
                SymbolColor color = redSwitch.isChecked() ? SymbolColor.RED_COLOR : SymbolColor.GREEN_COLOR;
                gameProcessor.setColor(color);
                camera.attachImageProcessor(gameProcessor);
                changeJoysticksVisibility(View.INVISIBLE);
                startButton.setText(R.string.reset);
                currentState = State.STARTED;
                predictionText.setText(R.string.prediction);
                arduinoManager.send("Hello");
                break;
            case STARTED:
                camera.attachImageProcessor(pointSelectionProcessor);
                changeJoysticksVisibility(View.VISIBLE);
                startButton.setText(R.string.start);
                currentState = State.INIT;
                predictionText.setText(R.string.prediction);
                break;
            default:
        }
    }

    private void changeJoysticksVisibility(int visibility) {
        joystickTR.setVisibility(visibility);
        joystickTL.setVisibility(visibility);
        joystickBR.setVisibility(visibility);
        joystickBL.setVisibility(visibility);
    }

    @Override
    public void onPredictionUpdate(final int prediction) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                predictionText.setText("My Move: " + prediction);
            }
        });
    }

    private class ArduinoConnection extends IArduinoConnection {
        private ArduinoConnection() {
            super(MainActivity.this);
        }

        @Override
        public void onConnected() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    arduinoText.setBackgroundColor(Color.GREEN);
                    arduinoText.setTextColor(Color.WHITE);
                    arduinoText.setText(R.string.connected);
                }
            });
        }

        @Override
        public void onDisconnected() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    arduinoText.setBackgroundColor(Color.YELLOW);
                    arduinoText.setTextColor(Color.BLACK);
                    arduinoText.setText(R.string.disconnected);
                }
            });
        }

        @Override
        public void onMessage(final String message) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    arduinoText.setBackgroundColor(Color.BLUE);
                    arduinoText.setTextColor(Color.WHITE);
                    arduinoText.setText(message);
                }
            });
        }

        @Override
        public void onError(final String error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    arduinoText.setBackgroundColor(Color.RED);
                    arduinoText.setTextColor(Color.WHITE);
                    arduinoText.setText(error);
                }
            });
        }
    }
}
