package aoc2023;

class WiringNode {
  WiringNode(String name) {
    this._name = name;
    this._touched = false;
  }

  @Override 
  public boolean equals(Object other) {
    if (!(other instanceof WiringNode)) {
      return false;
    }
    WiringNode rhs = (WiringNode) other;
    return this._name.equals(rhs._name);
  }

  @Override
  public String toString() {
    return this._name + ((this._touched) ? " (touched)" : "");
  }

  @Override 
  public int hashCode() {
    return this._name.hashCode();
  }

  void touch() {
    this._touched = true;
  }

  void clear() {
    this._touched = false;
  }

  boolean touched() {
    return this._touched;
  }

  String name() {
    return this._name;
  }

  private String _name;
  private boolean _touched;
}
