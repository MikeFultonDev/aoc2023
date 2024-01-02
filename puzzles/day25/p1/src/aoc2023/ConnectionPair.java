package aoc2023;

class ConnectionPair {
  ConnectionPair(WiringNode nodeA, WiringNode nodeB) {
    this._nodeA = nodeA;
    this._nodeB = nodeB;
  }

  WiringNode _nodeA;
  WiringNode _nodeB;
}
