#ifndef __CELL_INFO__
  #define __CELL_INFO__ 1

  enum CellDevice {
    None=0,
    Slash=1,
    BackSlash=2,
    VerticalSplitter=3,
    HorizontalSplitter=4
  };

  enum ActiveDirection {
    None=0,
    Up=0x1,
    Down=0x2,
    Left=0x4,
    Right=0x8
  };

  struct BeamCell {
    enum CellDevice device;
    enum ActiveDirection direction;
  };

  struct BeamMatrix {
    int cols;
    int rows;

    struct* BeamCell cells;

  };

  struct* BeamMatrix create_matrix(const char* raw_data);
  struct* BeamCell cell(struct* BeamMatrix matrix, int col, int row);
#endif
