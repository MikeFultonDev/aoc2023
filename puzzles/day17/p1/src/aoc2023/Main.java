package aoc2023;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

class Main {
  public static void main(String args[]) throws java.io.IOException, NumberFormatException {
    if (args.length != 1) {
      System.out.printf("Syntax: java %s <cityDiagram>\n", Main.class.getName());
      System.exit(4);
    }

    String cityDiagramFile = args[0];

    System.out.printf("Processing %s\n", cityDiagramFile);

    Main main = new Main(cityDiagramFile); 
    if (main != null) {
      if (!main.findPath()) {
        main.printResult();
      }
    }
  }


  private boolean findPath() throws java.io.IOException {
    List<String> blockLines = new ArrayList<String>();

    BufferedReader br = new BufferedReader(new FileReader(this._cityDiagramFile));
    String l;
    while ((l = br.readLine()) != null) {
      String trimLine = l.trim();
      if (!trimLine.equals("")) {
        blockLines.add(trimLine);
      }
    }
    BlockDiagram cityDiagram = new BlockDiagram(blockLines);
    System.out.println(cityDiagram);

    this._minimalHeatLoss = cityDiagram.minimalHeatLoss();

    return false;
  }

  Main(String cityDiagramFile) throws java.io.IOException {
    this._cityDiagramFile = cityDiagramFile;
  }

  private void printResult() {
    System.out.println("The product of the groups is: " + this._minimalHeatLoss);
  }

  private String _cityDiagramFile;
  private long _minimalHeatLoss;
}

