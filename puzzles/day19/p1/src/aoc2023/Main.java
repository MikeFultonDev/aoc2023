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
      System.out.printf("Syntax: java %s <workflow>\n", Main.class.getName());
      System.exit(4);
    }

    String workflowFile = args[0];

    System.out.printf("Processing %s\n", workflowFile);

    Main main = new Main(workflowFile); 
    if (main != null) {
      if (!main.processWorkflows()) {
        main.printResult();
      }
    }
  }

  Main(String workflowFile) {
    this._workflowFile = workflowFile;
  }

  private boolean processWorkflows() {
    ArrayList<String> lines = new ArrayList<String>();
    try {
      BufferedReader br = new BufferedReader(new FileReader(this._workflowFile));
      String l;
      while ((l = br.readLine()) != null) {
        String trimline = l.trim();
        if (!trimline.equals("")) {
          lines.add(trimline);
        }
      }

    } catch (IOException e) {
      System.err.println("Error " + e + " encountered trying to process workflow file " + this._workflowFile);
      return true;
    }

    return false;
  }

  private void printResult() {
    System.out.println("Total is: " + this._tot);
  }

  private long _tot;
  private String _workflowFile;
}

