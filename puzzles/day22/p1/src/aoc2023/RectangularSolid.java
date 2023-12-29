package aoc2023;
import java.util.List;

class RectangularSolid {
  RectangularSolid(List<Brick> bricks) {
    BrickRange xRange = BrickRange.getRange(bricks, BrickDimension.X);
    BrickRange yRange = BrickRange.getRange(bricks, BrickDimension.Y);
    BrickRange zRange = BrickRange.getRange(bricks, BrickDimension.Z);

    if (xRange.min() < 0 || yRange.min() < 0 || zRange.min() < 0) {
      throw new RuntimeException("brick coordinates less than 0 - write code");
    }
    System.out.println("Ranges: x: " + xRange + ", y: " + yRange + ", z: " + zRange);
    this._rectangularSolid = new boolean[xRange.max()+1][yRange.max()+1][zRange.max()+1];
  }

  boolean clear(int x, int y, int z) {
    boolean val = this._rectangularSolid[x][y][z];
    System.out.println("[" + x + "][" + y + "][" + z + "] is: " + val);
    return val == false;
  }

  void fill(int x, int y, int z) {
    System.out.println("Set [" + x + "][" + y + "][" + z + "]");
    this._rectangularSolid[x][y][z] = true;
  }

  private boolean _rectangularSolid[][][];
}
