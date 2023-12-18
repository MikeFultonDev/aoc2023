#include "cellinfo.h"
#include <stdio.h>
#include <stdlib.h>

static enum CellDevice c_to_device(char c) 
{

  switch(c) {
    case '.':
      return NoDevice;
    case '/':
      return Slash;
    case '\\': 
      return BackSlash;
    case '|':
      return VerticalSplitter;
    case '-':
      return HorizontalSplitter;
    default:
      fprintf(stderr, "Unexpected character detected in stream: %c (0x%x)\n", c, c);
      return NoDevice;
  }
}

struct BeamMatrix* create_beam_matrix(const char* raw_data, size_t raw_data_len)
{
  struct BeamMatrix* matrix = calloc(1, sizeof(struct BeamMatrix));
  if (matrix) {
    // This allocation is slightly too large - it includes newlines in the calculation
    matrix->cells = calloc(raw_data_len, sizeof(struct BeamCell));
    if (!matrix->cells) {
      free(matrix);
      matrix = NULL;
    }
  }
  if (matrix == NULL) {
    fprintf(stderr, "Unable to allocate underlying matrix structure\n");
  }

  size_t cols = 0;
  size_t col = 0;
  size_t row = 0;
  for (size_t i=0; i<raw_data_len; ++i) {
    if (raw_data[i] == '\n') {
      if (cols == 0) {
        cols = i;
        continue;
      }
      col = 0;
      ++row;
    } else {
      size_t raw_index = (row*cols) + col;
      matrix->cells[raw_index].device = c_to_device(raw_data[i]);
    }
  }
  matrix->cols = cols;
  matrix->rows = row;
  return matrix;
}

struct BeamCell* cell(struct BeamMatrix* matrix, int col, int row)
{
  size_t raw_index = (row * matrix->cols) + col;
  return &matrix->cells[raw_index];
}
