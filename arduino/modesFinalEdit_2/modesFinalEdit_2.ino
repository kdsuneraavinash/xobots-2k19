#include <LiquidCrystal.h>
#include <Servo.h>

LiquidCrystal lcd(12, 11, 5, 4, 6, 7);
Servo servo1, servo2, servo3, servo4;

boolean start = true;
int take_pos = 10;
int angle = 0;
int mode = 1;
int servo_num = 1;
////////
int s1_spd = 0;
int other_spd = 0;
////////
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

String posData[13][20] = {{"1099", "2138", "3145"}, {"2138", "3145"}, {"1076", "2138", "3145"},
  {"1101", "2115", "3125"}, {"2112", "3123"}, {"1073", "2115", "3125"},
  {"1105", "3107", "2098"}, {"3107", "2094"}, {"1069", "3107", "2094"},
  {"1044", "3097", "2090"}, {"2082"}, {"1134", "3097", "2090"},  { "1167", "4100", "2130", "3046"}
};
String refPos[4] = {"1086", "2110", "3085", "4125"};

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
  //  low_spd();
  high_spd();

  turnToPos(0);
  delay(1000);
  turnToPos(13);
  delay(1000);

  Serial.begin(9600);
}

void loop() {
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
      String rec = Serial.readString();
      if (rec != "won") {
        beep();
        beep();

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
        Serial.println("1");
      }
      //      else if (rec == "won") {
      //        beep_won();
      //
      //        //        Serial.flush();
      //        while (1);
      //      } else if (rec == "draw") {
      //        for (int i = 0; i < 10; i++) {
      //          beep_long();
      //        }
      //        while (1);
      //      }
    }
  }
}
