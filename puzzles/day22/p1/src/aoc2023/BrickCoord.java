package aoc2023;
import java.util.ArrayList;
import java.util.List;

class BrickCoord {
  BrickCoord(double x, double y, double z) {
    this._x = (int) x;
    this._y = (int) y;
    this._z = (int) z;
  }

  void dropZ(int drop) {
    this._z -= drop;
  }

  @Override
  public String toString() { 
    return "(" + this._x + "," + this._y + "," + this._z + ")";
  }

  private int _x;
  private int _y;
  private int _z;


  public static int diff(BrickCoord lhs, BrickCoord rhs, BrickDimension dim) {
    switch (dim) {
      case X: return lhs._x - rhs._x;
      case Y: return lhs._y - rhs._y;
      case Z: return lhs._z - rhs._z;
    }
    throw new RuntimeException("internal error");
  }
  public static int min(BrickCoord lhs, BrickCoord rhs, BrickDimension dim) {
    switch (dim) {
      case X: return Integer.min(lhs._x, rhs._x);
      case Y: return Integer.min(lhs._y, rhs._y);
      case Z: return Integer.min(lhs._z, rhs._z);
    }
    throw new RuntimeException("internal error");
  }
  public static int max(BrickCoord lhs, BrickCoord rhs, BrickDimension dim) {
    switch (dim) {
      case X: return Integer.max(lhs._x, rhs._x);
      case Y: return Integer.max(lhs._y, rhs._y);
      case Z: return Integer.max(lhs._z, rhs._z);
    }
    throw new RuntimeException("internal error");
  }
}
    
