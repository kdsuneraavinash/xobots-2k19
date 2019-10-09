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
      //      if (first == 0) {
      //        first = servoNum;
      //      } else if (second == 0) {
      //        second = servoNum;
      //      } else if (third == 0) {
      //        third = servoNum;
      //      } else if (fourth == 0) {
      //        fourth = servoNum;
      //      }
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
