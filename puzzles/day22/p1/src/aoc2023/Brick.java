package aoc2023;
import java.util.ArrayList;
import java.util.List;
import java.io.StringReader;
import java.io.Reader;
import java.io.StreamTokenizer;

class Brick implements Comparable<Brick> {
  Brick(String brickLine) throws java.io.IOException {
    parseBrick(brickLine);
  }

  public int compareTo(Brick rhs) {
    return (int) (BrickCoord.diff(this._startCoords, rhs._startCoords, BrickDimension.Z));
  }

  void parseBrick(String brickLine) throws java.io.IOException {
    Reader r = new StringReader(brickLine);
    StreamTokenizer st = new StreamTokenizer(r);
    List<Object> tokens = new ArrayList<Object>();
    int token;

    // Sample line: 1,0,1~1,2,1
    // No error checking for invalid lines
    double lastVal = 0;

    double nums[] = new double[6];
    int num = 0;
    while (true) {
      token = st.nextToken();
      if (token == StreamTokenizer.TT_EOF || st.ttype == ',' || st.ttype == '~') {
        nums[num++] = lastVal;
        if (token == StreamTokenizer.TT_EOF) {
          break;
        }
      } else if (st.ttype == StreamTokenizer.TT_NUMBER) {
        lastVal = st.nval;
      } else if (st.ttype == StreamTokenizer.TT_WORD) {
        throw new java.io.IOException("Unexpected text: " + st.sval + " encountered");
      } else {
        throw new java.io.IOException("Unexpected token: " + st.ttype + " encountered");
      }
    }
    this._startCoords = new BrickCoord(nums[0], nums[1], nums[2]);
    this._endCoords = new BrickCoord(nums[3], nums[4], nums[5]);
    this._safeToDisintegrate = false;
  }

  int min(BrickDimension dim) {
    return BrickCoord.min(_startCoords, _endCoords, dim);
  }

  int max(BrickDimension dim) {
    return BrickCoord.max(_startCoords, _endCoords, dim);
  }

  void adjust(RectangularSolid rs) {
    // Check each level below my lowest point and see if it is clear
    // Adjust the Z coordinate (start and end) by the amount I can drop

    int origZ = min(BrickDimension.Z);
    int newZ;
    for (newZ = origZ - 1; newZ >= 1; --newZ) {
      if (!brickFootprintClear(rs, newZ)) {
        break;
      }
    }
    String before = toString();
    int drop = origZ - newZ - 1;
    this._startCoords.dropZ(drop);
    this._endCoords.dropZ(drop);
    String after = toString();
  }

  boolean fillsCell(int cx, int cy, int cz) {
    for (int x=min(BrickDimension.X); x<=max(BrickDimension.X); ++x) {
      for (int y=min(BrickDimension.Y); y<=max(BrickDimension.Y); ++y) {
        for (int z=min(BrickDimension.Z); z<=max(BrickDimension.Z); ++z) {
          if (x == cx && y == cy && z == cz) {
            return true;
          }
        }
      }
    }
    return false;
  }

  boolean multipleSupports(RectangularSolid rs, BrickMap brickMap) {
    Brick firstSupport = null;
    for (int x=min(BrickDimension.X); x<=max(BrickDimension.X); ++x) {
      for (int y=min(BrickDimension.Y); y<=max(BrickDimension.Y); ++y) {
        int z=min(BrickDimension.Z); 
        if (!rs.clear(x,y,z-1)) {
          // Make sure the brick you find is not the same as the first one
          Brick support = brickMap.findBrick(x,y,z-1);
          if (firstSupport == null) {
            firstSupport = support;
          } else if (support != firstSupport) {
            return true;
          }
        }
      }
    }
    return false;
  }

  boolean safeToDisintegrate(RectangularSolid rs, BrickMap brickMap) {
    for (int x=min(BrickDimension.X); x<=max(BrickDimension.X); ++x) {
      for (int y=min(BrickDimension.Y); y<=max(BrickDimension.Y); ++y) {
        int z=max(BrickDimension.Z); 
        int cellAbove = z+1;
        if (!rs.clear(x,y,cellAbove)) {
          Brick supportedBrick = brickMap.findBrick(x,y,cellAbove);
          if (supportedBrick == null) {
            // No bricks on top of it, but then the cell should be clear
            throw new RuntimeException(this + " brick appears to have a brick on it at: [ " + x + "," + y + "," + cellAbove + "] but does not");
          }

          if (!supportedBrick.multipleSupports(rs, brickMap)) {
            return false;
          }
        }
      }
    }
    this._safeToDisintegrate = true;
    return this._safeToDisintegrate;
  }

  private boolean brickFootprintClear(RectangularSolid rs, int z) {
    for (int x=min(BrickDimension.X); x<=max(BrickDimension.X); ++x) {
      for (int y=min(BrickDimension.Y); y<=max(BrickDimension.Y); ++y) {
        if (!rs.clear(x,y,z)) {
          return false;
        }
      }
    }
    return true;
  }

  void add(RectangularSolid rs) {
    for (int x=min(BrickDimension.X); x<=max(BrickDimension.X); ++x) {
      for (int y=min(BrickDimension.Y); y<=max(BrickDimension.Y); ++y) {
        for (int z=min(BrickDimension.Z); z<=max(BrickDimension.Z); ++z) {
          rs.fill(x,y,z);
        }
      }
    }
  }

  @Override
  public String toString() {
    String out = _startCoords + " -> " + _endCoords;
    int lenX = BrickCoord.diff(_endCoords, _startCoords, BrickDimension.X)+1;
    int lenY = BrickCoord.diff(_endCoords, _startCoords, BrickDimension.Y)+1;
    int lenZ = BrickCoord.diff(_endCoords, _startCoords, BrickDimension.Z)+1;

    if (lenX != 1) {
      out = out + " X Length: " + lenX;
    }
    if (lenY != 1) {
      out = out + " Y Length: " + lenY;
    }
    if (lenZ != 1) {
      out = out + " Z Length: " + lenZ;
    }
    if (lenX == 1 && lenY == 1 && lenZ == 1) {
      out = out + " 1x1x1 cube";
    }
    if (this._safeToDisintegrate) {
      out = out + " (safe to disintegrate)";
    } else {
      out = out + " (CAN NOT be safely disintegrated)";
    }
    return out;
  }

  private BrickCoord _startCoords;
  private BrickCoord _endCoords;
  private boolean _safeToDisintegrate;

}
