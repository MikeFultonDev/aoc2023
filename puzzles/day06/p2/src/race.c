#include <stdlib.h>
#include <stdio.h>

signed long T; 
signed long long calcDistance(signed long long startTime)
{
  return startTime * T - startTime * startTime;
}
signed long long mysearch(signed long long min, signed long long start, signed long long end) 
{
  signed long long cur;

  while (end-start > 1) 
  {
    cur = start + ((end-start) / 2); 
    signed long long actual = calcDistance(cur);
    printf("cur: %lld start:%lld end:%lld min: %lld actual: %lld\n", cur, start, end, min, actual);
    if (actual > min) {
      end = cur-1; 
    } else if (actual < min) {
      start = cur+1;
    } else if (actual == min) {
      return cur+1;
    }
  }
  if (calcDistance(start) > min) {
    return start;
  } else if (calcDistance(end) > min) {
    return end;
  } else {
    fprintf(stderr, "Error. Neither start: %lld nor end: %lld are larger than min: %lld\n", start, end, min);
  }
}


int main(int argc, char* argv[]) 
{
  signed long long time;
  signed long long distance;
  signed long long val;

  if (argc != 3) {
    fprintf(stderr, "Syntax: %s <time> <distance>\n");
    return 4;
  }

  time = strtoull(argv[1], NULL, 10);
  distance = strtoull(argv[2], NULL, 10);

  T = time;

  /* Search across 1/2 the array so that you know it is continually increasing */

  /* Result is not useful because will likely not get an exact match - need last found number */

  val = mysearch(distance, 0, T/2);
  
  printf("Range: %lld to %lld : %lld ways\n", val, T-val, (T-val) - val + 1);
  return 0;
} 
