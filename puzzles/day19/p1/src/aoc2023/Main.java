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

  Main(String workflowFile) throws java.io.IOException {
    this._workflowFile = workflowFile;
  }

  private boolean processWorkflows() throws java.io.IOException {
    ArrayList<String> workflowLines = new ArrayList<String>();
    ArrayList<String> partLines = new ArrayList<String>();
    ArrayList<Workflow> workflows = new ArrayList<Workflow>();
    ArrayList<Part> parts = new ArrayList<Part>();
    ArrayList<String> lines = workflowLines;
    ProcessingState state = ProcessingState.ProcessingWorkflows;

    BufferedReader br = new BufferedReader(new FileReader(this._workflowFile));
    String l;
    while ((l = br.readLine()) != null) {
      String trimline = l.trim();
      if (!trimline.equals("")) {
        lines.add(trimline);
      } else {
        if (state == ProcessingState.ProcessingWorkflows) {
          state = ProcessingState.ProcessingParts;
          lines = partLines;
        }
      }
    }

    System.out.println("Parts");
    for (String part : partLines) {
      Part p = new Part(part);
      System.out.println(p);
      parts.add(p);
    }
    System.out.println();
    System.out.println("Workflows");
    for (String workflow : workflowLines) {
      Workflow w = new Workflow(workflow);
      System.out.println(w);
      workflows.add(w);
    }

    Workflow start = Workflow.find(workflows, "in");
    for (Part part : parts) {
      System.out.println("Part: " + part);
      Workflow cur = start;
      String next = cur.process(part);
      while (!next.equals("A") && !next.equals("R")) {
        System.out.println("workflow: " + cur);
        cur = Workflow.find(workflows, next);
        next = cur.process(part);
      }
      if (next.equals("A")) {
        _tot += part.rating();
        System.out.println("Part accepted. Total now: " + _tot);
      }
      if (next.equals("R")) {
        System.out.println("Part rejected");
      }
    }    

    return false;
  }

  private void printResult() {
    System.out.println("Total is: " + this._tot);
  }

  private long _tot;
  private String _workflowFile;
  private enum ProcessingState { ProcessingWorkflows, ProcessingParts };
}

