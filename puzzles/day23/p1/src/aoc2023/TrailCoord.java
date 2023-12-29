package aoc2023;

class TrailCoord {
  TrailCoord(int x, int y) {
    this._x = x;
    this._y = y;
  }

  int x() {
    return this._x;
  }
  int y() {
    return this._y;
  }
  private int _x;
  private int _y;
}
