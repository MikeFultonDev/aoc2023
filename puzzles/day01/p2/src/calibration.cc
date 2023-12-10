#include <stdio.h>
#include <string.h>
#include <sys/stat.h>
#include <stdlib.h>

enum State {
  nomatch=1,
  firstmatch=2,
  multimatch=3
} state;

static void updatestate(unsigned long long num, unsigned long long* first, unsigned long long* last, enum State* state)
{
  switch (*state) {
    case nomatch:
      *first = num;
      *state = firstmatch;
      break;
    case firstmatch:
      *last = num;
      *state = multimatch;
      break;
    case multimatch:
      *last = num;
      break;
  }
}

static const char* englishnum[] = {
  "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" // Note zero is place-holder
};
static unsigned long long englishnumlen[10] = { 0 }; // init on first use

static bool writtennum(const char* data, unsigned long long* num, size_t* index) 
{
  char c = data[*index];
  unsigned long long i;

  // Fastpath first character of numbers
  if (c != 'o' && c != 't' && c != 'f' && c != 's' && c != 'e' && c != 'n') {
    return false;
  }
  if (englishnumlen[0] == 0) {
    for (i=0ULL; i<10ULL; ++i) {
      englishnumlen[i] = strlen(englishnum[i]);
    }
  }

  const char* cp = &data[*index];
  for (i=1ULL; i<10ULL; ++i) {
    if (!memcmp(cp, englishnum[i], englishnumlen[i])) {
      *num = i;

      // Need to catch oneight as 18 so just advance by 1 character
      //*index += (englishnumlen[i] - 1);
      *index += 0;
      return true;
    }
  }
  return false;
}

static bool digitnum(const char digit, unsigned long long* num) 
{
  if (digit >= '0' && digit <= '9') {
    *num = digit - '0';
    return true;
  }
  return false;
}

static unsigned long long calibration(const char* data, size_t datasize) 
{
  unsigned long long tot=0ULL;
  unsigned long long totline;
  unsigned long long first, last, num;

  enum State state = nomatch;

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
    if (digitnum(data[i], &num)) {
      updatestate(num, &first, &last, &state);  
    } else if (writtennum(data, &num, &i)) {
      updatestate(num, &first, &last, &state);  
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

  char* data = (char*) malloc(filesize);
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
    

