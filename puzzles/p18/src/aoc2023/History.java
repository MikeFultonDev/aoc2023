package aoc2023;

class History {
  History(String line) {
    this._line = line;
    this._prev = null;
  }

  @Override 
  public String toString() {
    return this._line + " prev number is: " + this._prev;
  }

  long prev() throws NumberFormatException {
    if (this._prev == null) {
      // Need to compute
      computePrev();
    }
    return _prev;
  }

  private void computePrev() throws NumberFormatException {
    // Create a square array, first row is numbers from line
    // May not need the full 'square'
    // Add one extra slot to the start which is computed (prev)

    String[] words = this._line.split("\\s+");
    Long[][] matrix = new Long[words.length][words.length+1];
    int i=0;
    for (String word : words) {
      matrix[0][++i] = Long.parseLong(word);
    }

    // For each row, compute the prev row which is differences
    int row = 0;
    boolean allZeros;
    do {
      allZeros = true;
      ++row;
      for (int col = 1; col <= words.length-row; ++col) {
        matrix[row][col] = matrix[row-1][col+1] - matrix[row-1][col];
        if (matrix[row][col] != 0) {
          allZeros = false;
        }
      }
    } while (!allZeros);
    this._rows = row+1;

    // Fill in the number on the far right bottom to top
    int col = 0;
    long compute = 0;
    while (--row >= 0) {
      compute = matrix[row][col+1] - compute;
      matrix[row][col] = compute;
    }
    //printMatrix(matrix);
    _prev = compute;
  }

  private void printMatrix(Long[][] matrix) {
    for (int row=0; row < this._rows; ++row) {
      for (int col=0; col < matrix[row].length; ++col) {
        System.out.print(matrix[row][col] + " ");
      }
      System.out.println();
    }
  }
  private String _line;
  private Long _prev;
  private int _rows;
}
