package aoc2023;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ButtonModule extends Module {
  ButtonModule(String name, List<String> targetNames) {
    super(name, targetNames);
  }

  @Override
  public List<Pulse> processPulse(Pulse pulse) {
    List<Pulse> pulses = new ArrayList<Pulse>();
    pulses.add(new Pulse(this.name(), this.targetNames(), PulseState.LOW));
    return pulses;
  }

  @Override
  public String prefix() {
    return "";
  }
}
