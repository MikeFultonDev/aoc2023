package aoc2023;
import java.io.Reader;
import java.io.StringReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;

class WiringConnection {
  WiringConnection(String wiringLine) throws java.io.IOException {
    this._wiringLine = wiringLine;
    parse();
  }

  private void parse() throws java.io.IOException {
    Reader r = new StringReader(this._wiringLine);
    StreamTokenizer st = new StreamTokenizer(r);
    int token;

    // Sample line: jqt: rhn xhk nvd
    // No error checking for invalid lines

    String lastTarget = null;

    // Use a Lambda expression to count the number of characters
    long wiringWords = this._wiringLine.chars().filter(ch -> ch == ' ').count();
    String wiringTokens[] = new String[wiringWords];
    int count = 0;
    while (true) {
      token = st.nextToken();
      if (token == StreamTokenizer.TT_EOF || st.ttype == ':') {
        wiringTokens[count++] = lastTarget;
        if (token == StreamTokenizer.TT_EOF) {
          break;
        }
      } else if (st.ttype == StreamTokenizer.TT_NUMBER) {
        throw new java.io.IOException("Unexpected number: " + st.nval + " encountered");
      } else if (st.ttype == StreamTokenizer.TT_WORD) {
        lastTarget = st.sval;
      } else {
        throw new java.io.IOException("Unexpected token: " + st.ttype + " encountered");
      }
    }

    this._source = wiringTokens[0];
    this._targets = new ArrayList<String>();
    for (count=1; count<wiringTokens.length; ++count) {
      this._targets.add(wiringTokens[count]);
    }
  }

  @Override
  public String toString() {
    String out = this._source + " -> ";
    for (String target: this._targets) {
      out = out + target;
    }
    return out;
  }

  private String _source;
  private List<String> _targets;
}
