package aoc2023;
import java.io.Reader;
import java.io.StringReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;

class WiringConnection {
  WiringConnection(WiringNode source) {
    this._source = new WiringNode(source.name());
    this._targets = new ArrayList<WiringNode>();
  }

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

    this._targets = new ArrayList<WiringNode>();
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
          this._source = new WiringNode(st.sval);
        } else {
          this._targets.add(new WiringNode(st.sval));
        }
      } else {
        throw new java.io.IOException("Unexpected token: " + st.ttype + " encountered");
      }
    }
  }

  WiringNode source() {
    return this._source;
  }

  List<WiringNode> targets() {
    return this._targets;
  }

  @Override
  public String toString() {
    String out = this._source.name() + " ->";
    for (WiringNode target: this._targets) {
      out = out + " " + target.name();
    }
    return out;
  }

  private WiringNode _source;
  private List<WiringNode> _targets;
}
