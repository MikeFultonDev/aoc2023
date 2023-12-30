package aoc2023;

class TrailCoord {
  TrailCoord(int x, int y) {
    this._x = x;
    this._y = y;
  }
  TrailCoord(TrailCoord orig, TrailDirection direction) {
    this._x = orig.x();
    this._y = orig.y();
    switch(direction) {
      case North: 
        --this._y; 
        break;
      case South: 
        ++this._y;
        break;
      case West:  
        --this._x;
        break;
      case East:  
        ++this._x;
        break;
      default: 
        throw new RuntimeException("Internal Error. Unknown direction");
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof TrailCoord) {
      TrailCoord rhs = (TrailCoord) obj;
      return this._x == rhs._x && this._y == rhs._y;
    } else {
      return false;
    }
  }

  @Override
  public String toString() {
    return "[" + this._x + "," + this._y + "]";
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
