package aoc2023;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

class BlockDiagram {
  BlockDiagram(List<String> blockLines) {
    int y=0;
    int maxX=0;
    for (String blockLine : blockLines) {
      int curXEnd = blockLine.length();
      if (maxX == 0) {
        maxX = curXEnd;
        this._blockMap = new Block[maxX][blockLines.size()];
      } else if (curXEnd != maxX) {
        throw new RuntimeException("Expected a matrix - line: " + y + " is not the same length as the previous length line");
      }
      for (int x=0; x < maxX; ++x) {
        this._blockMap[x][y] = new Block(blockLine.charAt(x), x, y);
      }
      ++y;
    }
    this._minX = 0;
    this._minY = 0;
    this._maxX = maxX;
    this._maxY = y;

    this._startBlock = this._blockMap[this.minX()][this.minY()];
    this._endBlock = this._blockMap[this.maxX()-1][this.maxY()-1];
    this._shortest = this.maxX() * this.maxY();
  }

  // Need to recode using Dijstra's algorithm: https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
  List<Block> findShortestPath(Block source, Block end, List<Block> currentPath) {
    if (source.touched()) {
      return null;
    }
    if (source.equals(end)) {
      int currentCost = cost(currentPath);
      if (currentCost < this._shortest) {
        System.out.println("Found path of length: " + currentCost + " " + currentPath);
        this._shortest = currentCost;
      }
      List<Block> clonedCurrentPath = new ArrayList<Block>(currentPath);
      return clonedCurrentPath;
    }
    source.touch();

    List<Block> shortestPath = null;
    for (Block next : source.targets(currentPath, this)) {
      List<Block> nextPath = new ArrayList<Block>(currentPath);
      nextPath.add(next);
      List<Block> path = findShortestPath(next, end, nextPath);
      if (path != null) {
        if (shortestPath == null) {
          shortestPath = path;
        } else if (path.size() < shortestPath.size()) {
          shortestPath = path;
        }
      }
    }

    source.clear();
    return shortestPath;
  }

  List<Block> findShortestPath() {
    List<Block> currentPath = new ArrayList<Block>();
    return findShortestPath(this._startBlock, this._endBlock, currentPath);
  }

  long minimalHeatLoss() {
    findShortestPath();
    return this._shortest;
  }

  Block[][] blockMap() {
    return this._blockMap;
  }

  @Override 
  public String toString() {
    String out="";
    for (int y=minY(); y<maxY(); ++y) {  
      for (int x=minX(); x<maxX(); ++x) {  
        out = out + this._blockMap[x][y];
      }
      out = out + System.lineSeparator();
    }
    return out;
  }
  int minX() { return this._minX; }
  int minY() { return this._minY; }
  int maxX() { return this._maxX; }
  int maxY() { return this._maxY; }

  private int cost(List<Block> path) {
    int tot=0;
    for (Block block : path) {
      tot += block.cost();
    }
    return tot;
  }
  private int _minX;
  private int _maxX;
  private int _minY;
  private int _maxY;
  private int _shortest;

  private Block _startBlock;
  private Block _endBlock;
  private Block[][] _blockMap;
}
