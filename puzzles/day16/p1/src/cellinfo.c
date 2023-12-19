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

  printf("Matrix size: %zu cols by %zu rows\n", matrix->cols, matrix->rows);
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

static enum ActiveDirection compute_new_direction(struct BeamMatrix* matrix, enum ActiveDirection direction, size_t col, size_t row)
{
  switch (direction) {
    case Up:
      return (row > 0) ? Up : Blocked;
    case Down:
      return (row < matrix->rows-1) ? Down : Blocked;
    case Left:
      return (col > 0) ? Left : Blocked;
    case Right:
      return (col < matrix->cols-1) ? Right : Blocked;
    default:
      fprintf(stderr, "Unexpected direction for compute_new_direction %hhu\n", direction);
      return Blocked;
  }
}

static int compute_new_location(struct BeamMatrix* matrix, enum ActiveDirection direction, size_t col, size_t row, struct BeamOutState* state, struct BeamState* beam)
{
  state->num_directions = 1;
  ssize_t row_inc = 0;
  ssize_t col_inc = 0;
  switch (direction) {
    case Up: {
      beam->direction = compute_new_direction(matrix, direction, col, row);
      row_inc = -1;
      break;
    }
    case Down: {
      beam->direction = compute_new_direction(matrix, direction, col, row);
      row_inc = 1;
      break;
    }
    case Left: {
      beam->direction = compute_new_direction(matrix, direction, col, row);
      col_inc = -1;
      break;
    }
    case Right: {
      beam->direction = compute_new_direction(matrix, direction, col, row);
      col_inc = 1;
      break;
    }
    default:
      fprintf(stderr, "Unexpected direction for compute_new_location %hhu\n", direction);
      return 4;
  } 
  if (beam->direction == Blocked) {
    beam->col = col;
    beam->row = row;
  } else {
    beam->col = col + col_inc;
    beam->row = row + row_inc;
  }
  return 0;
}

static int compute_new_horizontal_locations(struct BeamMatrix* matrix, size_t col, size_t row, struct BeamOutState* out)
{
  size_t num_directions = 0;
  enum ActiveDirection direction;
  direction = compute_new_direction(matrix, Right, col, row);
  if (direction != Blocked) {
    out->beam[num_directions].direction = Right;
    out->beam[num_directions].col = col+1;
    out->beam[num_directions].row = row;
    ++num_directions;
  }
  direction = compute_new_direction(matrix, Left, col, row);
  if (direction != Blocked) {
    out->beam[num_directions].direction = Left;
    out->beam[num_directions].col = col-1;
    out->beam[num_directions].row = row;
    ++num_directions;
  }
  out->num_directions = num_directions;
  return 0;
}

static int compute_new_vertical_locations(struct BeamMatrix* matrix, size_t col, size_t row, struct BeamOutState* out)
{
  size_t num_directions = 0;
  enum ActiveDirection direction;
  direction = compute_new_direction(matrix, Up, col, row);
  if (direction != Blocked) {
    out->beam[num_directions].direction = Up;
    out->beam[num_directions].col = col;
    out->beam[num_directions].row = row-1;
    ++num_directions;
  }
  direction = compute_new_direction(matrix, Down, col, row);
  if (direction != Blocked) {
    out->beam[num_directions].direction = Down;
    out->beam[num_directions].col = col;
    out->beam[num_directions].row = row+1;
    ++num_directions;
  }
  out->num_directions = num_directions;
  return 0;
}

static int compute_to_state(struct BeamMatrix* matrix, struct BeamState* in, struct BeamOutState* out) 
{
  struct BeamCell* cell = beam_cell(matrix, in->col, in->row);
  if (!cell) {
    fprintf(stderr, "Unable to find cell at %zu %zu\n", in->col, in->row);
    return 16;
  }
  if (in->direction == Blocked) {
    return 0;
  }
  out->num_directions = 0;
  enum CellDevice device = cell->device;
  if (device == NoDevice) {
    return compute_new_location(matrix, in->direction, in->col, in->row, out, &out->beam[0]);
  }

  switch (in->direction) {
    case Up: {
      switch (device) {
        case Slash: {
          return compute_new_location(matrix, Right, in->col, in->row, out, &out->beam[0]);
        }
        case BackSlash: {
          return compute_new_location(matrix, Left, in->col, in->row, out, &out->beam[0]);
        }
        case VerticalSplitter: {
          return compute_new_location(matrix, in->direction, in->col, in->row, out, &out->beam[0]);
        }
        case HorizontalSplitter: {
          return compute_new_horizontal_locations(matrix, in->col, in->row, out);
        }
        default: {
          fprintf(stderr, "Unexpected direction as input to compute_state Up\n");
          return 8;
        }
      }
    }
    case Down: {
      switch (device) {
        case Slash: {
          return compute_new_location(matrix, Left, in->col, in->row, out, &out->beam[0]);
        }
        case BackSlash: {
          return compute_new_location(matrix, Right, in->col, in->row, out, &out->beam[0]);
        }
        case VerticalSplitter: {
          return compute_new_location(matrix, in->direction, in->col, in->row, out, &out->beam[0]);
        }
        case HorizontalSplitter: {
          return compute_new_horizontal_locations(matrix, in->col, in->row, out);
        }
        default: {
          fprintf(stderr, "Unexpected direction as input to compute_state Down\n");
          return 12;
        }
      }
    }
    case Left: {
      switch (device) {
        case Slash: {
          return compute_new_location(matrix, Down, in->col, in->row, out, &out->beam[0]);
        }
        case BackSlash: {
          return compute_new_location(matrix, Up, in->col, in->row, out, &out->beam[0]);
        }
        case VerticalSplitter: {
          return compute_new_vertical_locations(matrix, in->col, in->row, out);
        }
        case HorizontalSplitter: {
          return compute_new_location(matrix, in->direction, in->col, in->row, out, &out->beam[0]);
        }
        default: {
          fprintf(stderr, "Unexpected direction as input to compute_state Left\n");
          return 12;
        }
      }
    }
    case Right: {
      switch (device) {
        case Slash: {
          return compute_new_location(matrix, Up, in->col, in->row, out, &out->beam[0]);
        }
        case BackSlash: {
          return compute_new_location(matrix, Down, in->col, in->row, out, &out->beam[0]);
        }
        case VerticalSplitter: {
          return compute_new_vertical_locations(matrix, in->col, in->row, out);
        }
        case HorizontalSplitter: {
          return compute_new_location(matrix, in->direction, in->col, in->row, out, &out->beam[0]);
        }
        default: {
          fprintf(stderr, "Unexpected direction as input to compute_state Right\n");
          return 12;
        }
      }
    }
    default: {
      fprintf(stderr, "Unexpected direction as input to compute_state\n");
      return 4;
    }
  }

 
  return 0;
}

static void print_state(struct BeamState* beam) 
{
  printf("direction: %hhu col:%zu row:%zu \n", beam->direction, beam->col, beam->row);
}

static void print_out_state(struct BeamOutState* out) 
{
  printf("num entries: %zu\n", out->num_directions);
  for (size_t i=0; i<out->num_directions; ++i) {
    print_state(&out->beam[i]);
  }
}

int track_beam(struct BeamMatrix* matrix, struct BeamState* in)
{
  int rc;
  int errors = 0;
  struct BeamCell* cell = beam_cell(matrix, in->col, in->row);   
  if (cell->direction & in->direction) {
    return 0; // The beam has already gone through this cell in this direction
  }

  if (cell->direction == Blocked) {
    return 0; // Team beam has hit the edge of a wall
  }

  cell->direction |= in->direction; // Add in this new beam direction

  struct BeamOutState out = {0};
  rc = compute_to_state(matrix, in, &out);
  if (rc) {
    return 0;
  }

  //print_out_state(&out);
  for (size_t i=0; i<out.num_directions; ++i) {
    errors += track_beam(matrix, &out.beam[i]);
  }
  return errors;
}

int energized_cells(struct BeamMatrix* matrix) 
{
  int energized = 0;
  size_t row;
  size_t col;
  for (row=0; row<matrix->rows; ++row) {
    for (col=0; col<matrix->cols; ++col) {
      struct BeamCell* cell = beam_cell(matrix, col, row);
      if (cell->direction != NoDirection) {
        ++energized;
      }
    }
  }
  return energized;
}
