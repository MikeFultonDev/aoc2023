package aoc2023;

class ConnectionPair {
  ConnectionPair(WiringNode nodeA, WiringNode nodeB) {
    this._nodeA = nodeA;
    this._nodeB = nodeB;
  }

  WiringNode _nodeA;
  WiringNode _nodeB;

  @Override
  public String toString() {
    return "[" + this._nodeA + "," + this._nodeB + "]";
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof ConnectionPair) {
      ConnectionPair rhs = (ConnectionPair) other;
      if (this._nodeA.equals(rhs._nodeA) && this._nodeB.equals(rhs._nodeB)) {
        return true;
      }
      if (this._nodeA.equals(rhs._nodeB) && this._nodeB.equals(rhs._nodeA)) {
        return true;
      }
    }
    return false;
  }
}
