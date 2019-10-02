package com.kdsuneraavinash.xobotapp.recognizer;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.Core.bitwise_or;
import static org.opencv.core.Core.inRange;


public class Recognizer {
    private static final int GRID_SIZE = 300;
    static final int SQUARE_SIZE = GRID_SIZE / 3;

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

    public Recognizer(Point cornerTL, Point cornerTR, Point cornerBL, Point cornerBR) {
        this.cornerTL = cornerTL;
        this.cornerTR = cornerTR;
        this.cornerBL = cornerBL;
        this.cornerBR = cornerBR;
    }

    private void drawDot(Mat image, Point position) {
        Imgproc.circle(image, position, 5, Colors.COLOR_YELLOW, -1);
    }

    public SymbolColor[][] identifyGrid(Mat image) {
        // Draw 4 points
        drawDot(image, cornerTL);
        drawDot(image, cornerTR);
        drawDot(image, cornerBL);
        drawDot(image, cornerBR);

        // Warp image
        Mat pts1 = new Mat(4, 1, CvType.CV_32FC2);
        Mat pts2 = new Mat(4, 1, CvType.CV_32FC2);
        pts1.put(0, 0, cornerTL.x, cornerTL.y, cornerTR.x, cornerTR.y, cornerBL.x, cornerBL.y, cornerBR.x, cornerBR.y);
        pts2.put(0, 0, 0f, 0f, GRID_SIZE, 0f, 0f, GRID_SIZE, GRID_SIZE, GRID_SIZE);
        Mat M = Imgproc.getPerspectiveTransform(pts1, pts2);
        Mat warped = new Mat();
        Imgproc.warpPerspective(image, warped, M, new Size(GRID_SIZE, GRID_SIZE));

        // Get HSV image
        Mat hsv = new Mat();
        Imgproc.cvtColor(warped, hsv, Imgproc.COLOR_RGB2HSV);

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
                gridImg[i][j] = new SquareCell(redMask, greenMask, warped, SQUARE_SIZE * i,
                        SQUARE_SIZE * (i + 1), SQUARE_SIZE * j, SQUARE_SIZE * (j + 1));

                gridImg[i][j].findColors();
                gridImg[i][j].drawSquareWithDetectedColors();
                grid[i][j] = gridImg[i][j].detectColor();
                Mat rowRange = image.rowRange(image.rows() - GRID_SIZE, image.rows());
                Mat cellRange = rowRange.colRange(image.cols() - GRID_SIZE, image.cols());
                warped.copyTo(cellRange);
                rowRange.release();
                cellRange.release();
                gridImg[i][j].release();
            }
        }

        warped.release();
        hsv.release();
        redMask.release();
        greenMask.release();
        redMask1.release();
        redMask2.release();
        pts1.release();
        pts2.release();
        M.release();

        return grid;
    }
}
