package aoc2023;

class MapEntry implements java.lang.Comparable<MapEntry> {

  public static long rootSrc() {
    return ROOT_SOURCE;
  }
  @Override
  public int compareTo(MapEntry right) {
    Long lsrc = this.src();
    Long rsrc = right.src();
    return lsrc.compareTo(rsrc);
  }

  @Override
  public String toString() {
    long destStart = this._dest;
    long destEnd = this._dest + this._len;

    if (this._src >= 0) {
      long srcStart = this._src;
      long srcEnd = this._src + this._len;
      return srcStart + "-" + srcEnd + " -> " + destStart + "-" + destEnd;
    } else {
      return destStart + "-" + destEnd;
    }
  }

  MapEntry(String line) throws NumberFormatException {
    String[] tokens = line.split("\\s+"); 
    this._dest = Long.parseLong(tokens[0]);
    this._src = Long.parseLong(tokens[1]);
    this._len = Long.parseLong(tokens[2]);
  }

  MapEntry(String dest, String len) throws NumberFormatException {
    this._dest = Long.parseLong(dest);
    this._len = Long.parseLong(len);
    this._src = this._dest;
  }

  long srcStart() {
    return _src;
  }

  long srcEnd() {
    return _src + _len;
  }

  long src() {
    return _src;
  }

  long dest() {
    return _dest;
  }

  long len() {
    return _len;
  }

  boolean canMap(long value) {
    return (this._src <= value && value < (this._src + this._len));
  }

  long map(long value) {
    if (!canMap(value)) {
      System.err.println("oops\n");
      System.exit(4);
    }
    long offset = value - this._src; 
    return this._dest + offset;
  }

  long _len;
  long _src;
  long _dest;

  static long ROOT_SOURCE=-2;
}
