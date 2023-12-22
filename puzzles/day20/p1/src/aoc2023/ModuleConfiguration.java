package aoc2023;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

class ModuleConfiguration {
  ModuleConfiguration(List<String> lines) {
    _entriesMap = new HashMap<ModuleConfigurationEntry, Module>();
    _entriesList = new ArrayList<ModuleConfigurationEntry>();
    for (String line : lines) {
      this._entriesList.add(new ModuleConfigurationEntry(line));
    }
    for (ModuleConfigurationEntry entry : this._entriesList) {
      entry.establish(_entriesMap);
    }
  }
  private List<ModuleConfigurationEntry> _entriesList;
  private Map<ModuleConfigurationEntry, Module> _entriesMap;
}
