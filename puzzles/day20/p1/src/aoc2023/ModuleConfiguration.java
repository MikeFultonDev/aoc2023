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
    this._entriesList.add(Module.create("button", buttonTarget));
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

  void run() {
  }
  private List<Module> _entriesList;
  private Map<String, Module> _entriesMap;
}
