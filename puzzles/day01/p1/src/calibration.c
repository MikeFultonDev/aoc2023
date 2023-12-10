#include <stdio.h>
#include <sys/stat.h>
#include <stdlib.h>

static unsigned long long calibration(const char* data, size_t datasize) 
{
  unsigned long long tot=0ULL;
  unsigned long long totline;
  unsigned long long first, last, num;

  enum State {
    nomatch=1,
    firstmatch=2,
    multimatch=3
  } state = nomatch;

  for (size_t i=0; i<datasize; ++i) {
    if (data[i] == '\n') {
      if (state == nomatch) {
        totline = 0ULL;
      } else if (state == firstmatch) {
        totline = ((first * 10ULL) + first);
      } else {
        totline = ((first * 10ULL) + last);
      }
      state = nomatch;
      tot += totline;
    }
    if (data[i] >= '0' && data[i] <= '9') {
      num = data[i] - '0';
      switch (state) {
        case nomatch:
          first = num;
          state = firstmatch;
          break;
        case firstmatch:
          last = num;
          state = multimatch;
          break;
        case multimatch:
          last = num;
          break;
      }
    }
  }
     
  return tot;
}

int main(int argc, char* argv[]) {
  struct stat st;
  const char* cfname;

  if (argc != 2) {
    fprintf(stdout, "Syntax: %s <calibration file>\n", argv[0]);
    return(4);
  }
  cfname = argv[1];

  if (stat(cfname, &st)) {
    fprintf(stdout, "Unable to open calibration file %s\n", cfname);
    return(8);
  }

  size_t filesize = st.st_size;

  char* data = malloc(filesize);
  if (!data) {
    fprintf(stdout, "Unable to allocate calibration buffer %s\n", cfname);
    return(12);
  }

  FILE* fp = fopen(cfname, "r");
  if (!fp) {
    fprintf(stdout, "Unable to open calibration file %s\n", cfname);
    return(16);
  }
  size_t bytes = fread(data, 1, filesize, fp);
  if (bytes != filesize) {
    fprintf(stdout, "Unable to read calibration file %s\n", cfname);
    return(16);
  }
  if (fclose(fp)) {
    fprintf(stdout, "Unable to close calibration file %s\n", cfname);
    return(20);
  }

  printf("Calibration: %llu\n", calibration(data, filesize));

  return 0;
}
    

