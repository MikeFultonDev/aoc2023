package aoc2023;
import java.util.List;
import java.util.ArrayList;
import java.io.StringReader;
import java.io.Reader;
import java.io.StreamTokenizer;

class Workflow {
  Workflow(String workflowLine) throws java.io.IOException {
    this._workflowLine = workflowLine;
    parse();
  }

  private void parse() throws java.io.IOException {
    Reader r = new StringReader(this._workflowLine);
    StreamTokenizer st = new StreamTokenizer(r);
    List<Object> tokens = new ArrayList<Object>();
    int token;

    // Sample line: px{a<2006:qkq,m>2090:A,rfg}
    // No error checking for invalid lines
    String curVar = "";
    double curVal = 0;
    WorkflowCondition curCondition = null; 

    st.nextToken();
    this._workflowName = st.sval;
    this._conditions = new ArrayList<WorkflowCondition>();

    while ((token = st.nextToken()) != StreamTokenizer.TT_EOF) {
      if (st.ttype == StreamTokenizer.TT_WORD) {
        curVar = st.sval;
      } else if (st.ttype == StreamTokenizer.TT_NUMBER) {
        curVal = st.nval;
      } else {
        if (st.ttype == '<' || st.ttype == '>') {
          curCondition = new WorkflowCondition();
          curCondition.setVar(curVar);
          curCondition.setComparison(st.ttype);
        } else if (st.ttype == ':') {
          curCondition.setVal(curVal);
        } else if (st.ttype == ',') {
          curCondition.setTarget(curVar);
          this._conditions.add(curCondition); 
          curCondition = null; 
        }
      }
    }
    this._workflowTarget = curVar;
  }

  public String toString() {
    String out = "Workflow: " + _workflowName + " : ";
    for (WorkflowCondition wfc : _conditions) {
      out = out + wfc + ", ";
    }
    out = out + _workflowTarget;
    return out;
  }

  private String _workflowLine;
  private String _workflowName;
  private String _workflowTarget;
  private List<WorkflowCondition> _conditions;
}

