String servo_val(int val) {
  String str_val = (String)val;
  String spaces = "" ;
  for (int i = 0; i < (3 - str_val.length()); i++) {
    spaces += " ";
  }
  return  str_val + spaces;
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

String getString(int len, String str, int start) {
  String Word = "";
  for (int i = start; i < len + 1; i++) {
    Word += str[i];
  }
  return Word;
}

int strToInt(String str) {
  char val_e[4];
  str.toCharArray(val_e, 4);
  int val_g = 0;
  sscanf(val_e, "%d", &val_g);
  return val_g;
}


void low_spd() {
  s1_spd = 25;
  other_spd = 20;
}
void high_spd() {
  s1_spd = 15;
  other_spd = 10;
}
