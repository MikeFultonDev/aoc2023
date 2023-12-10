package aoc2023;

class Scratchcard {
  Scratchcard(int myIndex, String line) {
    this._myIndex = myIndex;
    this._line = line;
  }
  long calculateCards(long[] multiples) {
    computeCard(multiples);
    return multiples[this._myIndex];
  }

  private void computeCard(long[] multiples) {
    String headerAndData[] = this._line.split(":");
    String winnersAndNumbers[] = headerAndData[1].split("\\|");
    String winners[] = winnersAndNumbers[0].trim().split(" ");
    String numbers[] = winnersAndNumbers[1].trim().split(" ");

    // Inefficient loop, but the list is short
   
    int copies = 0;
    for (String number : numbers) {
      for (String winner : winners) {
        if (number.equals(winner) && !number.trim().equals("") && !winner.trim().equals("")) {
          //System.out.println("Number: " + number + " equals: " + winner);
          ++copies;
          break;
        }
      }
    }

    long myMultiple = multiples[this._myIndex];

    int end = this._myIndex + copies + 1; 
    if (end > multiples.length) {
      end = multiples.length;
    }
    for (int i=this._myIndex+1; i<end; ++i) {
      multiples[i] += myMultiple;
    }

    //System.out.println(this._line + " yields: " + this._copies);
  }
  private int _myIndex;
  private String _line;
}

