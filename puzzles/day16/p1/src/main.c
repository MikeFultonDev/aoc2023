#include "cellinfo.h"
#include "io.h"
#include <stdio.h>
#include <stddef.h>

int main(int argc, char* argv[]) {
  const char* cfname;
  int rc;
  struct BeamMatrix* matrix;
  struct BeamState beam_in = { Right, 0, 0 };

  if (argc != 2) {
    fprintf(stderr, "Syntax: %s <beam-matrix file>\n", argv[0]);
    return(4);
  }

  rc = read_beam_matrix(argv[1], &matrix);
  if (rc) {
    return rc;
  }

  print_matrix(matrix);

  rc = track_beam(matrix, &beam_in);
    
  print_matrix(matrix);
  
  printf("%d Engergized cells\n", energized_cells(matrix));
  return rc;
}
