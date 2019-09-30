import cv2
import numpy as np

COLOR_RED = (0, 0, 255)
COLOR_GREEN = (0, 255, 0)
COLOR_BLUE = (255, 0, 0)
COLOR_PURPLE = (255, 0, 255)


def draw_dot(image, position, color=(0, 0, 255)):
    cv2.circle(image, position, 5, color, -1)


url = 'http://192.168.8.100:8080/video'
cap = cv2.VideoCapture(url)

corner_tl = (140, 225)
corner_tr = (465, 185)
corner_bl = (180, 445)
corner_br = (580, 390)

while(True):
    ret, frame = cap.read()
    frame = cv2.resize(frame, (800, 600))
    frame = cv2.rotate(frame, cv2.ROTATE_90_CLOCKWISE)

    draw_dot(frame, corner_tl, COLOR_RED)
    draw_dot(frame, corner_tr, COLOR_GREEN)
    draw_dot(frame, corner_bl, COLOR_BLUE)
    draw_dot(frame, corner_br, COLOR_PURPLE)

    pts1 = np.float32([corner_tl, corner_tr, corner_bl, corner_br])
    pts2 = np.float32([[0, 0], [300, 0], [0, 300], [300, 300]])
    M = cv2.getPerspectiveTransform(pts1, pts2)
    dst = cv2.warpPerspective(frame, M, (300, 300))

    if frame is not None:
        cv2.imshow('frame', frame)
        cv2.imshow('warped', dst)
    q = cv2.waitKey(1)
    if q == ord("q"):
        break


cap.release()
cv2.destroyAllWindows()
