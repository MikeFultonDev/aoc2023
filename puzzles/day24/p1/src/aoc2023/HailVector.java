package aoc2023;
import java.io.Reader;
import java.io.StringReader;
import java.io.StreamTokenizer;

class HailVector {
  HailVector(String hailLine) throws java.io.IOException {
    this._hailLine = hailLine;
    parse();
  }

  public double inclination() {
    return this._inclination;
  }

  public double C() {
    return this._C;
  }

  public boolean parallelTo(HailVector other) {
    return this._inclination == other._inclination;
  }

  public boolean before(double x, double y) {
    // This point is _after_ the start point IF 
    // x is AFTER the start and the run is positive
    //   AND 
    //     y is AFTER the start and the rise is positive
    //       OR 
    //     y is BEFORE the start and the rise is negative
    //
    // OR
    // x is BEFORE the start and the run is negative
    //   AND ...
    //
    if (x >= this._startX) {
      if (this._run >= 0) {
        if (y >= this._startY) {
          if (this._rise >= 0) {
            return true;
          }
        } else if (this._rise <= 0) {
          return true;
        }
      }
    } else if (x <= this._startX) {
      if (this._run <= 0) {
        if (y >= this._startY) {
          if (this._rise >= 0) {
            return true;
          }
        } else if (this._rise <= 0) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return "y = " + this._C + " + " + this._inclination + "x";
  }

  private void parse() throws java.io.IOException {
    Reader r = new StringReader(this._hailLine);
    StreamTokenizer st = new StreamTokenizer(r);
    int token;

    // Sample line: 19, 13, 30 @ -2,  1, -2
    // No error checking for invalid lines

    double lastVal = 0;

    double nums[] = new double[6];
    int num = 0;
    while (true) {
      token = st.nextToken();
      if (token == StreamTokenizer.TT_EOF || st.ttype == ',' || st.ttype == '@') {
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

    this._startX = nums[0];
    this._startY = nums[1];
    double run = nums[3];
    double rise = nums[4];

    this._rise = rise;
    this._run = run;
    this._inclination = rise/run;
    this._C = this._startY - this._inclination * this._startX;
  }

  private double _startX;
  private double _startY;
  private double _rise;
  private double _run;
  private double _inclination;
  private double _C;

  String _hailLine;
}
