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

  int longestPath() {
    return 0;
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
