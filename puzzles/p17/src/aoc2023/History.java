package aoc2023;

class History {
  History(String line) {
    this._line = line;
    this._next = null;
  }

  @Override 
  public String toString() {
    return this._line + " next number is: " + this._next;
  }

  long next() throws NumberFormatException {
    if (this._next == null) {
      // Need to compute
      computeNext();
    }
    return _next;
  }

  private void computeNext() throws NumberFormatException {
    // Create a square array, first row is numbers from line
    // May not need the full 'square'
    // Add one extra slot to the end which is computed (next)

    String[] words = this._line.split("\\s+");
    Long[][] matrix = new Long[words.length][words.length+1];
    int i=0;
    for (String word : words) {
      matrix[0][i++] = Long.parseLong(word);
    }

    // For each row, compute the next row which is differences
    int row = 0;
    boolean allZeros;
    do {
      allZeros = true;
      ++row;
      for (int col = words.length-1; col >= row; --col) {
        matrix[row][col] = matrix[row-1][col] - matrix[row-1][col-1];
        if (matrix[row][col] != 0) {
          allZeros = false;
        }
      }
    } while (!allZeros);
    this._rows = row+1;

    // Fill in the number on the far right bottom to top
    int col = words.length;
    long compute = 0;
    while (--row >= 0) {
      compute = matrix[row][col-1] + compute;
      matrix[row][col] = compute;
    }
    //printMatrix(matrix);
    _next = compute;
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
  private Long _next;
  private int _rows;
}
