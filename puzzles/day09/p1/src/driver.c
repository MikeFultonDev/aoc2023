#pragma linkage(HELLO, OS)

int main() {
  write(0, "Hello", 5);
  HELLO();
}
