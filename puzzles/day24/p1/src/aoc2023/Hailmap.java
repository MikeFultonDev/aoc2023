package aoc2023;
import java.util.ArrayList;
import java.util.List;

class Hailmap {
  Hailmap(List<String> hailLines) throws java.io.IOException {
    this._hailList = new ArrayList<HailVector>();
    for (String hailLine : hailLines) {
      HailVector hailVector = new HailVector(hailLine);
      this._hailList.add(hailVector);
    }
  }
  int crossings(double startX, double startY, double endX, double endY) {
    for (HailVector hv1: _hailList) {
      for (HailVector hv2: _hailList) {
        if (hv1.parallelTo(hv2)) {
          continue;
        }
        double x = (hv2.C() - hv1.C()) / (hv1.inclination() - hv2.inclination());
        if (x >= startX && x <= endX) {
          System.out.println(hv1 + " and " + hv2 + " cross inside x range at: " + Double.toString(x));
        }
      }
    }
    
    return 0;
  }

  @Override
  public String toString() {
    String out = "";
    for (HailVector hailVector: _hailList) {
      out = out + hailVector + System.lineSeparator();
    }
    return out;
  }
  private List<HailVector> _hailList;
}
