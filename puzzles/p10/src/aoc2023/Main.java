package aoc2023;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

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

    int curLine = 1;
    while (curLine < lines.size()) {
      Map map = new Map(curLine, lines);
      maps.add(map);
      curLine = map.lastLine()+1;
    }

    int lastMapNum = maps.size()-1;
    for (int mapNum = lastMapNum; mapNum >=0; --mapNum) {
      Map map = maps.get(mapNum);
      System.out.println(map);
    }

    Map lastMap = maps.get(lastMapNum);
    int smallestDest = lastMap.firstDest();
    int smallestLen = lastMap.firstLen();
    for (int len=0; len<smallestLen; ++len) {
      int dest = smallestDest + len;
      for (int mapNum = lastMapNum; mapNum >=0; --mapNum) {
        System.out.print(dest + "->");
        if (dest < 0) {
          System.out.println(" no map\n");
          break;
        }
        Map map = maps.get(mapNum);
        dest = map.map(dest);
      }
      if (dest >= 0) {
        System.out.println(" Success\n");
        break;
      } else {
        System.out.println(" no map\n");
      }
    }

    return false;
  }
  private void printResult() {
    System.out.println("result");
  }

  private String _raceFile;
}

