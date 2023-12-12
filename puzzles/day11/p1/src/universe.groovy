if (args.length < 1) {
  println("Syntax: universe <text file>")
  System.exit(4)
}
String file = args[0]

def cols=0
def rows=0
def col=0
def row=0

def matrix
def lines = []

new File(file).eachLine { line ->
  lines.add(line)
}
rows = lines.size()

for (row=0; row<rows; ++row) {
    def aLine = lines.get(row).split('')
    if (cols == 0) {
      cols = aLine.length
      matrix = new String[rows][cols]
    }
    col = 0
    for (String entry : aLine) {
      matrix[row][col++] = entry;
    }
}

println "rows: " + row + " cols: " + cols

for (row = 0; row < rows; ++row) {
  for (col = 0; col < cols; ++col) {
    print matrix[row][col]
  }
  println ""
}
