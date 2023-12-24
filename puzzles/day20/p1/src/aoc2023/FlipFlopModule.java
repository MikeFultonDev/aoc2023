package aoc2023;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class FlipFlopModule extends Module {
  FlipFlopModule(String name, List<String> targetNames) {
    super(name, targetNames);
    _moduleState.setOff();
  }
  @Override
  public String prefix() {
    return "%";
  }

  @Override
  public List<Pulse> processPulse(Pulse source) {
    if (source.low()) {
      _moduleState.flip();
      List<Pulse> pulses = new ArrayList<Pulse>();
      PulseState out = (_moduleState.on()) ? PulseState.HIGH : PulseState.LOW;
      pulses.add(new Pulse(this.name(), this.targetNames(), out));
      return pulses;
    } else {
      return EMPTY_PULSE_LIST;
    }
  }

  private ModuleState _moduleState;
}
