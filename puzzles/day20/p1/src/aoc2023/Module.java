package aoc2023;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

abstract class Module {
  Module(String name, List<String> targetNames) {
    this._name = name;
    this._targetNames = targetNames;
  }
  public static Module create(String decoratedName, List<String> targetNames) throws java.io.IOException {
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
      case FlipFlop: return new FlipFlopModule(name, targetNames);
      case Conjunction: return new ConjunctionModule(name, targetNames);
      case Broadcast: return new BroadcastModule(name, targetNames);
      case Button: return new ButtonModule(name, targetNames);
      default: System.err.println("Should not get here. Type is: " + type); return null;
    }
  } 
  public String name() {
    return _name;
  }

  abstract public String prefix();
  abstract public List<Pulse> processPulse(Pulse source);

  @Override
  public String toString() {
    String out = prefix() + name();
    if (_targetNames != null) {
      out = out + " ->";
      for (String targetName : _targetNames) {
        out = out + " " + targetName;
      }
    }
    return out;
  }

  List<String> targetNames() {
    return _targetNames;
  }

  private enum ModuleType { FlipFlop, Conjunction, Broadcast, Button };
  protected String _name;
  private List<String> _targetNames;
  private Map<String, Module> _entries;

  protected final static List<Pulse> EMPTY_PULSE_LIST = new ArrayList<Pulse>();
}
