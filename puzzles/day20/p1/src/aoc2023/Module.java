package aoc2023;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Module {
  Module(String name) {
    this._name = name;
  }
  public static Module create(String decoratedName) throws java.io.IOException {
    String name;
    ModuleType type;
    if (decoratedName.startsWith("%")) {
      name = decoratedName.substring(1);
      type = ModuleType.FlipFlop;
    } else if (decoratedName.startsWith("&")) {
      name = decoratedName.substring(1);
      type = ModuleType.Conjunction;
    } else {
      name = decoratedName;
      if (name.equals("broadcaster")) {
        type = ModuleType.Broadcast;
      } else if (name.equals("button")) {
        type = ModuleType.Button;
      } else {
        throw new IOException("Unknown Module: " + decoratedName);
      }
    }
    switch (type) {
      case FlipFlop: return new FlipFlopModule(name);
      case Conjunction: return new ConjunctionModule(name);
      case Broadcast: return new BroadcastModule(name);
      case Button: return new ButtonModule(name);
      default: return null;
    }
  } 

  private enum ModuleType { FlipFlop, Conjunction, Broadcast, Button };
  private String _name;

  @Override
  public int hashCode() {
    return _name.hashCode();
  }
}
