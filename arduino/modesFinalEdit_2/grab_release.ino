void grab_ball() {
  int servo_val = strToInt(getString(3, refPos[3], 1));
  servoTurn(servo_val, 100, 4);
}
void release_ball() {
  int servo_val = strToInt(getString(3, refPos[3], 1));
  servoTurn(100, servo_val, 4);
  beep();
}
