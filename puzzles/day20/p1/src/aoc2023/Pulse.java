package aoc2023;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Pulse {
  Pulse(String source, List<String> targets, PulseState state) {
    this._source = source;
    this._targets = targets;
    this._state = state;
  }

  boolean low() {
    return _state.low();
  }
  boolean high() {
    return _state.high();
  }
  String source() {
    return _source;
  }
  List<String> targets() {
    return _targets;
  }
  PulseState state() {
    return _state;
  }
  private String _source;
  private List<String> _targets;
  private PulseState _state;
}
