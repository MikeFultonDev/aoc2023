package aoc2023;
import java.io.IOException;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

class ModuleConfigurationEntry {
  ModuleConfigurationEntry(String rawtext, Map<String, Module> entries) throws java.io.IOException {
    String words[] = rawtext.split(" ");
    List<String> targetNames = new ArrayList<String>();
    for (int i=2; i<words.length; ++i) {
      String strTarget = words[i].replace(",","");
      targetNames.add(strTarget);
    }
    this._module = Module.create(words[0], targetNames);
    entries.put(this._module.name(), this._module);
  }

  Module getModule() {
    return _module;
  }

  private Module _module;
}
