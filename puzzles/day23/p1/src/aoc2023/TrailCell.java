package aoc2023;

enum TrailCell {
  Unk, Start, End, SlopeSouth, SlopeNorth, SlopeWest, SlopeEast, Forest, Path, PathSeen;

  static TrailCell create(char c) {
    switch (c) {
      case '.': return Path;
      case 'o': return PathSeen;
      case '#': return Forest;
      case '>': return SlopeEast;
      case '<': return SlopeWest;
      case '^': return SlopeNorth;
      case 'v': return SlopeSouth;
      case 'S': return Start;
      case 'E': return End;
    }
    return Unk;
  }

  @Override
  public String toString() {
    switch (this) {
      case Path : return ".";
      case PathSeen : return "o";
      case Forest : return "#";
      case SlopeEast : return ">";
      case SlopeWest : return "<";
      case SlopeNorth : return "^";
      case SlopeSouth : return "v";
      case Start : return "S";
      case End : return "E";
    }
    return "?";
  }
}
