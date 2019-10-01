from kivy.app import App
from kivy.properties import StringProperty
from kivy.uix.widget import Widget
from kivy.clock import Clock
from minimax import Game, BOARD_EMPTY, BOARD_P1, BOARD_P2

ARE_YOU_PLAYER_1 = False


class XoBotGame(Widget):
    grid00 = StringProperty(BOARD_EMPTY)
    grid01 = StringProperty(BOARD_EMPTY)
    grid02 = StringProperty(BOARD_EMPTY)
    grid10 = StringProperty(BOARD_EMPTY)
    grid11 = StringProperty(BOARD_EMPTY)
    grid12 = StringProperty(BOARD_EMPTY)
    grid20 = StringProperty(BOARD_EMPTY)
    grid21 = StringProperty(BOARD_EMPTY)
    grid22 = StringProperty(BOARD_EMPTY)

    current_message = StringProperty("Play the game")

    def initialize_game(self):
        # * Change this to change the first player
        if self.is_processing:
            return
        is_ai_first_player = not ARE_YOU_PLAYER_1
        self.is_processing = True

        self.current_message = "Play the game"
        self.grid00 = BOARD_EMPTY
        self.grid01 = BOARD_EMPTY
        self.grid02 = BOARD_EMPTY
        self.grid10 = BOARD_EMPTY
        self.grid11 = BOARD_EMPTY
        self.grid12 = BOARD_EMPTY
        self.grid20 = BOARD_EMPTY
        self.grid21 = BOARD_EMPTY
        self.grid22 = BOARD_EMPTY

        if is_ai_first_player:
            self.player_symbol = BOARD_P1
            self.enemy_symbol = BOARD_P2
        else:
            self.player_symbol = BOARD_P2
            self.enemy_symbol = BOARD_P1

        self.game = Game(self.player_symbol, self.enemy_symbol)
        self.is_processing = False
        if is_ai_first_player:
            self.is_processing = True
            self.current_message = "First move by AI - processing"
            Clock.schedule_once(lambda x: self.process(), 0)

    def __init__(self, **kwargs):
        self.player_symbol = None
        self.enemy_symbol = None
        self.game = None
        self.is_processing = False
        super().__init__(**kwargs)
        Clock.schedule_once(lambda x: self.initialize_game(), 0)

    def is_finished(self):
        winner = self.game.is_end()
        if winner == self.player_symbol:
            self.current_message = "AI Won!"
            return True
        elif winner == self.enemy_symbol:
            self.current_message = r"You Won! \('o')/"
            return True
        elif winner == BOARD_EMPTY:
            self.current_message = "Game ended in a tie!"
            return True
        return False

    def get_current_board(self):
        return [[self.grid00, self.grid01, self.grid02],
                [self.grid10, self.grid11, self.grid12],
                [self.grid20, self.grid21, self.grid22]]

    def set_current_board(self, board):
        self.grid00 = board[0][0]
        self.grid01 = board[0][1]
        self.grid02 = board[0][2]
        self.grid10 = board[1][0]
        self.grid11 = board[1][1]
        self.grid12 = board[1][2]
        self.grid20 = board[2][0]
        self.grid21 = board[2][1]
        self.grid22 = board[2][2]
        self.game.set_board(board)

    def process(self):
        is_finished = self.is_finished()
        if is_finished:
            return
        self.is_processing = True

        current_grid = self.get_current_board()
        evaluation_time, (px, py), possible_outcome = \
            self.game.get_best_prediction(current_grid)
        self.current_message = f"Took {evaluation_time}s"
        current_grid[px][py] = self.player_symbol
        self.set_current_board(current_grid)

        self.is_processing = False
        is_finished = self.is_finished()
        if is_finished:
            return

    def pressed(self, number):
        if self.is_processing:
            return

        is_finished = self.is_finished()
        if is_finished:
            return
        self.is_processing = True

        xi = number % 3
        yi = number//3

        current_grid = self.get_current_board()
        if not self.game.is_valid(yi, xi):
            self.is_processing = False
            self.current_message = "Invalid position"
            return
        current_grid[yi][xi] = self.enemy_symbol
        self.set_current_board(current_grid)

        is_finished = self.is_finished()
        if is_finished:
            self.is_processing = False
            return

        self.current_message = "Processing"
        Clock.schedule_once(lambda x: self.process(), 1)


class XoBotApp(App):
    def build(self):
        return XoBotGame()


if __name__ == '__main__':
    XoBotApp().run()
