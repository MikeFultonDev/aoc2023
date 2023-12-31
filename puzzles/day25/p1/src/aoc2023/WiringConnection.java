package aoc2023;
import java.io.Reader;
import java.io.StringReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;

class WiringConnection {
  WiringConnection(String wiringLine) throws java.io.IOException {
    parse(wiringLine);
  }

  private void parse(String wiringLine) throws java.io.IOException {
    Reader r = new StringReader(wiringLine);
    StreamTokenizer st = new StreamTokenizer(r);
    int token;

    // Sample line: jqt: rhn xhk nvd
    // No error checking for invalid lines

    this._source = null;

    this._targets = new ArrayList<String>();
    while (true) {
      token = st.nextToken();
      if (token == StreamTokenizer.TT_EOF || st.ttype == ':') {
        if (token == StreamTokenizer.TT_EOF) {
          break;
        }
      } else if (st.ttype == StreamTokenizer.TT_NUMBER) {
        throw new java.io.IOException("Unexpected number: " + st.nval + " encountered");
      } else if (st.ttype == StreamTokenizer.TT_WORD) {
        if (this._source == null) {
          this._source = st.sval;
        } else {
          this._targets.add(st.sval);
        }
      } else {
        throw new java.io.IOException("Unexpected token: " + st.ttype + " encountered");
      }
    }
  }

  @Override
  public String toString() {
    String out = this._source + " ->";
    for (String target: this._targets) {
      out = out + " " + target;
    }
    return out;
  }

  private String _source;
  private List<String> _targets;
}
