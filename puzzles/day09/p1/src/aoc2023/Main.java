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
      System.out.printf("Syntax: java %s <history>\n", Main.class.getName());
      System.exit(4);
    }

    String historyFile = args[0];

    System.out.printf("Processing %s\n", historyFile);

    Main main = new Main(historyFile); 
    if (main != null) {
      if (!main.processHistorys()) {
        main.printResult();
      }
    }
  }

  Main(String historyFile) {
    this._historyFile = historyFile;
  }

  private boolean processHistorys() {
    ArrayList<String> lines = new ArrayList<String>();
    try {
      BufferedReader br = new BufferedReader(new FileReader(this._historyFile));
      String l;
      while ((l = br.readLine()) != null) {
        String trimline = l.trim();
        if (!trimline.equals("")) {
          lines.add(trimline);
        }
      }

    } catch (IOException e) {
      System.err.println("Error " + e + " encountered trying to process history file " + this._historyFile);
      return true;
    }

    this._tot = 0;
    for (String line : lines) {
      History history = new History(line);
      long next = history.next();
      System.out.println(history);
      this._tot += next;
    }

    return false;
  }

  private void printResult() {
    System.out.println("Total is: " + this._tot);
  }

  private long _tot;
  private String _historyFile;
}

