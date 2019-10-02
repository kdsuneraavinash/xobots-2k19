package com.kdsuneraavinash.xobotapp.recognizer;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.Core.bitwise_or;
import static org.opencv.core.Core.inRange;
import static org.opencv.core.CvType.CV_16U;


public class Recognizer {
    private static final int GRID_SIZE = 300;

    private static final Scalar HSV_LOWER_GREEN = new Scalar(10, 100, 50);
    private static final Scalar HSV_UPPER_GREEN = new Scalar(100, 255, 255);
    private static final Scalar HSV_LOWER_RED_1 = new Scalar(0, 70, 50);
    private static final Scalar HSV_UPPER_RED_1 = new Scalar(10, 255, 255);
    private static final Scalar HSV_LOWER_RED_2 = new Scalar(170, 70, 50);
    private static final Scalar HSV_UPPER_RED_2 = new Scalar(180, 255, 255);

    private Point cornerTL;
    private Point cornerTR;
    private Point cornerBL;
    private Point cornerBR;
    private Mat image;

    public Recognizer(Mat image, Point cornerTL, Point cornerTR, Point cornerBL, Point cornerBR) {
        this.image = image;
        this.cornerTL = cornerTL;
        this.cornerTR = cornerTR;
        this.cornerBL = cornerBL;
        this.cornerBR = cornerBR;
    }

    private void drawDot(Mat image, Point position) {
        Imgproc.circle(image, position, 5, Colors.COLOR_YELLOW, -1);
    }

    public SymbolColor[][] identifyGrid() {
        // Resize
        Mat resized = new Mat();
        Imgproc.resize(image, resized, new Size(800, 600));

        // Draw 4 points
        drawDot(resized, cornerTL);
        drawDot(resized, cornerTR);
        drawDot(resized, cornerBL);
        drawDot(resized, cornerBR);

        // Warp image
        Mat pts1 = Mat.ones(4, 2, CV_16U);
        Mat pts2 = Mat.ones(4, 2, CV_16U);
        pts1.put(0, 0, cornerTL.x);
        pts1.put(0, 1, cornerTL.y);
        pts1.put(1, 0, cornerTR.x);
        pts1.put(1, 1, cornerTR.y);
        pts1.put(2, 0, cornerBL.x);
        pts1.put(2, 1, cornerBL.y);
        pts1.put(3, 0, cornerBR.x);
        pts1.put(3, 1, cornerBR.y);
        pts2.put(0, 0, 0);
        pts2.put(0, 1, 0);
        pts2.put(1, 0, GRID_SIZE);
        pts2.put(1, 1, 0);
        pts2.put(2, 0, 0);
        pts2.put(2, 1, GRID_SIZE);
        pts2.put(3, 0, GRID_SIZE);
        pts2.put(3, 1, GRID_SIZE);
        Mat M = Imgproc.getPerspectiveTransform(pts1, pts2);
        Mat warped = new Mat();
        Imgproc.warpPerspective(resized, warped, M, new Size(GRID_SIZE, GRID_SIZE));

        // Get HSV image
        Mat hsv = new Mat();
        Imgproc.cvtColor(warped, hsv, Imgproc.COLOR_BGR2HSV);

        // Define masks
        Mat greenMask = new Mat();
        Mat redMask1 = new Mat();
        Mat redMask2 = new Mat();
        Mat redMask = new Mat();
        inRange(hsv, HSV_LOWER_GREEN, HSV_UPPER_GREEN, greenMask);
        inRange(hsv, HSV_LOWER_RED_1, HSV_UPPER_RED_1, redMask1);
        inRange(hsv, HSV_LOWER_RED_2, HSV_UPPER_RED_2, redMask2);
        bitwise_or(redMask1, redMask2, redMask);

        SquareCell[][] gridImg = {{null, null, null}, {null, null, null}, {null, null, null}};
        SymbolColor[][] grid = {{SymbolColor.NO_COLOR, SymbolColor.NO_COLOR, SymbolColor.NO_COLOR},
                {SymbolColor.NO_COLOR, SymbolColor.NO_COLOR, SymbolColor.NO_COLOR},
                {SymbolColor.NO_COLOR, SymbolColor.NO_COLOR, SymbolColor.NO_COLOR}};

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gridImg[i][j] = new SquareCell(cornerTL, cornerBR, redMask, greenMask, warped);
                gridImg[i][j].findColors();
                gridImg[i][j].drawSquareWithDetectedColors();
                grid[i][j] = gridImg[i][j].detectColor();
            }
        }
        return grid;
    }
}
