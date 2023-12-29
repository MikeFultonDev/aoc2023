package aoc2023;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

class BrickMap {
  BrickMap(List<String> brickLines) throws java.io.IOException {
    this._bricks = new ArrayList<Brick>();
    for (String brickLine : brickLines) {
      this._bricks.add(new Brick(brickLine));   
    }
  }

  @Override
  public String toString() {
    String out = "";
    for (Brick brick : this._bricks) {
      out = out + brick + System.lineSeparator();
    }
    return out;
  }
 
  Brick findBrick(int cx, int cy, int cz) {
    for (Brick brick: this._bricks) {
      if (brick.fillsCell(cx, cy, cz)) {
        return brick;
      }
    }
    return null;
  }

  void adjustZCoordinate() {
    for (Brick brick: this._bricks) {
      // Adjust the Z value (have the brick settle on the stack of bricks) 
      // starting with the bricks closest to the ground
      brick.adjust(this._rectangularSolid);
      brick.add(this._rectangularSolid);
    }
  }

  int safeToDisintegrate() {
    Collections.sort(this._bricks);
    this._rectangularSolid = new RectangularSolid(this._bricks);
    adjustZCoordinate();
    Collections.sort(this._bricks);
    for (Brick brick: this._bricks) {
      // For each brick, see if it can be removed. If so, increment count by 1
      if (brick.safeToDisintegrate(this._rectangularSolid, this)) {
        this._safeToDisintegrate++;
      }
    }
    System.out.println("Determine which is Safe to Disintegrate");
    System.out.println(this.toString());
    return this._safeToDisintegrate;
  }
  private List<Brick> _bricks;
  private RectangularSolid _rectangularSolid;
  private int _safeToDisintegrate;
}
