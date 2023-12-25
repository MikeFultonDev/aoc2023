package aoc2023;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

class Main {
  public static void main(String args[]) throws java.io.IOException, NumberFormatException {
    if (args.length != 2) {
      System.out.printf("Syntax: java %s <steps> <plot>\n", Main.class.getName());
      System.exit(4);
    }

    String steps = args[0];
    String plotFile = args[1];

    System.out.printf("Processing %s\n", plotFile);

    Main main = new Main(steps, plotFile); 
    if (main != null) {
      if (!main.walk()) {
        main.printResult();
      }
    }
  }

  Main(String steps, String plotFile) throws java.io.IOException, NumberFormatException {
    this._steps = Integer.parseInt(steps);
    this._plotFile = plotFile;
  }

  private boolean walk() throws java.io.IOException {
    List<String> plotLines = new ArrayList<String>();

    BufferedReader br = new BufferedReader(new FileReader(this._plotFile));
    String l;
    while ((l = br.readLine()) != null) {
      String trimLine = l.trim();
      if (!trimLine.equals("")) {
        plotLines.add(trimLine);
      }
    }
    PlotMap plotMap = new PlotMap(plotLines); 
    System.out.println(plotMap);
    return false;
  }

  private void printResult() {
    System.out.println("Number of plots in " + this._steps + " steps is: " + this._tot);
  }

  private int _steps;
  private long _tot;
  private String _plotFile;
}

