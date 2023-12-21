package aoc2023;
import java.util.List;
import java.util.ArrayList;
import java.io.StringReader;
import java.io.Reader;
import java.io.StreamTokenizer;

class Part {
  Part(String partLine) throws java.io.IOException {
    this._partLine = partLine;
    parse();
  }
  private void parse() throws java.io.IOException {
    Reader r = new StringReader(this._partLine);
    StreamTokenizer st = new StreamTokenizer(r);
    List<Object> tokens = new ArrayList<Object>();
    double token;

    // Sample line: {x=2127,m=1623,a=2188,s=1013}
    // No error checking for invalid lines
    String curVar = "";
    double curVal;
    while ((token = st.nextToken()) != StreamTokenizer.TT_EOF) {
      if (st.ttype == StreamTokenizer.TT_WORD) {
        curVar = st.sval;
      } else if (st.ttype == StreamTokenizer.TT_NUMBER) {
        curVal = st.nval;
        if (curVar.equals("x")) {
          this._x = curVal;
        } else if (curVar.equals("m")) {
          this._m = curVal;
        } else if (curVar.equals("a")) {
          this._a  = curVal;
        } else if (curVar.equals("s")) {
          this._s = curVal;
        } else {
          System.err.println("Unable to parse line: " + this._partLine);
        }
      }
    }
  }

  private double _x;
  private double _m;
  private double _a;
  private double _s;

  private String _partLine;

  public long x() {
    return Double.valueOf(this._x).longValue();
  }
  public long m() {
    return Double.valueOf(this._m).longValue();
  }
  public long a() {
    return Double.valueOf(this._a).longValue();
  }
  public long s() {
    return Double.valueOf(this._s).longValue();
  }

  public String toString() {
    return "x: " + x() + " m: " + m() + " a: " + a() + " s: " + s();
  }
}

