package aoc2023;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

class ModuleConfiguration {
  ModuleConfiguration(List<String> lines) throws java.io.IOException {
    _entriesMap = new HashMap<String, Module>();
    _entriesList = new ArrayList<Module>();
    List<String> buttonTarget = new ArrayList<String>();
    buttonTarget.add("broadcaster");
    Module buttonModule = Module.create("button", buttonTarget);
    this._entriesList.add(buttonModule);
    this._entriesMap.put("button", buttonModule);
    for (String line : lines) {
      ModuleConfigurationEntry entry = new ModuleConfigurationEntry(line, this._entriesMap);
      this._entriesList.add(entry.getModule());
    }

    //
    // Establish the input lists for the conjunction modules
    //
    for (String source : _entriesMap.keySet()) {
      Module sourceModule = _entriesMap.get(source);
      for (String target : sourceModule.targetNames()) {
        Module targetModule = _entriesMap.get(target);
        if (targetModule instanceof ConjunctionModule) {
          ConjunctionModule cmod = (ConjunctionModule) targetModule;
          cmod.addInput(source);
        }
      }
    }
  }

  @Override
  public String toString() {
    String out = "";
    for (Module module : _entriesList) {
      out = out + "\n" + module;
    }
    return out;
  }

  private void processPulses(List<Pulse> pulses) {
    if (pulses.isEmpty()) {
      return;
    }
   
    List<Pulse> cumulativePulses = new ArrayList<Pulse>();
    for (Pulse pulse : pulses) {
      if (pulse.low()) {
        this._lowPulses += pulse.targets().size();
      } else {
        this._highPulses += pulse.targets().size();
      }
      for (String target : pulse.targets()) {
        Module targetModule = _entriesMap.get(target);
        if (targetModule != null) {
          List<Pulse> targetPulses = _entriesMap.get(target).processPulse(pulse);
          cumulativePulses.addAll(targetPulses);
        }
      }
    }
    processPulses(cumulativePulses);
  }

  Long run(int iterations) {
    this._lowPulses = 0L;
    this._highPulses = 0L;
    for (int i=0; i<iterations; ++i) {
      Module buttonModule = _entriesMap.get("button");
      List<Pulse> pulses = buttonModule.processPulse(null);
      processPulses(pulses);
    }

    System.out.println("Processed " + this._lowPulses + " low pulses, and " + this._highPulses + " high pulses");
    return this._lowPulses * this._highPulses;
  }

  private List<Module> _entriesList;
  private Map<String, Module> _entriesMap;
  private Long _lowPulses;
  private Long _highPulses;
}
