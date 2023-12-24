package aoc2023;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ModuleState {
  ModuleState(ModuleState source) {
    this._moduleLevel = source._moduleLevel;
  }
  private ModuleState(ModuleLevel source) {
    this._moduleLevel = source;
  }

  public boolean off() {
    return (this._moduleLevel == ModuleLevel.Off);
  }
  public boolean on() {
    return (this._moduleLevel == ModuleLevel.On);
  }
  public void setOff() {
    this._moduleLevel = ModuleLevel.Off;
  }
  public void setOn() {
    this._moduleLevel = ModuleLevel.On;
  }

  public ModuleLevel flip() {
    if (this._moduleLevel == ModuleLevel.Off) {
      this._moduleLevel = ModuleLevel.On;
    } else {
      this._moduleLevel = ModuleLevel.Off;
    }
    return this._moduleLevel;
  }

  public final static ModuleState ON = new ModuleState(ModuleLevel.On);
  public final static ModuleState OFF = new ModuleState(ModuleLevel.Off);
  private enum ModuleLevel { Off, On };
  private ModuleLevel _moduleLevel;
}
