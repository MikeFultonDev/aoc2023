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
    this._crossings = 0;
    for (HailVector hv1: _hailList) {
      for (HailVector hv2: _hailList) {
        if (hv1.parallelTo(hv2)) {
          continue;
        }
        double x = (hv2.C() - hv1.C()) / (hv1.inclination() - hv2.inclination());
        if (x >= startX && x <= endX) {
          double y = x * hv1.inclination() + hv1.C();
          if (y >= startY && y <= endY) {
            System.out.println(hv1 + " and " + hv2 + " cross inside x and y range at: " + Double.toString(x) + "," + Double.toString(y));
            this._crossings++;
          }

        }
      }
    }
    
    return this._crossings;
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
  private int _crossings;
}
