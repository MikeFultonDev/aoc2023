package aoc2023;
import java.util.ArrayList;
import java.util.List;

class PlotMap {
  PlotMap(List<String> plotLines) {
    this._startRow = this._startCol = -1;
    this._numRows = plotLines.size();
    this._numCols = plotLines.get(0).length();
    this._matrix = new char[this._numRows][this._numCols];

    int row = 0;
    for (String plotRow : plotLines) {
      for (int col=0; col<this._numCols; ++col) {
        char c = plotRow.charAt(col);
        if (c == PLOT_START) {
          this._startRow = row;
          this._startCol = col;
        }
        _matrix[row][col] = c;
      }
      ++row;
    }
  }

  boolean isPlot(int row, int col) {
    if (row < 0 || col < 0 || row >= this._numRows || col >= this._numCols) {
      return false;
    }
    if (this._matrix[row][col] == PLOT_STEP || this._matrix[row][col] == PLOT_GARDEN) {
      return true;
    } else {
      return false;
    }
  }

  void markThenExpand(int row, int col, int remainingSteps) {
    if (remainingSteps < 0) {
      return;
    }

    if (remainingSteps % 2 == 0) {
      this._matrix[row][col] = PLOT_STEP;
    }

    if (isPlot(row-1, col)) {
      markThenExpand(row-1, col, remainingSteps-1);
    }
    if (isPlot(row+1, col)) {
      markThenExpand(row+1, col, remainingSteps-1);
    }
    if (isPlot(row, col-1)) {
      markThenExpand(row, col-1, remainingSteps-1);
    }
    if (isPlot(row, col+1)) {
      markThenExpand(row, col+1, remainingSteps-1);
    }
  }
    
  void walkNSteps(int n) {
    markThenExpand(_startRow, _startCol, n);
  }

  int countSteps() {
    int count=0;
    for (int row=0; row<this._numRows; ++row) {
      for (int col=0; col<this._numCols; ++col) {
        if (_matrix[row][col] == PLOT_STEP) {
          ++count;
        }
      }
    }
    return count;
  }
      
  @Override
  public String toString() {
    String out="";
    for (int row=0; row<this._numRows; ++row) {
      for (int col=0; col<this._numCols; ++col) {
        out = out + _matrix[row][col];
      }
      out = out + System.lineSeparator();
    }
    return out;
  }
  private char _matrix[][];
  private int _startRow;
  private int _startCol;
  private int _numRows;
  private int _numCols;

  private static final char PLOT_START = 'S';
  private static final char PLOT_STEP = 'O';
  private static final char PLOT_GARDEN = '.';
  private static final char PLOT_ROCK = '#';
}
