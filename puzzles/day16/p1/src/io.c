#include "io.h"
#include "cellinfo.h"
#include <stdio.h>
#include <sys/stat.h>
#include <stdlib.h>

int read_beam_matrix(const char* cfname, struct BeamMatrix* matrix)
{
  struct stat st;
  if (stat(cfname, &st)) {
    fprintf(stderr, "Unable to open beam-matrix file %s\n", cfname);
    return(8);
  }

  size_t filesize = st.st_size;

  char* data = malloc(filesize);
  if (!data) {
    fprintf(stderr, "Unable to allocate beam-matrix buffer %s\n", cfname);
    return(12);
  }

  FILE* fp = fopen(cfname, "r");
  if (!fp) {
    fprintf(stderr, "Unable to open beam-matrix file %s\n", cfname);
    return(16);
  }
  size_t bytes = fread(data, 1, filesize, fp);
  if (bytes != filesize) {
    fprintf(stderr, "Unable to read beam-matrix file %s\n", cfname);
    return(16);
  }
  if (fclose(fp)) {
    fprintf(stderr, "Unable to close beam-matrix file %s\n", cfname);
    return(20);
  }

  matrix = create_beam_matrix(data, bytes);
  if (!matrix) {
    return(24);
  }
  return 0;
}
