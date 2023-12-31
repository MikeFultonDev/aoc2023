package aoc2023;
import java.util.ArrayList;
import java.util.List;

class WiringDiagram {
  WiringDiagram(List<String> wiringLines) {
    private this._wiringConnections = new ArrayList<WiringConnection>();
    for (String wiringLine : wiringLines) {
      _wiringConnections.add(new WiringConnection(wiringLine));
    }
  }

  long groupProduct() {
    return 0L;
  }

  @Override
  public String toString() {
    String out = "";
    for (WiringConnection connection: this._wiringConnections) {
      out = out + connection + System.lineSeparator();
    }
    return out;
  }
  private List<WiringConnection> _wiringConnections;
}
