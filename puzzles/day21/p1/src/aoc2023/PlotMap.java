package aoc2023;
import java.util.ArrayList;
import java.util.List;

class PlotMap {
  PlotMap(List<String> plotLines) {
    this._startRow = this._startCol = -1;

    this._matrix = new char[plotLines.size()][plotLines.get(0).length()];
    int row = 0;
    for (String plotRow : plotLines) {
      for (int col=0; col<plotRow.length(); ++col) {
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
  @Override
  public String toString() {
    String out="";
    for (int row=0; row<_matrix.length; ++row) {
      for (int col=0; col<_matrix[0].length; ++col) {
        out = out + _matrix[row][col];
      }
      out = out + System.lineSeparator();
    }
    return out;
  }
  private char _matrix[][];
  private int _startRow;
  private int _startCol;

  private static final char PLOT_START = 'S';
  private static final char PLOT_GARDEN = '.';
  private static final char PLOT_ROCK = '#';
}
