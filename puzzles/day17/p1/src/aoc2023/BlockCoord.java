package aoc2023;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

class BlockCoord {
  BlockCoord(int x, int y) {
    this._x = x;
    this._y = y;
  }

  @Override 
  public String toString() {
    return "[" + this._x + "," + this._y + "]";
  }

  int x() { return _x; }
  int y() { return _y; }

  boolean isMinX(BlockDiagram blockDiagram) {
    return this._x == blockDiagram.minX();
  }
  boolean isMaxX(BlockDiagram blockDiagram) {
    return this._x == blockDiagram.maxX()-1;
  }
  boolean isMinY(BlockDiagram blockDiagram) {
    return this._y == blockDiagram.minY();
  }
  boolean isMaxY(BlockDiagram blockDiagram) {
    return this._y == blockDiagram.maxY()-1;
  }

  private int _x;
  private int _y;
}
