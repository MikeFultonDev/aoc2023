package aoc2023;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

class Main {
  public static void main(String args[]) throws java.io.IOException, NumberFormatException {
    if (args.length != 1) {
      System.out.printf("Syntax: java %s <trailmap>\n", Main.class.getName());
      System.exit(4);
    }

    String trailmapFile = args[0];

    System.out.printf("Processing %s\n", trailmapFile);

    Main main = new Main(trailmapFile); 
    if (main != null) {
      if (!main.walk()) {
        main.printResult();
      }
    }
  }

  Main(String trailmapFile) throws java.io.IOException {
    this._trailmapFile = trailmapFile;
  }

  private boolean walk() throws java.io.IOException {
    List<String> trailLines = new ArrayList<String>();

    BufferedReader br = new BufferedReader(new FileReader(this._trailmapFile));
    String l;
    while ((l = br.readLine()) != null) {
      String trimLine = l.trim();
      if (!trimLine.equals("")) {
        trailLines.add(trimLine);
      }
    }
    Trailmap trailMap = new Trailmap(trailLines); 
    System.out.println(trailMap);

    this._longestPath = trailMap.longestPath();

    return false;
  }

  private void printResult() {
    System.out.println("The longest path is: " + this._longestPath + " steps");
  }

  private String _trailmapFile;
  private int _longestPath;
}

