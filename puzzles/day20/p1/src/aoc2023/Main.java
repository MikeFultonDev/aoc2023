package aoc2023;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

class Main {
  public static void main(String args[]) throws java.io.IOException {
    if (args.length != 1) {
      System.out.printf("Syntax: java %s <config>\n", Main.class.getName());
      System.exit(4);
    }

    String configFile = args[0];

    System.out.printf("Processing %s\n", configFile);

    Main main = new Main(configFile); 
    if (main != null) {
      if (!main.processConfigs()) {
        main.printResult();
      }
    }
  }

  Main(String configFile) throws java.io.IOException {
    this._configFile = configFile;
  }

  private boolean processConfigs() throws java.io.IOException {
    ArrayList<String> configLines = new ArrayList<String>();

    BufferedReader br = new BufferedReader(new FileReader(this._configFile));
    String l;
    while ((l = br.readLine()) != null) {
      String trimline = l.trim();
      if (!trimline.equals("")) {
        configLines.add(trimline);
      }
    }
    return false;
  }

  private void printResult() {
    System.out.println("Total is: " + this._tot);
  }

  private long _tot;
  private String _configFile;
}

