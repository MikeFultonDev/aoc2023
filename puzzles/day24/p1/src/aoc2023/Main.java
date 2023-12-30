package aoc2023;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

class Main {
  public static void main(String args[]) throws java.io.IOException, NumberFormatException {
    if (args.length != 1) {
      System.out.printf("Syntax: java %s <hailmap>\n", Main.class.getName());
      System.exit(4);
    }

    String hailmapFile = args[0];

    System.out.printf("Processing %s\n", hailmapFile);

    Main main = new Main(hailmapFile); 
    if (main != null) {
      if (!main.track()) {
        main.printResult();
      }
    }
  }

  Main(String hailmapFile) throws java.io.IOException {
    this._hailmapFile = hailmapFile;
  }

  private boolean track() throws java.io.IOException {
    List<String> hailLines = new ArrayList<String>();

    BufferedReader br = new BufferedReader(new FileReader(this._hailmapFile));
    String l;
    while ((l = br.readLine()) != null) {
      String trimLine = l.trim();
      if (!trimLine.equals("")) {
        hailLines.add(trimLine);
      }
    }
    Hailmap hailMap = new Hailmap(hailLines); 
    System.out.println(hailMap);

    this._crossings = hailMap.crossings();

    return false;
  }

  private void printResult() {
    System.out.println("The longest path is: " + this._crossings + " steps");
  }

  private String _hailmapFile;
  private int _crossings;
}

