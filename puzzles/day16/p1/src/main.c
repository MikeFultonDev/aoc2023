#include "cellinfo.h"

#include "io.h"

int main(int argc, char* argv[]) {
  struct stat st;
  const char* cfname;

  if (argc != 2) {
    fprintf(stderr, "Syntax: %s <beam-matrix file>\n", argv[0]);
    return(4);
  }

  rc = read_beam_matrix(argv[1], &matrix);
  if (rc) {
    return rc;
  }
  return 0;
}
