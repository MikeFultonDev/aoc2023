package aoc2023;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

class Trailmap {
  Trailmap(List<String> trailLines) {
    int y=0;
    int maxX=0;
    for (String line : trailLines) {
      int curXEnd = line.length();
      if (maxX == 0) {
        maxX = curXEnd;
        this._trailmap = new TrailCell[maxX][trailLines.size()];
      } else if (curXEnd != maxX) {
        throw new RuntimeException("Expected a matrix - line: " + y + " is not the same length as the previous length line");
      }
      for (int x=0; x < maxX; ++x) {
        this._trailmap[x][y] = TrailCell.create(line.charAt(x));
      }
      ++y;
    }
         
    this._start = new TrailCoord(1,0);
    this._end = new TrailCoord(this._trailmap[0].length-2, this._trailmap.length-1);

    setCell(this._start, TrailCell.Start);
    setCell(this._end, TrailCell.End);
  }

  void setCell(TrailCoord coord, TrailCell cell) {
    this._trailmap[coord.x()][coord.y()] = cell;
  }

  boolean canHead(TrailCoord newLocation, TrailDirection direction) {
    TrailCell cell = this._trailmap[newLocation.x()][newLocation.y()];
    if (cell == TrailCell.Forest || cell == TrailCell.Start || cell == TrailCell.PathSeen) { 
      return false;
    }
    if (cell == TrailCell.Path || cell == TrailCell.End) { 
      return true;
    }
    switch (direction) {
      case North: 
        return (cell == TrailCell.SlopeNorth);
      case South:
        return (cell == TrailCell.SlopeSouth);
      case East:
        return (cell == TrailCell.SlopeEast);
      case West:
        return (cell == TrailCell.SlopeWest);
      default:
        throw new RuntimeException("Unexpected direction");
    }
  }

  boolean isSeen(TrailCoord coord) {
    TrailCell cell = this._trailmap[coord.x()][coord.y()];
    return (cell == TrailCell.PathSeen || cell == TrailCell.Start);
  }

  void markSeen(TrailCoord coord) {
    TrailCell cell = this._trailmap[coord.x()][coord.y()];
    if (cell == TrailCell.Path) {
      this._trailmap[coord.x()][coord.y()]= TrailCell.PathSeen;
    }
  }
  void markUnseen(TrailCoord coord) {
    TrailCell cell = this._trailmap[coord.x()][coord.y()];
    if (cell == TrailCell.PathSeen) {
      this._trailmap[coord.x()][coord.y()]= TrailCell.Path;
    }
  }

  int longestPath(TrailCoord location, int length) {
    int curMax = 0;

    if (location.equals(this._end)) {
      System.out.println("Reached end in: " + length + " steps");
      return length;
    }

    if (isSeen(location)) {
      return 0;
    }
    System.out.println("Coordinate: " + location);

    markSeen(location);

    TrailCoord northLocation = new TrailCoord(location, TrailDirection.North);
    TrailCoord southLocation = new TrailCoord(location, TrailDirection.South);
    TrailCoord westLocation = new TrailCoord(location, TrailDirection.West);
    TrailCoord eastLocation = new TrailCoord(location, TrailDirection.East);
    if (canHead(northLocation, TrailDirection.North)) {
      int northMax = longestPath(northLocation, length+1);
      if (northMax > curMax) {
        curMax = northMax;
      }
    }
    if (canHead(southLocation, TrailDirection.South)) {
      int southMax = longestPath(southLocation, length+1);
      if (southMax > curMax) {
        curMax = southMax;
      }
    }
    if (canHead(westLocation, TrailDirection.West)) {
      int westMax = longestPath(westLocation, length+1);
      if (westMax > curMax) {
        curMax = westMax;
      }
    }
    if (canHead(eastLocation, TrailDirection.East)) {
      int eastMax = longestPath(eastLocation, length+1);
      if (eastMax > curMax) {
        curMax = eastMax;
      }
    }

    markUnseen(location);

    return curMax; 
  }

  int longestPath() {
    // Walk from Start to End in all possible ways, and return the longest path
    // Start out one step south of the start and then can avoid all range checks

    int length = 1;
    return longestPath(new TrailCoord(this._start, TrailDirection.South), length);
  }

  @Override 
  public String toString() {
    String out = "";
    for (int y=0; y<this._trailmap.length; ++y) {
      for (int x=0; x<this._trailmap[0].length; ++x) {
        out = out + this._trailmap[x][y];
      }
      out = out + System.lineSeparator();
    }
    return out;
  }

  private TrailCoord _start;
  private TrailCoord _end;
  private TrailCell[][] _trailmap;
}
