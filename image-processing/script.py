import numpy as np
import cv2
import algorithms
import math

cap = cv2.VideoCapture(0)


def reduce_lines(lines):
    gvf, avgs_1 = algorithms.jnb_algorithm_r4(lines)
    if gvf < 0.8:
        return []  # Bad detection - not a good split
    return avgs_1


while(True):
    ret, frame = cap.read()

    grayscaled = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    blur = cv2.GaussianBlur(grayscaled, (5, 5), 0)
    threshed = cv2.adaptiveThreshold(
        blur, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY, 115, 11)
    edges = cv2.Canny(threshed, 100, 200)

    kernel = np.ones((7, 7), np.uint8)
    dilation = cv2.dilate(edges, kernel, iterations=1)
    erosion = cv2.erode(dilation, kernel, iterations=1)

    line_thresh = 0.3
    x_lines = cv2.HoughLines(erosion, 1, np.pi/180, 250,
                             min_theta=math.pi/2-line_thresh, max_theta=math.pi/2+line_thresh)
    y_lines = cv2.HoughLines(erosion, 1, np.pi/180, 250,
                             min_theta=-line_thresh, max_theta=line_thresh)
    drawn_img = np.copy(frame)

    if x_lines is not None and len(x_lines) > 0:
        if y_lines is not None and len(y_lines) > 0:
            x_lines = np.reshape(x_lines, (-1, 2))
            x_theta = x_lines[0][1]
            x_intersects = reduce_lines([p[0] for p in x_lines])
            if len(x_intersects) == 0:
                continue

            y_lines = np.reshape(y_lines, (-1, 2))
            y_theta = y_lines[0][1]
            y_intersects = reduce_lines([p[0] for p in y_lines])
            if len(y_intersects) == 0:
                continue

            intersection_points = []
            for x_intersect in x_intersects:
                for y_intersect in y_intersects:
                    y = (y_intersect - x_intersect*np.tan(x_theta)) / \
                        (np.tan(y_theta) - np.tan(x_theta))
                    x = (x_intersect - y)*np.tan(x_theta)
                    intersection_points.append((x, y))

            for intersection_point in intersection_points:
                cv2.circle(drawn_img, intersection_point, 3, (0, 0, 255), -1)

    cv2.imshow('frame', erosion)
    cv2.imshow('lines drawn', drawn_img)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
