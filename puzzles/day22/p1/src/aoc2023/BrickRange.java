package aoc2023;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

class BrickRange {
  BrickRange(int min, int max) {
    this._min = min;
    this._max = max;
  }

  static BrickRange getRange(List<Brick> bricks, BrickDimension dim) {
    int min = Integer.MAX_VALUE;
    int max = Integer.MIN_VALUE;
    for (Brick brick: bricks) {
      int minval = brick.min(dim);
      int maxval = brick.max(dim);
      if (minval < min) {
        min = minval;
      }
      if (maxval > max) {
        max = maxval;
      }
    }
    return new BrickRange(min, max);
  }

  int max() {
    return this._max;
  }
  int min() {
    return this._min;
  }

  @Override
  public String toString() {
    return Integer.toString(this._min) + " -> " + Integer.toString(this._max);
  }

  private int _min;
  private int _max;
}
