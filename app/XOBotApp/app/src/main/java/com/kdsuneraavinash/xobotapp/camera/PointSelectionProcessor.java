package com.kdsuneraavinash.xobotapp.camera;

import com.kdsuneraavinash.xobotapp.recognizer.Colors;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.Core.FONT_HERSHEY_SIMPLEX;

public class PointSelectionProcessor implements ImageProcessor {
    private Point leftTop;
    private Point rightTop;
    private Point leftBottom;
    private Point rightBottom;

    @Override
    public Mat process(Mat image) {
        drawLine(image, leftTop, rightTop);
        drawLine(image, leftTop, leftBottom);
        drawLine(image, rightBottom, leftBottom);
        drawLine(image, rightBottom, rightTop);
        drawDot(image, leftTop);
        drawDot(image, rightTop);
        drawDot(image, leftBottom);
        drawDot(image, rightBottom);
        type(image, leftTop, -10, -10);
        type(image, rightTop, 10, -10);
        type(image, leftBottom, -10, 10);
        type(image, rightBottom, 10, 10);
        return image;
    }

    private void type(Mat image, Point point, int xPadding, int yPadding) {
        Imgproc.putText(image, "{" + (int)point.x + ", " + (int)point.y + "}", new Point(point.x + xPadding, point.y + yPadding), FONT_HERSHEY_SIMPLEX, 1, Colors.COLOR_BLACK, 2);
    }

    private void drawDot(Mat image, Point position) {
        Imgproc.circle(image, position, 15, Colors.COLOR_BLACK, -1);
        Imgproc.circle(image, position, 10, Colors.COLOR_WHITE, -1);
    }

    private void drawLine(Mat image, Point start, Point end) {
        Imgproc.line(image, start, end, Colors.COLOR_BLACK, 4);
        Imgproc.line(image, start, end, Colors.COLOR_YELLOW, 2);
    }


    public void setLeftTop(Point leftTop) {
        this.leftTop = leftTop;
    }

    public void setRightTop(Point rightTop) {
        this.rightTop = rightTop;
    }

    public void setLeftBottom(Point leftBottom) {
        this.leftBottom = leftBottom;
    }

    public void setRightBottom(Point rightBottom) {
        this.rightBottom = rightBottom;
    }
}
