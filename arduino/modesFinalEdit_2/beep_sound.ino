void beep() {
  digitalWrite(A2, HIGH);
  delay(50);
  digitalWrite(A2, LOW);
  delay(50);
}
void beep_long() {
  digitalWrite(A2, HIGH);
  delay(300);
  digitalWrite(A2, LOW);
  delay(50);
}
void beep_won() {
  beep();
  beep();
  beep_long();
  beep();
  beep();
  beep_long();
  beep();
  beep();
  beep_long();
  beep();
  beep();
  beep();
  beep();
  beep_long();
}
