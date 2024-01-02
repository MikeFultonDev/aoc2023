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

  ConnectionPair mostConnected() {
    // Add first pair to the list with highest degree of connectivity (hardcoded for 9x9)
    // Could make general by finding highest degree of connectivity and working down until a connection
    WiringNode mostConnected = null;
    for (WiringConnection connection: this._wiringConnections.values()) {
      WiringNode source = connection.source();
      int sourceDegree = connection.targets().size();
      if (sourceDegree == 9) {
        //System.out.println("Connection to consider: " + connection);
        for (WiringNode target : connection.targets()) {
          int targetDegree = this._wiringConnections.get(target).targets().size();
          if (targetDegree == sourceDegree) {
            System.out.println("Consider pair with degrees " + sourceDegree + " and " + targetDegree + " [" + source + "," + target + "]");
            return new ConnectionPair(source, target);
          }
        }
      }
    }
    return null;
  }

  void removeConnection(ConnectionPair pair) {
    this._wiringConnections.get(pair._nodeA).targets().remove(pair._nodeB);
    this._wiringConnections.get(pair._nodeB).targets().remove(pair._nodeA);
  }

  void findPath(WiringConnection sourceConnection, WiringNode end, List<ArrayList<WiringNode>> paths, List<WiringNode> currentPath) {
    WiringNode source = sourceConnection.source();
    if (source.equals(end)) {
      currentPath.add(source);
      return;
    }
    if (source.touched()) {
      return;
    }
    source.touch();

    for (WiringNode next : sourceConnection.targets()) {
      if (!next.touched()) {
        WiringConnection nextConnection = this._wiringConnections.get(next);
        findPath(nextConnection, end, paths, currentPath);
      }
    }
    source.clear();
  }

  List<ArrayList<WiringNode>> findPaths(ConnectionPair connection) {
    // Walk from connection nodeA to connection nodeB and 
    // add each successful path to a list of lists
    WiringNode start = connection._nodeA;
    WiringNode end = connection._nodeB;
    List<ArrayList<WiringNode>> paths = new ArrayList<ArrayList<WiringNode>>();

    List<WiringNode> currentPath = new ArrayList<WiringNode>();
    findPath(this._wiringConnections.get(start), end, paths, currentPath);
    return paths;
  }

  long groupProduct() {

    ConnectionPair mostConnected = mostConnected();
    removeConnection(mostConnected);

    List<ArrayList<WiringNode>> paths = findPaths(mostConnected);
    System.out.println("There are " + paths.size() + " paths connecting " + mostConnected);
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
