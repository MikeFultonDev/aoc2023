package aoc2023;
import java.util.ArrayList;
import java.util.TreeSet;

class Map {
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
    _mapset = new TreeSet<MapEntry>();
    while (curLine < _lines.size() && !(this._lines.get(curLine).trim().equals(""))) {
      // Each line has <dest> <src> <len>
      MapEntry mapEntry = new MapEntry(this._lines.get(curLine).trim());
      _mapset.add(mapEntry);
      ++curLine;
    }
    this._lastLine = curLine;
  }

  int firstDest() {
    return _mapset.first().dest();
  }
  int firstLen() {
    return _mapset.first().len();
  }

  int map(int value) {
    for (MapEntry entry : _mapset) {
      if (entry.canMap(value)) {
        return entry.map(value);
      }
    }
    return -1;
  }
  int lastLine() {
    return _lastLine;
  }
  String name() {
    return _name;
  }

  @Override
  public String toString() {
    StringBuffer out = new StringBuffer(_name);
    out.append("\n  ");
    for (MapEntry entry : _mapset) {
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
