#include <LiquidCrystal.h>
#include <Servo.h>

LiquidCrystal lcd(12, 11, 5, 4, 6, 7);
Servo servo1, servo2, servo3, servo4;

int take_pos = 10;
int angle = 0;
int mode = 1;
int servo_num = 1;
unsigned long butDecTime = 0;
unsigned long butIncTime = 0;
unsigned long butSetTime = 0;
unsigned long butSETime = 0;
unsigned long butSendTime = 0;
unsigned long butSetPlayTime = 0;
int lastServoNum = 1;
int lastPos = 1;

boolean set_mode = false;
boolean set_play = false;
boolean edit_move = false;
boolean change_value = false;
boolean rotate_servo = false;

boolean butDecState = false;
boolean butDeclastState = false;

boolean butIncState = false;
boolean butInclastState = false;

boolean butSetState = false;
boolean butSetlastState = false;

boolean butSEState = false;
boolean butSElastState = false;

boolean butSendState = false;
boolean butSendlastState = false;

String posData[13][20] = {{"1083", "2138", "3145"}, {"2138", "3145"}, {"1060", "2138", "3145"},
  {"1085", "2115", "3125"}, {"2112", "3123"}, {"1057", "2115", "3125"},
  {"1089", "2098", "3107"}, {"3107","2094"}, {"1053", "2094", "3107"},
  {"1028", "3097", "2090"}, {"2082"}, {"1118", "3097", "2090"},  {"2157", "3020", "1005", "4100"}
};
String refPos[4] = {"1070", "2110", "3085", "4125"};

////////////////////////////
uint8_t sPreAngle[4] = {0, 0, 0, 0};

uint8_t first = 0;
uint8_t second = 0;
uint8_t third = 0;
uint8_t fourth = 0;
///////////////////////////

void setup() {

  pinMode(A8, INPUT_PULLUP);
  pinMode(A9, INPUT_PULLUP);//A9
  pinMode(A10, INPUT_PULLUP);//A8
  pinMode(A11, INPUT_PULLUP);
  pinMode(A12, INPUT_PULLUP);
  pinMode(A3, INPUT);
  pinMode(A2, OUTPUT);
  // pinMode(A11, OUTPUT);

  //attachInterrupt(digitalPinToInterrupt(3), save_edit, RISING);//19
  //digitalWrite(A11,HIGH);
  lcd.begin(16, 2);
  servo1.attach(8);
  servo2.attach(9);
  servo3.attach(10);
  servo4.attach(13);
  display_mode();
  turnToPos(0);
  delay(2000);
  turnToPos(13);
  delay(1000);
  //groupData();
  //  servo1.write(90);
  //  delay(200);
  //  servo2.write(90);
  //  delay(200);
  //  servo3.write(90);
  //  delay(200);
  //  while (1);

  Serial.begin(9600);
}

void loop() {
  //  String a = "002";
  //  Serial.println(strToInt(a));
  //  turnToPos(1);
  //  while (1);
  display_mode();
  delay(100);
  if (change_value == true) {

    boolean butIncValue = digitalRead(A10);//18
    boolean butDecValue = digitalRead(A12);//A9
    boolean butSetValue = digitalRead(A11);//A8
    boolean butSEValue = digitalRead(A9);
    //Decrement button
    if (butDecState != butDeclastState) {
      butDecTime = millis();
    }
    if ((millis() - butDecTime) > 200) {
      if (butDecState != butDecValue) {
        butDecState = butDecValue;
        if (butDecState == true) {
          decrease();
        }
      }
    }
    butDeclastState = butDecValue;

    //Increment button
    if (butIncState != butInclastState) {
      butIncTime = millis();
    }
    if ((millis() - butIncTime) > 200) {
      if (butIncState != butIncValue) {
        butIncState = butIncValue;
        if (butIncState == true) {
          increase();
        }
      }
    }
    butInclastState = butIncValue;

    //Set button
    if (butSetState != butSetlastState) {
      butSetTime = millis();
    }
    if ((millis() - butSetTime) > 200) {
      if (butSetState != butSetValue) {
        butSetState = butSetValue;
        if (butSetState == true) {
          set();
        }
      }
    }
    butSetlastState = butSetValue;

    //SE button
    if (butSEState != butSElastState) {
      butSETime = millis();
    }
    if ((millis() - butSETime) > 200) {
      if (butSEState != butSEValue) {
        butSEState = butSEValue;
        if (butSEState == true) {
          save_edit();
        }
      }
    }
    butSElastState = butSEValue;
  } else {
    //SE button
    boolean butSEValue = digitalRead(A9);
    if (butSEState != butSElastState) {
      butSETime = millis();
    }
    if ((millis() - butSETime) > 200) {
      if (butSEState != butSEValue) {
        butSEState = butSEValue;
        if (butSEState == true) {
          save_edit();
        }
      }
    }
    butSElastState = butSEValue;

    ///////////
    boolean butSendValue = digitalRead(A8);
    if (butSendState != butSendlastState) {
      butSendTime = millis();
    }
    if ((millis() - butSendTime) > 200) {
      if (butSendState != butSendValue) {
        butSendState = butSendValue;
        if (butSendState == true) {
          Serial.println("0");
        }
      }
    }
    butSendlastState = butSendValue;
    //////////

    if (Serial.available()) {
      beep();
      beep();
      String rec = Serial.readString();
      lcd.setCursor(15, 0);
      lcd.print(rec);
      backToCenter(13);
      turnToPos(take_pos);
      grab_ball();
      backToCenter(take_pos);
      turnToPos((int)rec[0] - 48 + 1);
      release_ball();
      backToCenter((int)rec[0] - 48 + 1);
      turnToPos(13);
      take_pos++;
      if (take_pos == 13) {
        take_pos = 10;
      }
    }
    //    delay(3000);
    //    turnToPos(10);
    //    grab_ball();
    //    backToCenter(10);
    //    turnToPos(7);
    //    release_ball();
    //    backToCenter(7);
    //    while (1);
    //    turnToPos(1);
    //    delay(2000);
    //    backToCenter(1);
    //    while(1);
    //    Serial.println('c');
    //    Serial.println(posData[1][i]);
    //    for (int i = 0; i < 20; i++) {
    //      Serial.println(posData[1][i]);
    //    }
    //    turnToPos(1);
    //    delay(2000);
    //    backToCenter(1);
    //    while (1);
    //turnToPos(1);

    //      //Serial.println();
    //      r++;
    //      if (r == 1) {
    //        //EEPROM.put(eepromWriteAddr, "12345");
    //      } else if (r == 2) {
    //        EEPROM.get(0, a);
    //        EEPROM.get(1, b);
    //        EEPROM.get(2, c);
    //        EEPROM.get(3, d);
    //        EEPROM.get(4, e);
    //      } else {
    //        Serial.print(a);
    //        Serial.print(b);
    //        Serial.print(c);
    //        Serial.print(d);
    //        Serial.println(e);
    //      }
    //    }
    //    int val = 255;
    //    String val_a = (String)(servo_num) + (String)val;
    //    char val_e[5];
    //
    //    val_a.toCharArray(val_e, 5);
    //    int val_g = 0;
    //    sscanf(val_e, "%d", &val_g);
    //    Serial.println(val_g);
  }
}
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

String servo_val(int val) {
  String str_val = (String)val;
  String spaces = "" ;
  for (int i = 0; i < (3 - str_val.length()); i++) {
    spaces += " ";
  }
  return  str_val + spaces;
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
    delay(3000);
    turnToPos(mode - 1);
    delay(2000);
    backToCenter(mode - 1);
  } else {
    if (edit_move == true) {
      angle = map(analogRead(A3), 0, 1023, 0, 180);
      String val_a = digit_change((mode - 1), 2) + (String)(servo_num) + digit_change(angle, 3);
      //Serial.println(val_a);
    }
    rotate_servo = true;
  }

}

String digit_change(int val, int count) {
  String str_val = (String)val;
  int len = str_val.length();
  String final_str = "";
  if (len < count) {
    for (int i = 0; i < count - len; i++) {
      final_str += "0";
    }
    final_str += str_val;
  } else {
    final_str = str_val;
  }
  return final_str;
}
void getSerialData() {
  String data;
  int last_pos = 1;
  int index_one = 0;
  int index_two = 0;

  //  String serialArray[200] = {"11180", "12120", "11030", "13130"};

  boolean repeat = true;
  Serial.println('g');
  while (repeat) {
    Serial.println('r');
    while (!Serial.available());
    while (Serial.available()) {
      data = Serial.readString();
      /////
      int pos = ((int)(data[0] + data[1]) - 48);
      if (last_pos != pos) {
        index_two = 0;
        last_pos = pos;
        posData[pos - 1][index_two] = getString(4, data, 2);
      } else {
        posData[pos - 1][index_two] = getString(4, data, 2);
        index_two++;
      }
      ////
      if (data == "f") {
        repeat = false;
      }
      Serial.flush();
    }
    lcd.setCursor(0, 0);
    lcd.print(data);
  }
}
String getString(int len, String str, int start) {
  String Word = "";
  for (int i = start; i < len + 1; i++) {
    Word += str[i];
  }
  return Word;
}
//void groupData() {
//  String data;
//  int last_pos = 1;
//  int index_one = 0;
//  int index_two = 0;
//  while (1) {
//    data = serialArray[index_one];
//    if (data == "f") {
//      break;
//    } else {
//      int pos = ((int)data[0] - 48);
//      if (last_pos != pos) {
//        index_two = 0;
//        last_pos = pos;
//        posData[pos - 1][index_two] = getString(4, data);
//      } else {
//        posData[pos - 1][index_two] = getString(4, data);
//        index_two++;
//      }
//    }
//    index_one++;
//  }
//}
void turnToPos(int num) {
  String val;
  int servoNum;
  int angle;
  sPreAngle[0] = strToInt(getString(3, refPos[0], 1));
  sPreAngle[1] = strToInt(getString(3, refPos[1], 1));
  sPreAngle[2] = strToInt(getString(3, refPos[2], 1));
  sPreAngle[3] = strToInt(getString(3, refPos[3], 1));
  if (num == 0) {
    for (int i = 0; i < sizeof(refPos) / sizeof(refPos[0]); i++) {
      val = refPos[i];
      servoNum = (int)(val[0]) - 48;
      if (first == 0) {
        first = servoNum;
      } else if (second == 0) {
        second = servoNum;
      } else if (third == 0) {
        third = servoNum;
      } else if (fourth == 0) {
        fourth = servoNum;
      }
      angle = strToInt(getString(3, val, 1));
      //      Serial.println(angle);
      if (servoNum == 1) {
        servo1.write(angle);
        delay(25);
      } else if (servoNum == 2) {
        servo2.write(angle);
        delay(20);
      } else if (servoNum == 3) {
        servo3.write(angle);
        delay(20);
      } else if (servoNum == 4) {
        servo4.write(angle);
        delay(20);
      }
    }
  } else {
    for (int i = 0; i < sizeof(posData[num - 1]) / sizeof(posData[num - 1][0]); i++) {
      val = posData[num - 1][i];
      servoNum = (int)(val[0]) - 48;
      if (first == 0) {
        first = servoNum;
      } else if (second == 0) {
        second = servoNum;
      } else if (third == 0) {
        third = servoNum;
      } else if (fourth == 0) {
        fourth = servoNum;
      }
      angle = strToInt(getString(3, val, 1));
      if (servoNum == 1) {
        servoTurn(sPreAngle[0], angle, 1);
        sPreAngle[0] = angle;
      } else if (servoNum == 2) {
        servoTurn(sPreAngle[1], angle, 2);
        sPreAngle[1] = angle;
      } else if (servoNum == 3) {
        servoTurn(sPreAngle[2], angle, 3);
        sPreAngle[2] = angle;
      } else if (servoNum == 4) {
        servoTurn(sPreAngle[3], angle, 4);
        sPreAngle[3] = angle;
      }
    }
  }
}
int strToInt(String str) {
  char val_e[4];
  str.toCharArray(val_e, 4);
  int val_g = 0;
  sscanf(val_e, "%d", &val_g);
  return val_g;
}
void servoTurn(int from, int to, int s_num) {
  if (from > to) {
    for (int i = from; i >= to; i--) {
      if (s_num == 1) {
        servo1.write(i);
        delay(20);
      } else if (s_num == 2) {
        servo2.write(i);
        delay(20);
      } else if (s_num == 3) {
        servo3.write(i);
        delay(20);
      } else if (s_num == 4) {
        servo4.write(i);
        delay(20);
      } else {

      }
    }
  } else {
    for (int i = from; i <= to; i++) {
      if (s_num == 1) {
        servo1.write(i);
        delay(20);
      } else if (s_num == 2) {
        servo2.write(i);
        delay(20);
      } else if (s_num == 3) {
        servo3.write(i);
        delay(20);
      } else if (s_num == 4) {
        servo4.write(i);
        delay(20);
      } else {

      }
    }
  }
}

void backToCenter(int num) {
  String val;
  int servoNum;
  int angle;

  for (int i = (sizeof(posData[num - 1]) / sizeof(posData[num - 1][0])) - 1; i >= 0; i--) {
    val = posData[num - 1][i];
    servoNum = (int)(val[0]) - 48;
    angle = strToInt(getString(3, val, 1));
    if (servoNum == 1) {
      servoTurn(sPreAngle[0], angle, 1);
      sPreAngle[0] = angle;
    } else if (servoNum == 2) {
      servoTurn(sPreAngle[1], angle, 2);
      sPreAngle[1] = angle;
    } else if (servoNum == 3) {
      servoTurn(sPreAngle[2], angle, 3);
      sPreAngle[2] = angle;
    } else if (servoNum == 4) {
      servoTurn(sPreAngle[3], angle, 4);
      sPreAngle[3] = angle;
    }
  }
  servoTurn(sPreAngle[fourth - 1], strToInt(getString(3, refPos[fourth - 1], 1)), fourth);
  servoTurn(sPreAngle[third - 1], strToInt(getString(3, refPos[third - 1], 1)), third);
  servoTurn(sPreAngle[second - 1], strToInt(getString(3, refPos[second - 1], 1)), second);
  servoTurn(sPreAngle[first - 1], strToInt(getString(3, refPos[first - 1], 1)), first);
  first = 0;
  second = 0;
  third = 0;
  fourth = 0;
}
void beep() {
  digitalWrite(A2, HIGH);
  delay(50);
  digitalWrite(A2, LOW);
  delay(50);
}
void grab_ball() {
  int servo_val = strToInt(getString(3, refPos[3], 1));
  servoTurn(servo_val, 100, 4);
}
void release_ball() {

  int servo_val = strToInt(getString(3, refPos[3], 1));
  servoTurn(100, servo_val, 4);
  beep();
}
