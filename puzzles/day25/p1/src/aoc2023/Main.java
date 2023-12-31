package aoc2023;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

class Main {
  public static void main(String args[]) throws java.io.IOException, NumberFormatException {
    if (args.length != 1) {
      System.out.printf("Syntax: java %s <wiringDiagram>\n", Main.class.getName());
      System.exit(4);
    }

    String wiringDiagramFile = args[0];

    System.out.printf("Processing %s\n", wiringDiagramFile);

    Main main = new Main(wiringDiagramFile); 
    if (main != null) {
      if (!main.cutWires()) {
        main.printResult();
      }
    }
  }

  Main(String wiringDiagramFile) throws java.io.IOException {
    this._wiringDiagramFile = wiringDiagramFile;
  }

  private boolean cutWires() throws java.io.IOException {
    List<String> wiringLines = new ArrayList<String>();

    BufferedReader br = new BufferedReader(new FileReader(this._wiringDiagramFile));
    String l;
    while ((l = br.readLine()) != null) {
      String trimLine = l.trim();
      if (!trimLine.equals("")) {
        wiringLines.add(trimLine);
      }
    }
    WiringDiagram wiringDiagram = new WiringDiagram(wiringLines); 
    System.out.println(wiringDiagram);

    this._groupProduct = wiringDiagram.groupProduct();

    return false;
  }

  private void printResult() {
    System.out.println("The product of the groups is: " + this._groupProduct);
  }

  private String _wiringDiagramFile;
  private long _groupProduct;
}

