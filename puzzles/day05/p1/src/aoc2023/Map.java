package aoc2023;
import java.util.ArrayList;
import java.util.TreeSet;

class Map {
  Map(String seeds) {
    this._name = "seeds:";
    this._firstLine = 1;
    this._lastLine = 1;
    this._lines = new ArrayList<String>();
    this._lines.add(seeds);

    // The seed line has multiple ranges on it as pairs
    // There is no 'source' since this is the first entry

    this._mapset = new TreeSet<MapEntry>();
    String[] tokens = seeds.split("\\s+");

    // Skip over 'seeds:' header
    for (int i=1; i<tokens.length; i+=2) {
      MapEntry mapEntry = new MapEntry(tokens[i], tokens[i+1]);
      this._mapset.add(mapEntry);
    }
  }

  Map(int curLine, ArrayList<String> lines) {
    this._firstLine = curLine;
    this._lines = lines;

    curLine = curLine;

    // Skip any blank lines
    while (this._lines.get(curLine).trim().equals("")) {
      ++curLine;
    }
    this._name = this._lines.get(curLine);
    ++curLine;

    // Skip any blank lines
    while (this._lines.get(curLine).trim().equals("")) {
      ++curLine;
    }

    // Process the mapping information
    this._mapset = new TreeSet<MapEntry>();
    while (curLine < _lines.size() && !(this._lines.get(curLine).trim().equals(""))) {
      // Each line has <dest> <src> <len>
      MapEntry mapEntry = new MapEntry(this._lines.get(curLine).trim());
      this._mapset.add(mapEntry);
      ++curLine;
    }
    this._lastLine = curLine;
  }

  long firstDest() {
    return this._mapset.first().dest();
  }
  long firstLen() {
    return this._mapset.first().len();
  }

  long map(long value) {
    for (MapEntry entry : this._mapset) {
      if (entry.canMap(value)) {
        return entry.map(value);
      }
    }
    return value;
  }
  int lastLine() {
    return _lastLine;
  }
  String name() {
    return _name;
  }

  TreeSet<MapEntry> mapset() {
    return _mapset;
  }

  @Override
  public String toString() {
    StringBuffer out = new StringBuffer(_name);
    out.append("\n  ");
    for (MapEntry entry : this._mapset) {
      out = out.append(entry);
      out = out.append("\n  ");
    }

    return new String(out);
  }
  private String _name;
  private int _firstLine;
  private int _lastLine;
  private ArrayList<String> _lines;
  private TreeSet<MapEntry> _mapset;
}
