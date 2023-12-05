package aoc2023;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class Main {
  public static void main(String args[]) {
    if (args.length != 1) {
      System.out.printf("Syntax: java %s <scratchcards>\n", Main.class.getName());
      System.exit(4);
    }

    String scratchcardFile = args[0];

    System.out.printf("Processing %s\n", scratchcardFile);

    Main main = new Main(scratchcardFile); 
    if (main != null) {
      if (!main.processCards()) {
        main.printResult();
      }
    }
  }

  private boolean processCards() {
    try {
      List<String> lines = Files.readAllLines(Paths.get(this._scratchcardFile), java.nio.charset.StandardCharsets.ISO_8859_1);
      this._scratchcards = new ArrayList<Scratchcard>();
      for (String line : lines) {
        this._scratchcards.add(new Scratchcard(line));
      }
    } catch (IOException e) {
      System.err.println("Error " + e + " encountered trying to process scratch card file " + this._scratchcardFile);
      return true;
    }
    return false;
  }
  private void printResult() {
    System.out.printf("Scratch cards are worth %d points\n", this._points);
  }
  private Main(String scratchcardFile) {
    this._scratchcardFile = scratchcardFile;
  }

  private List<Scratchcard> _scratchcards;
  private long _points;
  private String _scratchcardFile;
}

