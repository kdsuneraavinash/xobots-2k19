import cv2
import numpy as np

COLOR_RED = (0, 0, 255)
COLOR_GREEN = (0, 255, 0)
COLOR_BLUE = (255, 0, 0)
COLOR_PURPLE = (255, 0, 255)
COLOR_WHITE = (255, 255, 255)
COLOR_BLACK = (0, 0, 0)
COLOR_YELLOW = (0, 211, 255)

SQUARE_NONE = 0
SQUARE_GREEN = 1
SQUARE_RED = 2


class Square:
    def __init__(self, image, red_mask, green_mask, top_left, bottom_right):
        self.top_left = top_left
        self.bottom_right = bottom_right
        self.red_mask = red_mask
        self.green_mask = green_mask
        self.image = warped
        self.green_count = None
        self.red_count = None

    def draw_square(self, color):
        cv2.rectangle(self.image, (self.top_left[0] + 5, self.top_left[1] + 5),
                      (self.bottom_right[0] - 5, self.bottom_right[1] - 5),
                      color, 2)

    def detect_color(self):
        if self.red_count > self.green_count and self.red_count > 500:
            return SQUARE_RED
            self.draw_square(COLOR_RED)
        elif self.green_count > self.red_count and self.green_count > 500:
            return SQUARE_GREEN
            self.draw_square(COLOR_GREEN)
        else:
            return SQUARE_NONE
            self.draw_square(COLOR_WHITE)

    def draw_square_with_detected_color(self):
        square_type = self.detect_color()
        if square_type == SQUARE_RED:
            self.draw_square(COLOR_RED)
        elif square_type == SQUARE_GREEN:
            self.draw_square(COLOR_GREEN)
        else:
            self.draw_square(COLOR_WHITE)

    def type(self, text, color, position):
        font = cv2.FONT_HERSHEY_SIMPLEX
        text_bl_corner = position
        font_scale = 0.5
        font_color = color
        line_type = 2
        cv2.putText(self.image, str(text),
                    text_bl_corner,
                    font,
                    font_scale,
                    font_color,
                    line_type)

    def select_region(self, image):
        return image[self.top_left[1]:  self.bottom_right[1], self.top_left[0]:self.bottom_right[0]]

    def find_red_color(self):
        image_region = self.select_region(self.image)
        red_mask_region = self.select_region(self.red_mask)

        self.red_count = len(image_region[red_mask_region == 255])
        self.type(f"Red: {self.red_count}", COLOR_BLACK,
                  (self.top_left[0] + 10,  self.top_left[1] + 30))

    def find_green_color(self):
        image_region = self.select_region(self.image)
        green_mask_region = self.select_region(self.green_mask)

        self.green_count = len(image_region[green_mask_region == 255])
        self.type(f"Grn: {self.green_count}", COLOR_BLACK,
                  (self.top_left[0] + 10,  self.top_left[1] + 60))

    def find_colors(self):
        self.find_red_color()
        self.find_green_color()


def draw_dot(image, position, color=(0, 0, 255)):
    cv2.circle(image, position, 5, color, -1)


url = 'http://192.168.8.100:8080/video'
cap = cv2.VideoCapture(url)

corner_tl = (140, 230)
corner_tr = (465, 190)
corner_bl = (180, 445)
corner_br = (580, 390)

grid_size = 300
square_size = grid_size//3

lower_green = np.array([10, 100, 50])
upper_green = np.array([100, 255, 255])
lower_red_1 = np.array([0, 70, 50])
upper_red_1 = np.array([10, 255, 255])
lower_red_2 = np.array([170, 70, 50])
upper_red_2 = np.array([180, 255, 255])

while(True):
    ret, frame = cap.read()
    frame = cv2.resize(frame, (800, 600))
    frame = cv2.rotate(frame, cv2.ROTATE_90_CLOCKWISE)

    draw_dot(frame, corner_tl, COLOR_YELLOW)
    draw_dot(frame, corner_tr, COLOR_YELLOW)
    draw_dot(frame, corner_bl, COLOR_YELLOW)
    draw_dot(frame, corner_br, COLOR_YELLOW)

    pts1 = np.float32([corner_tl, corner_tr,
                       corner_bl, corner_br])
    pts2 = np.float32([[0, 0], [grid_size, 0],
                       [0, grid_size], [grid_size, grid_size]])

    M = cv2.getPerspectiveTransform(pts1, pts2)
    warped = cv2.warpPerspective(frame, M, (grid_size, grid_size))

    hsv = cv2.cvtColor(warped, cv2.COLOR_BGR2HSV)

    green_mask = cv2.inRange(hsv, lower_green, upper_green)
    red_mask_1 = cv2.inRange(hsv, lower_red_1, upper_red_1)
    red_mask_2 = cv2.inRange(hsv, lower_red_2, upper_red_2)
    red_mask = cv2.bitwise_or(red_mask_1, red_mask_2)

    grid_img = [[None]*3 for _ in range(3)]
    grid = [[SQUARE_NONE]*3 for _ in range(3)]
    for xi in range(3):
        for yi in range(3):
            grid_img[xi][yi] = Square(warped, red_mask, green_mask,
                                      (square_size*xi, square_size*yi),
                                      (square_size*(xi+1), square_size*(yi+1)))
            grid_img[xi][yi].find_colors()
            grid_img[xi][yi].draw_square_with_detected_color()
            grid[xi][yi] = grid_img[xi][yi].detect_color()

    print(grid)

    if frame is not None:
        cv2.imshow('frame', frame)
        cv2.imshow('warped', warped)
    q = cv2.waitKey(1)
    if q == ord("q"):
        break


cap.release()
cv2.destroyAllWindows()
