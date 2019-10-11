void display_mode() {
  lcd.clear();
  if (mode == 1) {
    lcd.setCursor(0, 0);
    lcd.print("Play Mode");
  } else {
    lcd.setCursor(0, 0);
    lcd.print("Position " + String(mode - 1));
    if (set_mode == true) {
      lcd.setCursor(0, 1);
      angle = map(analogRead(A3), 0, 1023, 0, 180);
      if (rotate_servo == true) {
        if (servo_num == 1) {
          servo1.write(angle);
          delay(15);
        } else if (servo_num == 2) {
          servo2.write(angle);
          delay(15);
        } else if (servo_num == 3) {
          servo3.write(angle);
          delay(15);
        } else if (servo_num == 4) {
          servo4.write(angle);
          delay(15);
        }
      }
      lcd.print("Servo " + String(servo_num) + " " + servo_val(angle)); //
    }
  }
  if (edit_move == true) {
    lcd.setCursor(15, 0);
    lcd.print("*");
  }
}
