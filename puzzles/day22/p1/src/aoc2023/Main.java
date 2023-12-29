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
    if (args.length != 1) {
      System.out.printf("Syntax: java %s <bricks>\n", Main.class.getName());
      System.exit(4);
    }

    String bricksFile = args[0];

    System.out.printf("Processing %s\n", bricksFile);

    Main main = new Main(bricksFile); 
    if (main != null) {
      if (!main.disintegrate()) {
        main.printResult();
      }
    }
  }

  Main(String bricksFile) throws java.io.IOException {
    this._bricksFile = bricksFile;
  }

  private boolean disintegrate() throws java.io.IOException {
    List<String> brickLines = new ArrayList<String>();

    BufferedReader br = new BufferedReader(new FileReader(this._bricksFile));
    String l;
    while ((l = br.readLine()) != null) {
      String trimLine = l.trim();
      if (!trimLine.equals("")) {
        brickLines.add(trimLine);
      }
    }
    BrickMap brickMap = new BrickMap(brickLines); 

    this._safeToDisintegrate = brickMap.safeToDisintegrate();

    return false;
  }

  private void printResult() {
    System.out.println(this._safeToDisintegrate + " bricks are safe to disintegrate");
  }

  private int _safeToDisintegrate;
  private String _bricksFile;
}

