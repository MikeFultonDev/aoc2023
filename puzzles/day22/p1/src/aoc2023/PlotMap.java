package aoc2023;
import java.util.ArrayList;
import java.util.List;

class PlotMap {
  PlotMap(List<String> plotLines) throws java.io.IOException {
    this._startRow = this._startCol = -1;
    this._numRows = plotLines.size();
    this._numCols = plotLines.get(0).length();
    this._matrix = new char[this._numRows][this._numCols];

    int row = 0;
    for (String plotRow : plotLines) {
      for (int col=0; col<this._numCols; ++col) {
        char c = plotRow.charAt(col);
        switch (c) {
          case 'S': 
            this._startRow = row;
            this._startCol = col;
            _matrix[row][col] = PLOT_STEP;
            break;
          case '.': 
            _matrix[row][col] = PLOT_GARDEN;
            break;
          case '#': 
            _matrix[row][col] = PLOT_ROCK;
            break;
          default:
            System.err.println("Unexpected character: " + c + " in input plot");
            throw new java.io.IOException();
        }
      }
      ++row;
    }
  }

  boolean isPlot(int row, int col) {
    if (row < 0 || col < 0 || row >= this._numRows || col >= this._numCols) {
      return false;
    }
    if (this._matrix[row][col] == PLOT_GARDEN) {
      return true;
    } else {
      return false;
    }
  }

  boolean isStep(int row, int col) {
    if (row < 0 || col < 0 || row >= this._numRows || col >= this._numCols) {
      return false;
    }
    if (this._matrix[row][col] == PLOT_STEP) {
      return true;
    } else {
      return false;
    }
  }

  void mark(char n) {
    for (int row=0; row<this._numRows; ++row) {
      for (int col=0; col<this._numCols; ++col) {
        if (_matrix[row][col] == n) {
          if (isPlot(row-1, col)) {
            _matrix[row-1][col] = (char) (n+1);
          }
          if (isPlot(row+1, col)) {
            _matrix[row+1][col] = (char) (n+1);
          }
          if (isPlot(row, col-1)) {
            _matrix[row][col-1] = (char) (n+1);
          }
          if (isPlot(row, col+1)) {
            _matrix[row][col+1] = (char) (n+1);
          }
        }
      }
    }
  }
    
  void walkNSteps(int n) {
    _matrix[this._startRow][this._startCol] = 0;
    for (char i=0; i<n; i++) {
      mark(i);
    }
  }

  int countSteps() {
    int count=0;
    for (int row=0; row<this._numRows; ++row) {
      for (int col=0; col<this._numCols; ++col) {
        if (_matrix[row][col] % 2 == 0) {
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
        char c;
        char rawc = _matrix[row][col];
        if (rawc % 2 == 0) {
          c = 'w';
        } else if (rawc == PLOT_START) {
          c = 'S';
        } else if (rawc == PLOT_GARDEN) {
          c = '.';
        } else if (rawc == PLOT_ROCK) {
          c = '#';
        } else {
          c = '-';
        }
        out = out + c;
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
  private int _lowPoint;

  private static final char PLOT_STEP = 125;
  private static final char PLOT_START = 123;
  private static final char PLOT_GARDEN = 121;
  private static final char PLOT_ROCK = 119;
}
