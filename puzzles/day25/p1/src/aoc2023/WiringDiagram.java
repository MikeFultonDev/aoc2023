package aoc2023;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

class WiringDiagram {
  WiringDiagram(List<String> wiringLines) throws java.io.IOException {
    this._wiringConnections = new HashMap<String, WiringConnection>();
    for (String wiringLine : wiringLines) {
      WiringConnection wc = new WiringConnection(wiringLine);
      this._wiringConnections.put(wc.source(), wc);
    }

    // Add in the extra 'backward' connections
    String sourceCopies[] = this._wiringConnections.keySet().toArray(new String[0]);
    for (String source : sourceCopies) {
      //System.out.println("Processing source: " + source);
      String targetCopies[] = this._wiringConnections.get(source).targets().toArray(new String[0]);
      for (String target : targetCopies) {
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
    String mostConnected = null;
    for (WiringConnection connection: this._wiringConnections.values()) {
      String source = connection.source();
      int sourceDegree = connection.targets().size();
      if (sourceDegree == 9) {
        //System.out.println("Connection to consider: " + connection);
        for (String target : connection.targets()) {
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

  List<List<String>> findPaths(WiringConnection source, String end, List<List<String>> paths) {
    return null;
  }

  List<List<String>> findPaths(ConnectionPair connection) {
    // Walk from connection nodeA to connection nodeB and 
    // add each successful path to a list of lists
    String start = connection._nodeA;
    String end = connection._nodeB;
    List<List<String>> paths = new List<List<String>>();

    return findPaths(this._wiringConnections.get(start), end, paths);
  }

  long groupProduct() {

    ConnectionPair mostConnected = mostConnected();
    removeConnection(mostConnected);

    List<List<String>> paths = findPaths(mostConnected);
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
  private Map<String,WiringConnection> _wiringConnections;
}
