int atoi(const char* num)
{
  int val = 0;
  int i=0;
  while (1) {
    char c = num[i++];
    if (c < '0' || c > '9') { 
      return val;
    }
    val = (val*10) + (c - '0');
  }
}
   
int main() {
  printf("%d\n", atoi("33"));
  return 0;
}
