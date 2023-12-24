package aoc2023;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

class ConjunctionModule extends Module {
  ConjunctionModule(String name, List<String> targetNames) {
    super(name, targetNames);
    _memory = new HashMap<String, PulseState>();
  }
  @Override
  public String prefix() {
    return "&";
  }

  void addInput(String source) {
    _memory.put(source, PulseState.LOW);
  }

  @Override
  public List<Pulse> processPulse(Pulse pulse) {
    List<Pulse> pulses = new ArrayList<Pulse>();
    if (_memory.get(pulse.source()) != null) {
      _memory.put(pulse.source(), pulse.state());
    }
    PulseState out;
    if (inputAllHigh()) {
      out = PulseState.LOW;
    } else {
      out = PulseState.HIGH;
    }
    pulses.add(new Pulse(this.name(), this.targetNames(), out));
    return pulses;
  }

  private boolean inputAllHigh() {
    for (PulseState value : _memory.values()) {
      if (value.low()) {
        return false;
      }
    }
    return true;
  }

  private Map<String, PulseState> _memory;
}
