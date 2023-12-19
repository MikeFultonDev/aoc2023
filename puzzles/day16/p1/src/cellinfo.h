#ifndef __CELL_INFO__
  #define __CELL_INFO__ 1

  #include <stddef.h>
  #include <stdint.h>

  enum CellDevice {
    NoDevice=0,
    Slash=1,
    BackSlash=2,
    VerticalSplitter=3,
    HorizontalSplitter=4
  };

  enum ActiveDirection {
    NoDirection=0,
    Up=0x1,
    Down=0x2,
    Left=0x4,
    Right=0x8,
    Blocked=0x10
  };

  struct BeamCell {
    enum CellDevice device;
    uint32_t direction;
  };

  struct BeamMatrix {
    size_t cols;
    size_t rows;

    struct BeamCell* cells;
  };

  struct BeamState {
    enum ActiveDirection direction;
    size_t col;
    size_t row;
  };

  struct BeamOutState {
    size_t num_directions;
    struct BeamState beam[2];
  };

  struct BeamMatrix* create_beam_matrix(const char* raw_data, size_t raw_data_len);
  int print_matrix(struct BeamMatrix* matrix);
  struct BeamCell* beam_cell(struct BeamMatrix* matrix, int col, int row);

  int track_beam(struct BeamMatrix* matrix, struct BeamState* in);
  int energized_cells(struct BeamMatrix* matrix);
#endif
