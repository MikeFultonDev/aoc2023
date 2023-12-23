package aoc2023;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Pulse {
  Pulse(String sourceName, List<String> targetNames, PulseState pulseState) {
    this._sourceName = sourceName;
    this._targetNames = targetNames;
    this._pulseState = pulseState;
  }

  public boolean low() {
    return (_pulseState == PulseState.Low);
  }
  public boolean high() {
    return (_pulseState == PulseState.High);
  }
  private String _sourceName;
  private List<String> _targetNames;
  private PulseState _pulseState;

  public enum PulseState { Low, High };
}
