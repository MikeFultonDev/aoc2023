package aoc2023;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class PulseState {
  PulseState(PulseLevel initialLevel) {
    this._pulseLevel = initialLevel;
  }

  public boolean low() {
    return (this._pulseLevel == PulseLevel.Low);
  }
  public boolean high() {
    return (this._pulseLevel == PulseLevel.High);
  }
  public void setLow() {
    this._pulseLevel = PulseLevel.Low;
  }
  public void setHigh() {
    this._pulseLevel = PulseLevel.High;
  }

  public PulseLevel flip() {
    if (this._pulseLevel == PulseLevel.Low) {
      this._pulseLevel = PulseLevel.High;
    } else {
      this._pulseLevel = PulseLevel.Low;
    }
    return this._pulseLevel;
  }

  public static final PulseState LOW = new PulseState(PulseLevel.Low);
  public static final PulseState HIGH = new PulseState(PulseLevel.High);

  private enum PulseLevel { Low, High };
  private PulseLevel _pulseLevel;
}
