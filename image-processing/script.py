import numpy as np
import cv2
import algorithms
import math

cap = cv2.VideoCapture(0)


def reduce_lines(lines):
    gvf, angle_1, angle_2 = algorithms.jnb_algorithm_r2([p[1] for p in lines])
    if gvf == None:
        return
    if gvf < 0.8:
        # Bad detection - not a good split
        return
    if not 80 < abs(angle_1 - angle_2)*180/math.pi < 100:
        # Bad detection - angles are not perpendicular
        return

    gvf, avgs_1 = algorithms.jnb_algorithm_r4(
        [p[0] for p in lines if abs(p[1]-angle_1) < 0.2])

    if gvf < 0.8:
        return  # Bad detection - not a good split

    gvf, avgs_2 = algorithms.jnb_algorithm_r4(
        [p[0] for p in lines if abs(p[1]-angle_2) < 0.2])

    if gvf < 0.8:
        return  # Bad detection - not a good split

    print(avgs_1, avgs_2)


while(True):
    ret, frame = cap.read()
    grayscaled = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    blur = cv2.GaussianBlur(grayscaled, (5, 5), 0)
    threshed = cv2.adaptiveThreshold(
        blur, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY, 115, 11)
    edges = cv2.Canny(threshed, 100, 200)

    kernel = np.ones((5, 5), np.uint8)
    dilation = cv2.dilate(edges, kernel, iterations=1)
    erosion = cv2.erode(dilation, kernel, iterations=1)

    lines = cv2.HoughLines(erosion, 1, np.pi/180, 250)

    if lines is None:
        lines = []

    lines = np.reshape(lines, (-1, 2))

    drawn_img = np.copy(frame)
    for line in lines:
        rho, theta = line
        a = np.cos(theta)
        b = np.sin(theta)
        x0 = a*rho
        y0 = b*rho
        x1 = int(x0 + 1000*(-b))
        y1 = int(y0 + 1000*(a))
        x2 = int(x0 - 1000*(-b))
        y2 = int(y0 - 1000*(a))

        cv2.line(drawn_img, (x1, y1), (x2, y2), (0, 0, 255), 2)

    reduce_lines(lines)

    cv2.imshow('frame', erosion)
    cv2.imshow('lines drawn', drawn_img)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
