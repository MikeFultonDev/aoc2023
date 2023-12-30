package aoc2023;
import java.util.ArrayList;
import java.util.List;

class Hailmap {
  Hailmap(List<String> hailLines) {
    this._hailList = new ArrayList<HailVector>();
    for (String hailLine : hailLines) {
      HailVector hailVector = new HailVector(hailLine);
      this._hailList.add(hailVector);
    }
  }
  private List<HailVector> _hailList;
}
