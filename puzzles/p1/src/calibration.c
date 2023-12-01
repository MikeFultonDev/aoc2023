#include <stdio.h>
#include <sys/stat.h>
#include <stdlib.h>

unsigned long long calibration(const char* data) 
{
  return 0ULL;
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

  printf("Calibration: %llu\n", calibration(data));

  return 0;
}
    

