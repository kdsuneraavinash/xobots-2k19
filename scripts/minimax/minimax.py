import math
import time

BOARD_EMPTY = '.'
BOARD_P1 = 'X'
BOARD_P2 = 'O'


class Game:
    def __init__(self, player_1=BOARD_P1, player_2=BOARD_P2):
        self._current_state = [[BOARD_EMPTY]*3 for _ in range(3)]
        self._player_sign = player_1
        self._enemy_sign = player_2

    def draw_board(self):
        for i in range(3):
            for j in range(3):
                print(self._current_state[i][j], end="")
            print()
        print()

    def set_board(self, board):
        self._current_state = board

    def get_board(self):
        return self._current_state

    def is_cell_empty(self, i, j):
        return self._current_state[i][j] == BOARD_EMPTY

    def is_valid(self, px, py):
        if (not 0 <= px < 3) or (not 0 <= py < 3):
            return False
        elif not self.is_cell_empty(px, py):
            return False
        return True

    def get_winner_of_three_cells(self, a, b, c):
        if (not self.is_cell_empty(a[0], a[1])) and \
                self._current_state[a[0]][a[1]] == \
                self._current_state[b[0]][b[1]] == \
                self._current_state[c[0]][c[1]]:
            return True, self._current_state[a[0]][a[1]]
        else:
            return False, BOARD_EMPTY

    def is_end(self):
        for i in range(3):
            has_winner, winner = \
                self.get_winner_of_three_cells((0, i), (1, i), (2, i))
            if has_winner:
                return winner

            has_winner, winner = \
                self.get_winner_of_three_cells((i, 0), (i, 1), (i, 2))
            if has_winner:
                return winner

        has_winner, winner = \
            self.get_winner_of_three_cells((0, 0), (1, 1), (2, 2))
        if has_winner:
            return winner

        has_winner, winner = \
            self.get_winner_of_three_cells((0, 2), (1, 1), (2, 0))
        if has_winner:
            return winner

        for i in range(3):
            for j in range(3):
                if self.is_cell_empty(i, j):
                    return None

        return BOARD_EMPTY

    def maximize(self):
        max_value = -2
        px = py = None

        result = self.is_end()

        if result == self._player_sign:
            return (1, 0, 0)
        elif result == self._enemy_sign:
            return (-1, 0, 0)
        elif result == BOARD_EMPTY:
            return (0, 0, 0)

        for i in range(3):
            for j in range(3):
                if self.is_cell_empty(i, j):
                    self._current_state[i][j] = self._player_sign
                    (m, min_i, min_j) = self.minimize()
                    if m > max_value:
                        max_value = m
                        px = i
                        py = j
                    self._current_state[i][j] = BOARD_EMPTY

        return (max_value, px, py)

    def minimize(self):
        min_value = 2
        qx = qy = None

        result = self.is_end()

        if result == self._player_sign:
            return (1, 0, 0)
        elif result == self._enemy_sign:
            return (-1, 0, 0)
        elif result == BOARD_EMPTY:
            return (0, 0, 0)

        for i in range(3):
            for j in range(3):
                if self.is_cell_empty(i, j):
                    self._current_state[i][j] = self._enemy_sign
                    (m, max_i, max_j) = self.maximize()
                    if m < min_value:
                        min_value = m
                        qx = i
                        qy = j
                    self._current_state[i][j] = BOARD_EMPTY

        return (min_value, qx, qy)

    def check_end(self):
        self.draw_board()
        self.result = self.is_end()

        if self.result != None:
            if self.result == BOARD_P1:
                print('The winner is Player 1!')
            elif self.result == BOARD_P2:
                print('The winner is Player 2!')
            elif self.result == BOARD_EMPTY:
                print("It's a tie!")
            return True
        return False

    def get_best_prediction(self, board):
        self.set_board(board)
        start = time.time()
        (m, px, py) = self.maximize()
        end = time.time()
        return (end-start), (px, py), m

    def play(self):
        while True:
            if self.check_end():
                break

            # AI Move
            evaluation_time, (px, py), possible_outcome = self.get_best_prediction(
                self._current_state)
            self._current_state[px][py] = self._player_sign
            print(
                f'Evaluation time: {round(evaluation_time, 7)}s Result is {possible_outcome}')

            if self.check_end():
                break

            while True:
                px, py = list(
                    map(int, input('Insert the coordinate: ').strip().split()))

                if self.is_valid(px, py):
                    self._current_state[px][py] = self._enemy_sign
                    break
                else:
                    print('The move is not valid! Try again.')


if __name__ == "__main__":
    game = Game()
    game.play()
