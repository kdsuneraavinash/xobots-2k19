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
