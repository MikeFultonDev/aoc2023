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

    return destStart + "-" + destEnd + " <- " + srcStart + "-" + srcEnd;
  }

  MapEntry(String line) throws NumberFormatException {
    String[] tokens = line.split("\\s+"); 
    this._dest = Integer.parseInt(tokens[0]);
    this._src = Integer.parseInt(tokens[1]);
    this._len = Integer.parseInt(tokens[2]);
  }

  int dest() {
    return _dest;
  }

  int len() {
    return _len;
  }

  boolean canMap(int value) {
    return (this._dest <= value && value < (this._dest + this._len));
  }

  int map(int value) {
    if (!canMap(value)) {
      System.err.println("oops\n");
      System.exit(4);
    }
    int offset = value - this._dest; 
    return this._src + offset;
  }

  int _len;
  int _src;
  int _dest;
}
