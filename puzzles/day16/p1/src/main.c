#include "cellinfo.h"
#include "io.h"
#include <stdio.h>

int main(int argc, char* argv[]) {
  const char* cfname;
  int rc;
  struct BeamMatrix* matrix;

  if (argc != 2) {
    fprintf(stderr, "Syntax: %s <beam-matrix file>\n", argv[0]);
    return(4);
  }

  rc = read_beam_matrix(argv[1], &matrix);
  if (rc) {
    return rc;
  }

  print_matrix(matrix);

  return 0;
}
