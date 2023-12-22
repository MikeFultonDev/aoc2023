package aoc2023;
import java.io.IOException;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

class ModuleConfigurationEntry {
  ModuleConfigurationEntry(String rawtext) {
    String words[] = rawtext.split(" ");
    this._source = words[0];
    this._targetNames = new ArrayList<String>();
    for (int i=2; i<words.length; ++i) {
      String strTarget = words[i].replace(",","");
      this._targetNames.add(strTarget);
    }
  }
  void establish(Map<ModuleConfigurationEntry, Module> entries) {
    this._targetModules = new ArrayList<Module>();
    for (String targetName : this._targetNames) {
      this._targetModules.add(entries.get(targetName));
    }
  }

  String name() {
    return this._source;
  }

  private String _source;
  private List<String> _targetNames;
  private List<Module> _targetModules;
}
