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

  long groupProduct() {

    // Add pairs to the list that differ in degree by 0 or 1
    for (WiringConnection connection: this._wiringConnections.values()) {
      String source = connection.source();
      for (String target : connection.targets()) {
        int sourceDegree = connection.targets().size();
        int targetDegree = this._wiringConnections.get(target).targets().size();
        int diff = targetDegree - sourceDegree;
        if (diff < 0) {
          diff = -diff;
        }
        if (diff <= 1) {
          System.out.println("Pair to consider: [ " + source + "," + target + "]");
        }
      }
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
  private Map<String,WiringConnection> _wiringConnections;
}
