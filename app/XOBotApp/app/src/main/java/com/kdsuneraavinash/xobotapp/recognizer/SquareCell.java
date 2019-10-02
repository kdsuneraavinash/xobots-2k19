package com.kdsuneraavinash.xobotapp.recognizer;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.Core.FONT_HERSHEY_SIMPLEX;

public class SquareCell implements ISquareCell {
    private static final int RED_COUNT_THRESH = 500;
    private static final int GREEN_COUNT_THRESH = 500;

    private final Point topLeft;
    private final Point bottomRight;
    private final Mat redMask;
    private final Mat greenMask;
    private final Mat image;
    private int greenCount;
    private int redCount;

    SquareCell(Point topLeft, Point bottomRight, Mat redMask, Mat greenMask, Mat warpedImage) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        this.redMask = redMask;
        this.greenMask = greenMask;
        this.image = warpedImage;
        greenCount = -1;
        redCount = -1;
    }

    private void drawSquare(Scalar color) {
        Imgproc.rectangle(image, new Point(topLeft.x + 5, topLeft.y + 5), new Point(bottomRight.x - 5, bottomRight.y - 5), color, 2);
    }

    private void type(String text, Point position, int yPadding) {
        Imgproc.putText(image, text, new Point(position.x + 10, position.y + 10 + yPadding), FONT_HERSHEY_SIMPLEX, 0.5, Colors.COLOR_BLACK, 2);
    }

    @Override
    public SymbolColor detectColor() {
        if (redCount > greenCount && redCount > RED_COUNT_THRESH) {
            return SymbolColor.RED_COLOR;
        } else if (greenCount > redCount && greenCount > GREEN_COUNT_THRESH) {
            return SymbolColor.GREEN_COLOR;
        } else {
            return SymbolColor.NO_COLOR;
        }
    }

    private void findRedColor() {
        if (redCount == -1) {
            redCount = 0;
            for (int r = (int) topLeft.y; r < bottomRight.y; r++) {
                for (int c = (int) topLeft.x; c < bottomRight.x; c++) {
                    if (redMask.get(r, c)[0] == 255) {
                        redCount++;
                    }
                }
            }
            type("Red " + redCount, topLeft, 20);
        }
    }

    private void findGreenColor() {
        if (greenCount == -1) {
            greenCount = 0;
            for (int r = (int) topLeft.y; r < bottomRight.y; r++) {
                for (int c = (int) topLeft.x; c < bottomRight.x; c++) {
                    if (greenMask.get(r, c)[0] == 255) {
                        greenCount++;
                    }
                }
            }
            type("Grn " + greenCount, topLeft, 50);
        }
    }

    @Override
    public void findColors() {
        findRedColor();
        findGreenColor();
    }

    @Override
    public void drawSquareWithDetectedColors() {
        SymbolColor symbolColor = detectColor();
        switch (symbolColor) {
            case RED_COLOR:
                drawSquare(Colors.COLOR_RED);
                break;
            case GREEN_COLOR:
                drawSquare(Colors.COLOR_GREEN);
                break;
            default:
                drawSquare(Colors.COLOR_WHITE);
        }
    }


}
