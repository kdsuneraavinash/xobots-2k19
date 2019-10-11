void servoTurn(int from, int to, int s_num) {
  if (from > to) {
    for (int i = from; i >= to; i--) {
      if (s_num == 1) {
        servo1.write(i);
        delay(s1_spd);
      } else if (s_num == 2) {
        servo2.write(i);
        delay(other_spd);
      } else if (s_num == 3) {
        servo3.write(i);
        delay(other_spd);
      } else if (s_num == 4) {
        servo4.write(i);
        delay(other_spd);
      } else {

      }
    }
  } else {
    for (int i = from; i <= to; i++) {
      if (s_num == 1) {
        servo1.write(i);
        delay(s1_spd);
      } else if (s_num == 2) {
        servo2.write(i);
        delay(other_spd);
      } else if (s_num == 3) {
        servo3.write(i);
        delay(other_spd);
      } else if (s_num == 4) {
        servo4.write(i);
        delay(other_spd);
      } else {

      }
    }
  }
}
