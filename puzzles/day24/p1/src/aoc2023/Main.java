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
    this._startX = 7;
    this._startY = 7;
    this._endX = 27;
    this._endY = 27;
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

    this._crossings = hailMap.crossings(this._startX, this._startY, this._endX, this._endY);

    return false;
  }

  private void printResult() {
    System.out.println("There are: " + this._crossings + " crossings inside the range: [" + this._startX + "," + this._startY + "] -> [" + this._endX + "," + this._endY + "]" );
  }

  private String _hailmapFile;
  private int _crossings;

  private double _startX;
  private double _startY;
  private double _endX;
  private double _endY;
}

