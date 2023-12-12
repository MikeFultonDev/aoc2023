if (args.length < 1) {
  println("Syntax: universe <text file>")
  System.exit(4)
}
String file = args[0]

def BLANK='.'
def GALAXY='#'
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
      print matrix[col][row]
    }
    println ""
  }
}

def copyrow(inMatrix, outMatrix, inRow, outRow, cols) {
  def col
  for (col = 0; col < cols; ++col) {
    outMatrix[col][outRow] = inMatrix[col][inRow]
  }
}
  
def copycol(inMatrix, outMatrix, inCol, outCol, rows) {
  def row
  for (row = 0; row < rows; ++row) {
    outMatrix[outCol][row] = inMatrix[inCol][row]
  }
}

def determinePairs(matrix, rows, cols, symbol) {
  def count = 0;
  for (row=0; row<rows; ++row) {
    for (col=0; col<cols; ++col) {
      if (matrix[col][row] == symbol) {
        ++count;
      }
    }
  }
  return count * (count-1) / 2;
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
      matrix = new String[cols][rows]
      expandedRowMatrix = new String[cols*2][rows*2]
      expandedColMatrix = new String[cols*2][rows*2]
    }
    col = 0
    for (String entry : aLine) {
      matrix[col++][row] = entry;
    }
}

//printMatrix(matrix, rows, cols)

// 
// Walk through the array - first by rows and then
// by columns, to see how much the universe needs to expand
//
def expandedRow = 0;
for (row = 0; row < rows; ++row) {
  def allblank = true
  for (col = 0; col < cols; ++col) {
    if (matrix[col][row] != BLANK) {
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
//printMatrix(expandedRowMatrix, expandedRows, cols)

def expandedCol = 0;
for (col = 0; col < cols; ++col) {
  def allblank = true
  for (row = 0; row < expandedRows; ++row) {
    if (expandedRowMatrix[col][row] != BLANK) {
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


matrix = expandedColMatrix
rows = expandedRows
cols = expandedCols

printMatrix(matrix, rows, cols)
numPairs = determinePairs(matrix, rows, cols, GALAXY)

println "Process " + numPairs + " pairs"

//
// Walk through the matrix. The outer loop will find the next
// galaxy, and the inner loop will then find all the galaxies
// it can pair with, summing up the shortest paths
//

def pathLengthTotal = 0
def innerRowStart
for (col = 0; col < cols; ++col) {
  for (row = 0; row < rows; ++row) {
    if (matrix[col][row] == GALAXY) {
      // Start inner loop from the next location on
      def innerRowFirst=row+1
      def innerColStart=col
      if (innerRowFirst == rows) {
        innerColStart = col+1 
        innerRowFirst = 0
      }
      def innerCol
      def innerRow
      innerRowStart = innerRowFirst
      //println "cols from " + innerColStart + " to " + cols + " and rows " + innerRowStart + " to " + rows
      for (innerCol = innerColStart; innerCol < cols; ++innerCol) {
        for (innerRow = innerRowStart; innerRow < rows; ++innerRow) {
          //println "check [" + innerCol + "," + innerRow + "] : " + matrix[innerCol][innerRow];
          if (matrix[innerCol][innerRow] == GALAXY) {
            def pathLengthRow = innerRow - row
            def pathLengthCol = innerCol - col
            if (pathLengthRow < 0) {
              pathLengthRow = -1 * pathLengthRow
            }
            if (pathLengthCol < 0) {
              pathLengthCol = -1 * pathLengthCol
            } 
            def pathLength = pathLengthRow + pathLengthCol
            //println "Path Length from [" + col + "," + row + "] to [" + innerCol + "," + innerRow + "] is: " + pathLength
            pathLengthTotal += pathLength
          }
        }
        innerRowStart = 0
      }
    }
  }
}
println "Path Length Total: " + pathLengthTotal
