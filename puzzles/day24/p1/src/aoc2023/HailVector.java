package aoc2023;
import java.util.Reader;
import java.util.StringReader;
import java.util.StreamTokenizer;

class HailVector {
  HailVector(String hailLine) throws java.io.IOException {
    this._hailLine = hailLine;
    parse();
  }

  @Override
  public String toString() {
    return "[" + this._startX + "," + this._startY + "] -> [" + this._dirX + "," + this._dirY + "]";
  }

  private void parse() throws java.io.IOException {
    Reader r = new StringReader(this._workflowLine);
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
    this._dirX = nums[3];
    this._dirY = nums[4];
  }

  private double _startX;
  private double _startY;
  private double _dirX;
  private double _dirY;

  String _hailLine;
}
