if (args.length < 1) {
  println("Syntax: universe <text file>")
  System.exit(4)
}
String file = args[0]

def BLANK='.'
def cols=0
def rows=0
def col=0
def row=0

def matrix
def lines = []

def printMatrix(matrix, rows, cols) {
  println "rows: " + rows + " cols: " + cols

  for (row = 0; row < rows; ++row) {
    for (col = 0; col < cols; ++col) {
      print matrix[row][col]
    }
    println ""
  }
}

def copyrow(inMatrix, outMatrix, inRow, outRow, cols) {
  def col
  for (col = 0; col < cols; ++col) {
    outMatrix[outRow][col] = inMatrix[inRow][col]
  }
}
  
def copycol(inMatrix, outMatrix, inCol, outCol, rows) {
  def row
  for (row = 0; row < rows; ++row) {
    outMatrix[row][outCol] = inMatrix[row][inCol]
  }
}

new File(file).eachLine { line ->
  lines.add(line)
}
rows = lines.size()

//
// Set up the matrix and an expanded matrix twice as big
// to account for maximum array expansion
//
for (row=0; row<rows; ++row) {
    def aLine = lines.get(row).split('')
    if (cols == 0) {
      cols = aLine.length
      matrix = new String[rows][cols]
      expandedRowMatrix = new String[rows*2][cols*2]
      expandedColMatrix = new String[rows*2][cols*2]
    }
    col = 0
    for (String entry : aLine) {
      matrix[row][col++] = entry;
    }
}

printMatrix(matrix, rows, cols)

// 
// Walk through the array - first by rows and then
// by columns, to see how much the universe needs to expand
//
def expandedRow = 0;
for (row = 0; row < rows; ++row) {
  def allblank = true
  for (col = 0; col < cols; ++col) {
    if (matrix[row][col] != BLANK) {
      allblank = false
      break
    }
  }
  copyrow(matrix, expandedRowMatrix, row, expandedRow++, cols)
  if (allblank) {
    copyrow(matrix, expandedRowMatrix, row, expandedRow++, cols)
  }
}
def expandedRows = expandedRow
printMatrix(expandedRowMatrix, expandedRows, cols)

def expandedCol = 0;
for (col = 0; col < cols; ++col) {
  def allblank = true
  for (row = 0; row < expandedRows; ++row) {
    if (expandedRowMatrix[row][col] != BLANK) {
      allblank = false
      break
    }
  }
  copycol(expandedRowMatrix, expandedColMatrix, col, expandedCol++, expandedRows);
  if (allblank) {
    copycol(expandedRowMatrix, expandedColMatrix, col, expandedCol++, expandedRows);
  }
}
def expandedCols = expandedCol

printMatrix(expandedColMatrix, expandedRows, expandedCols)
