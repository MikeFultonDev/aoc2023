package aoc2023;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

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
    int numCards = 0;
    ArrayList<String> lines = new ArrayList<String>();
    
    try {
      BufferedReader br = new BufferedReader(new FileReader(this._scratchcardFile));
      String l;
      while ((l = br.readLine()) != null) {
        lines.add(l);
        ++numCards;
      }
    } catch (IOException e) {
      System.err.println("Error " + e + " encountered trying to process scratch card file " + this._scratchcardFile);
      return true;
    }

    long multiples[] = new long[numCards];
    Arrays.fill(multiples, 1);

    this._scratchcards = new ArrayList<Scratchcard>();
    int i=0; 
    for (String line : lines) {
      this._scratchcards.add(new Scratchcard(i++, line));
    }
    for (Scratchcard scratchcard : this._scratchcards) {
      this._totalCards += scratchcard.calculateCards(multiples);
    }
    return false;
  }
  private void printResult() {
    System.out.println("There are " + this._totalCards + " total cards");
  }
  private Main(String scratchcardFile) {
    this._scratchcardFile = scratchcardFile;
  }

  private List<Scratchcard> _scratchcards;
  private long _totalCards;
  private String _scratchcardFile;
}

