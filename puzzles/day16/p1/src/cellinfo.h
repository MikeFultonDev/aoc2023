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
    Right=0x8
  };

  struct BeamCell {
    enum CellDevice device;
    uint32_t direction;
  };

  struct BeamMatrix {
    int cols;
    int rows;

    struct BeamCell* cells;

  };

  struct BeamMatrix* create_beam_matrix(const char* raw_data, size_t raw_data_len);
  int print_matrix(struct BeamMatrix* matrix);
  struct BeamCell* beam_cell(struct BeamMatrix* matrix, int col, int row);
#endif
