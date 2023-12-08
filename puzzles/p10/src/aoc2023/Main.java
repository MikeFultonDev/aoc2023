package aoc2023;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

class Main {
  public static void main(String args[]) {
    if (args.length != 1) {
      System.out.printf("Syntax: java %s <race>\n", Main.class.getName());
      System.exit(4);
    }

    String raceFile = args[0];

    System.out.printf("Processing %s\n", raceFile);

    Main main = new Main(raceFile); 
    if (main != null) {
      if (!main.processMaps()) {
        main.printResult();
      }
    }
  }

  Main(String raceFile) {
    this._raceFile = raceFile;
  }

  private boolean processMaps() {
    ArrayList<String> lines = new ArrayList<String>();
    try {
      BufferedReader br = new BufferedReader(new FileReader(this._raceFile));
      String l;
      while ((l = br.readLine()) != null) {
        lines.add(l);
      }
    } catch (IOException e) {
      System.err.println("Error " + e + " encountered trying to process race file " + this._raceFile);
      return true;
    }

    String seeds = lines.get(0);
    ArrayList<Map> maps = new ArrayList<Map>();

    Map seedMap = new Map(seeds); 
    maps.add(seedMap);
    int curLine = 1;
    while (curLine < lines.size()) {
      Map map = new Map(curLine, lines);
      maps.add(map);
      curLine = map.lastLine()+1;
    }

    for (int mapNum = 0; mapNum < maps.size(); ++mapNum) {
      Map map = maps.get(mapNum);
      System.out.println(map);
    }

    // Map the couple numbers

    long winnerSeed=-1;
    long winnerResult = Long.MAX_VALUE;

    TreeSet<MapEntry> seedEntries = maps.get(0).mapset();
    for (MapEntry seedEntry : seedEntries) {
      System.out.println("Processing seed range: " + seedEntry.srcStart() + "-" + seedEntry.srcEnd());
      for (long seed=seedEntry.srcStart(); seed<seedEntry.srcEnd(); seed += 1000) {
        long src = seed;
        for (int mapNum = 0; mapNum < maps.size(); ++mapNum) { 
          //System.out.print(src + "->");
          Map map = maps.get(mapNum);
          src = map.map(src);
        }
        if (src < winnerResult) {
          winnerResult = src;
          winnerSeed = seed;
        }
        //System.out.println("\n");
      }
      System.out.println("Winner so far: " + winnerSeed + " -> " + winnerResult);
    }

    // Use 'winner so far as starter point and go +/- 1000 from it
    for (long seed=winnerSeed-1000; seed<winnerSeed+1000; ++seed) {
      long src = seed;
      for (int mapNum = 0; mapNum < maps.size(); ++mapNum) { 
        //System.out.print(src + "->");
        Map map = maps.get(mapNum);
        src = map.map(src);
      }
      if (src < winnerResult) {
        winnerResult = src;
        winnerSeed = seed;
      }
    }
    System.out.println("Refined winner: " + winnerSeed + " -> " + winnerResult);

    _result = "Winner: " + winnerSeed + " -> " + winnerResult;
    return false;
  }

  private void printResult() {
    System.out.println(_result);
  }

  private String _result;
  private String _raceFile;
}

