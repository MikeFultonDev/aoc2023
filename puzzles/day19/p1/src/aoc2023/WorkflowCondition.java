package aoc2023;

class WorkflowCondition {
  WorkflowCondition() {
  }

  void setVar(String var) {
    this._var = var;
  }
  void setComparison(int comparison) {
    if (comparison == '<') {
      this._comparison = Comparison.LessThan;
    } else if (comparison == '>') {
      this._comparison = Comparison.GreaterThan;
    } else {
      System.err.println("Invalid Comparison: " + comparison);
    }
  }
  void setVal(double val) {
    this._val = val;
  }
  void setTarget(String target) {
    this._target = target;
  }

  public String toString() {
    char comparison = (_comparison == Comparison.LessThan) ? '<' : '>';
    return _var + comparison + val() + " -> " + _target;
  }
  public long val() {
    return Double.valueOf(this._val).longValue();
  }

  private String _var;
  private enum Comparison { LessThan, GreaterThan }
  private Comparison _comparison;
  private double _val; 
  private String _target;
}

