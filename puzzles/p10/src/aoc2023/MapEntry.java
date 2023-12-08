package aoc2023;

class MapEntry implements java.lang.Comparable<MapEntry> {

  @Override
  public int compareTo(MapEntry right) {
    Integer ldest = this.dest();
    Integer rdest = right.dest();
    return rdest.compareTo(ldest);
  }

  @Override
  public String toString() {
    int srcStart = this._src;
    int srcEnd = this._src + this._len;
    int destStart = this._dest;
    int destEnd = this._dest + this._len;

    return srcStart + "-" + srcEnd + " -> " + destStart + "-" + destEnd;
  }

  MapEntry(String line) throws NumberFormatException {
    String[] tokens = line.split("\\s+"); 
    this._dest = Integer.parseInt(tokens[0]);
    this._src = Integer.parseInt(tokens[1]);
    this._len = Integer.parseInt(tokens[2]);
  }

  private int dest() {
    return _dest;
  }

  int _len;
  int _src;
  int _dest;
}
