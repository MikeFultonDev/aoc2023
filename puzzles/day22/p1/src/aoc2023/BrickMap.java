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
  
  void adjustZCoordinate() {
    System.out.println("sorted by Z");
    System.out.println(this.toString());
    for (Brick brick: this._bricks) {
      // Adjust the Z value (have the brick settle on the stack of bricks) 
      // starting with the bricks closest to the ground
      brick.adjust(this._rectangularSolid);
      brick.add(this._rectangularSolid);
    }
    System.out.println("adjusted by Z");
    System.out.println(this.toString());
  }

  int safeToDisintegrate() {
    Collections.sort(this._bricks);
    this._rectangularSolid = new RectangularSolid(this._bricks);
    adjustZCoordinate();
    return 0;
  }
  private List<Brick> _bricks;
  private RectangularSolid _rectangularSolid;
}
