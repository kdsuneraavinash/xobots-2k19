void decrease() {
  beep();
  rotate_servo = false;
  if (set_mode == false) {
    if (mode <= 1) {
      mode = 1;
    } else {
      mode--;
    }
  } else {
    if (servo_num <= 1) {
      servo_num = 1;
    } else {
      servo_num--;
    }
  }
}

void increase() {
  rotate_servo = false;
  if (set_mode == false) {
    if (mode >= 14) {
      mode = 14;
    } else {
      mode++;
    }
  } else {
    if (servo_num >= 4) {
      servo_num = 4;
    } else {
      servo_num++;
    }

  }
  beep();
}

void set() {
  if (set_mode == false) {
    set_mode = true;
    edit_move = false;
  } else if (set_mode == true && edit_move == true) {
    set_mode = false ;
    edit_move = false;
  } else {
    edit_move = true;
  }
  beep();
}

void save_edit() {
  beep();
  if (mode == 1) {
    if (change_value == false) {
      change_value = true;
    } else {
      change_value = false;
    }

  } else if (set_mode == false) {
    delay(1000);
    backToCenter(13);
    turnToPos(mode - 1);
    delay(1000);
    backToCenter(mode - 1);
    turnToPos(13);
    delay(1000);
  } else {
    if (edit_move == true) {
      angle = map(analogRead(A3), 0, 1023, 0, 180);
      String val_a = digit_change((mode - 1), 2) + (String)(servo_num) + digit_change(angle, 3);
      //Serial.println(val_a);
    }
    rotate_servo = true;
  }

}
