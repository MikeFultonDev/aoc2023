#include "cellinfo.h"
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>

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

static char device_to_c(enum CellDevice cd)
{

  switch(cd) {
    case NoDevice:
      return '.';
    case Slash:
      return '/';
    case BackSlash:
      return '\\';
    case VerticalSplitter:
      return '|';
    case HorizontalSplitter:
      return '-';
    default:
      fprintf(stderr, "Unexpected character detected in stream: (0x%x)\n", cd);
      return '.';
  }
}

// The following code sequence gets number of bits set and clang should compile to popcnt.
/// See: https://stackoverflow.com/questions/109023/count-the-number-of-set-bits-in-a-32-bit-integer
static int numberOfSetBits(uint32_t i)
{
     i = i - ((i >> 1) & 0x55555555);  // add pairs of bits
     i = (i & 0x33333333) + ((i >> 2) & 0x33333333);  // quads
     i = (i + (i >> 4)) & 0x0F0F0F0F;  // groups of 8
     i *= 0x01010101;                  // horizontal sum of bytes
     return  i >> 24;                  // return just that top byte (after truncating to 32-bit even when int is wider than uint32_t)
}

static size_t cell_directions(uint32_t direction)
{ 
  return numberOfSetBits(direction);
}

// Integer must be from 0 to 9 - single byte 'character' representation
static char itoc(uint32_t i) 
{
  if (i > 9) {
    fprintf(stderr, "itoc on number that is too large: %u\n", i);
    return '0';
  }
  return '0' + i;
}

int print_matrix(struct BeamMatrix* matrix)
{
  if (!matrix) {
    return 4;
  }
  size_t row;
  size_t col;
  char* cell_text;
  char* cell_count;

  cell_text = malloc(matrix->cols+1);
  cell_count = malloc(matrix->cols+1);

  if (!cell_text || !cell_count) {
    fprintf(stderr, "Unable to allocate print structures\n");
    return 4;
  }

  printf("Matrix size: %d cols by %d rows\n", matrix->cols, matrix->rows);
  for (row=0; row<matrix->rows; ++row) {
    for (col=0; col<matrix->cols; ++col) {
      struct BeamCell* cell = beam_cell(matrix, col, row);
      cell_text[col] = device_to_c(cell->device);
      cell_count[col] = itoc(cell_directions(cell->direction));
    }
    cell_text[col] = '\0';
    cell_count[col] = '\0';
    printf("%s    |   %s\n", cell_text, cell_count);
 };
 return 0;
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

  size_t col = 0;
  size_t row = 0;
  for (size_t i=0; i<raw_data_len; ++i) {
    if (raw_data[i] == '\n') {
      if (matrix->cols == 0) {
        matrix->cols = col;
      }
      col = 0;
      ++row;
      continue;
    } else {
      struct BeamCell* cell = beam_cell(matrix, col, row);
      cell->device = c_to_device(raw_data[i]);
      //printf("cell[%zu][%zu]: %c %c\n", col, row, raw_data[i], device_to_c(matrix->cells[raw_index].device));
      ++col;
    }
  }
  matrix->rows = row;
  return matrix;
}

struct BeamCell* beam_cell(struct BeamMatrix* matrix, int col, int row)
{
  size_t raw_index = (row * matrix->cols) + col;
  return &matrix->cells[raw_index];
}
