package aoc2023;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class FlipFlopModule extends Module {
  FlipFlopModule(String name, List<String> targetNames) {
    super(name, targetNames);
    _pulseState = Pulse.PulseState.Low;
  }
  @Override
  public String prefix() {
    return "%";
  }

  @Override
  public List<String> processPulse(Pulse source) {
    if (source.low()) {
      flip(_pulseState);
      return new Pulse(this.name(), this.targetNames(), _pulseState);
    } else {
      return EMPTY_TARGET_LIST;
    }
  }

  private Pulse.PulseState _pulseState;
}
