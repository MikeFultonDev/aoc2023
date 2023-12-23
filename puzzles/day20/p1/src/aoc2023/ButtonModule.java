package aoc2023;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ButtonModule extends Module {
  ButtonModule(String name, List<String> targetNames) {
    super(name, targetNames);
  }
  @Override
  public String prefix() {
    return "";
  }
}
