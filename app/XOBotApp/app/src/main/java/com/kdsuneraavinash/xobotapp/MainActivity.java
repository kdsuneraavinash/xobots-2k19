package com.kdsuneraavinash.xobotapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.jmedeisis.bugstick.Joystick;
import com.kdsuneraavinash.xobotapp.arduino.ArduinoAdapter;
import com.kdsuneraavinash.xobotapp.arduino.IArduinoConnection;
import com.kdsuneraavinash.xobotapp.camera.Camera;
import com.kdsuneraavinash.xobotapp.camera.GameProcessor;
import com.kdsuneraavinash.xobotapp.camera.PointSelectionProcessor;
import com.kdsuneraavinash.xobotapp.camera.PredictionListener;
import com.kdsuneraavinash.xobotapp.joystick.JoystickController;
import com.kdsuneraavinash.xobotapp.recognizer.SymbolColor;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Point;

public class MainActivity extends Activity implements PredictionListener {
    ArduinoAdapter arduinoAdapter;
    Camera camera = new Camera(this);
    private final PointSelectionProcessor pointSelectionProcessor;
    private final GameProcessor gameProcessor;
    private final JoystickController joystickLeftTop;
    private final JoystickController joystickLeftBottom;
    private final JoystickController joystickRightTop;
    private final JoystickController joystickRightBottom;

    private Joystick joystickTL;
    private Joystick joystickTR;
    private Joystick joystickBL;
    private Joystick joystickBR;
    private Button startButton;
    private Switch redSwitch;
    private TextView predictionText;

    State currentState;

    MainActivity() {
        currentState = State.INIT;
        Point leftTop = new Point(514, 189);
        Point rightTop = new Point(931, 189);
        Point leftBottom = new Point(499, 525);
        Point rightBottom = new Point(1000, 520);
        joystickLeftTop = new JoystickController(leftTop);
        joystickLeftBottom = new JoystickController(leftBottom);
        joystickRightTop = new JoystickController(rightTop);
        joystickRightBottom = new JoystickController(rightBottom);
        pointSelectionProcessor = new PointSelectionProcessor();
        pointSelectionProcessor.setLeftTop(leftTop);
        pointSelectionProcessor.setRightTop(rightTop);
        pointSelectionProcessor.setLeftBottom(leftBottom);
        pointSelectionProcessor.setRightBottom(rightBottom);
        gameProcessor = new GameProcessor(leftTop, rightTop, leftBottom, rightBottom, this);
    }

    private void initView() {
        joystickTL = findViewById(R.id.joystick_tl);
        joystickTR = findViewById(R.id.joystick_tr);
        joystickBL = findViewById(R.id.joystick_bl);
        joystickBR = findViewById(R.id.joystick_br);
        startButton = findViewById(R.id.button_start);
        redSwitch = findViewById(R.id.switch_is_red);
        predictionText = findViewById(R.id.prediction_text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        arduinoAdapter = new ArduinoAdapter(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        camera.start((CameraBridgeViewBase) findViewById(R.id.java_surface_view));

        initView();

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

        camera.attachImageProcessor(pointSelectionProcessor);
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
        camera.pause();
        arduinoAdapter.onDestroy();
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
}
