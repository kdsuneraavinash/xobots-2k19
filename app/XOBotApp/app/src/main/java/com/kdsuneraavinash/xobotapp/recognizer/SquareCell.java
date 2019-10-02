package com.kdsuneraavinash.xobotapp.recognizer;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.Core.FONT_HERSHEY_SIMPLEX;
import static org.opencv.core.Core.countNonZero;

public class SquareCell implements ISquareCell {
    private static final int RED_COUNT_THRESH = 500;
    private static final int GREEN_COUNT_THRESH = 500;

    private final Mat redMask;
    private final Mat greenMask;
    private final Mat image;
    private int greenCount;
    private int redCount;

    SquareCell(Mat redMask, Mat greenMask, Mat warpedImage, int rowStart, int rowEnd, int colStart, int colEnd) {
        Mat rowRange = warpedImage.rowRange(rowStart, rowEnd);
        this.image = rowRange.colRange(colStart, colEnd);
        rowRange.release();

        rowRange = redMask.rowRange(rowStart, rowEnd);
        this.redMask = rowRange.colRange(colStart, colEnd);
        rowRange.release();

        rowRange = greenMask.rowRange(rowStart, rowEnd);
        this.greenMask = rowRange.colRange(colStart, colEnd);
        rowRange.release();

        greenCount = -1;
        redCount = -1;
    }

    private void drawSquare(Scalar color) {
        Imgproc.rectangle(image, new Point(5, 5), new Point(Recognizer.SQUARE_SIZE - 5, Recognizer.SQUARE_SIZE - 5), color, 2);
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
        redCount = countNonZero(redMask);
        type("Red " + redCount, new Point(0, 0), 20);
    }

    private void findGreenColor() {
        greenCount = countNonZero(greenMask);
        type("Grn " + greenCount, new Point(0, 0), 50);
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

    void release(){
        image.release();
        redMask.release();
        greenMask.release();
    }
}
