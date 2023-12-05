package aoc2023;

class Scratchcard {
  Scratchcard(String line) {
    this._line = line;
  }
  long getPoints() {
    computeCard();
    return this._points;
  }

  private void computeCard() {
    
    String headerAndData[] = this._line.split(":");
    String winnersAndNumbers[] = headerAndData[1].split("\\|");
    String winners[] = winnersAndNumbers[0].trim().split(" ");
    String numbers[] = winnersAndNumbers[1].trim().split(" ");

    // Inefficient loop, but the list is short
    
    for (String number : numbers) {
      for (String winner : winners) {
        if (number.equals(winner)) {
          if (_points == 0) {
            _points = 1;
          } else {
            _points <<= 1;
          }
          break;
        }
      }
    }
  }
  private String _line;
  private long _points;
}

