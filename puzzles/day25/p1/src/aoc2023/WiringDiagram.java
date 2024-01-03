package aoc2023;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

class WiringDiagram {
  WiringDiagram(List<String> wiringLines) throws java.io.IOException {
    this._wiringConnections = new HashMap<WiringNode, WiringConnection>();
    for (String wiringLine : wiringLines) {
      WiringConnection wc = new WiringConnection(wiringLine);
      this._wiringConnections.put(wc.source(), wc);
    }

    // Add in the extra 'backward' connections
    WiringNode sourceCopies[] = this._wiringConnections.keySet().toArray(new WiringNode[0]);
    for (WiringNode source : sourceCopies) {
      //System.out.println("Processing source: " + source);
      WiringNode targetCopies[] = this._wiringConnections.get(source).targets().toArray(new WiringNode[0]);
      for (WiringNode target : targetCopies) {
        //System.out.println("Processing target: " + target);
        WiringConnection twc = this._wiringConnections.get(target);
        if (twc == null) {
          this._wiringConnections.put(target, new WiringConnection(target));
          twc = this._wiringConnections.get(target);
          //System.out.println("Create new node: " + target);
        } 
        if (!twc.targets().contains(source)) {
          //System.out.println("Add: " + source + " to existing node: " + twc.source());
          twc.targets().add(source);
        }
      }
    }
  }

  List<WiringNode> nodesOfDegreeN(int n) {
    WiringNode mostConnected = null;
    List<WiringNode> degreeN = new ArrayList<WiringNode>();

    for (WiringConnection connection: this._wiringConnections.values()) {
      int sourceDegree = connection.targets().size();
      if (sourceDegree == n) {
        degreeN.add(connection.source());
      }
    }
    return degreeN;
  }

  List<WiringNode> mostConnected() {
    // Return list of sources with highest degree of connectivity
    int maxDegree = 0;

    List<WiringNode> mostConnected = null;
    for (WiringConnection connection: this._wiringConnections.values()) {
      WiringNode source = connection.source();
      int sourceDegree = connection.targets().size();
      if (sourceDegree > maxDegree) {
        System.out.println("Max Degree: " + sourceDegree);
        maxDegree = sourceDegree;
      }
    }
    return nodesOfDegreeN(maxDegree);
  }

  void removeConnection(ConnectionPair pair) {
//    System.out.println("remove: " + pair);
    List<WiringNode> nodeATargets = this._wiringConnections.get(pair._nodeA).targets();
    List<WiringNode> nodeBTargets = this._wiringConnections.get(pair._nodeB).targets();
    if (!nodeATargets.remove(pair._nodeB)) {
      throw new RuntimeException("Unable to find: " + pair._nodeB + " in: " + nodeATargets);
    }
    if (!nodeBTargets.remove(pair._nodeA)) {
      throw new RuntimeException("Unable to find: " + pair._nodeB + " in: " + nodeATargets);
    }
  }

  void addConnection(ConnectionPair pair) {
//    System.out.println("add: " + pair);
    this._wiringConnections.get(pair._nodeA).targets().add(pair._nodeB);
    this._wiringConnections.get(pair._nodeB).targets().add(pair._nodeA);
  }

  ArrayList<WiringNode> findPath(WiringConnection sourceConnection, WiringNode end, ArrayList<WiringNode> currentPath) {
    WiringNode source = sourceConnection.source();
    if (source.touched()) {
      return null;
    }
    if (source.equals(end)) {
      //System.out.println("Found Path: with " + currentPath.size() + " nodes");
      ArrayList<WiringNode> clonedCurrentPath = new ArrayList<WiringNode>(currentPath);
      return clonedCurrentPath;
    }
    source.touch();

    ArrayList<WiringNode> fullPath = null;
    for (WiringNode next : sourceConnection.targets()) {
      if (!next.touched()) {
        WiringConnection nextConnection = this._wiringConnections.get(next);
        ArrayList<WiringNode> nextPath;
        if (currentPath != null) {
          nextPath = new ArrayList<WiringNode>(currentPath);
        } else {
          nextPath = new ArrayList<WiringNode>();
        }
        nextPath.add(next);
        //System.out.println(currentPath);
        if ((fullPath = findPath(nextConnection, end, nextPath)) != null) {
          break;
        }
      }
    }

    source.clear();
    return fullPath;
  }

  List<WiringNode> findPaths(ConnectionPair connection) {
    // Walk from connection nodeA to connection nodeB and 
    // add each successful path to a list of lists
    WiringNode start = connection._nodeA;
    WiringNode end = connection._nodeB;
    List<ArrayList<WiringNode>> paths = new ArrayList<ArrayList<WiringNode>>();

    ArrayList<WiringNode> currentPath = null;
    return findPath(this._wiringConnections.get(start), end, currentPath);
  }

  long groupProduct() {
    List<ConnectionPair> pairs = new ArrayList<ConnectionPair>();
    for (WiringConnection connection: this._wiringConnections.values()) {
      WiringNode source = connection.source();
      for (WiringNode target : connection.targets()) {
        ConnectionPair pair = new ConnectionPair(source, target);
        pairs.add(pair);
      }
    }

    for (ConnectionPair first : pairs) {
      removeConnection(first);
      for (ConnectionPair second : pairs) {
        if (first.equals(second)) continue;
        removeConnection(second);
        for (ConnectionPair third : pairs) {
          if (first.equals(third)) continue;
          if (second.equals(third)) continue;
          removeConnection(third);

          List<WiringNode> path = findPaths(first);
          if (path == null) {
            System.out.println("NO Path exists for: " + first + " after removing: " + second + " and " + third);
            return 1L;
          } else {
            ;
          }
          addConnection(third);
        }
        addConnection(second);
      }
      addConnection(first);
    }
        
    return 0L;
  }

  @Override
  public String toString() {
    String out = "";
    for (WiringConnection connection: this._wiringConnections.values()) {
      out = out + connection + System.lineSeparator();
    }
    return out;
  }
  private Map<WiringNode,WiringConnection> _wiringConnections;
}
