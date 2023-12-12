if (args.length < 1) {
  println("Syntax: universe <text file>")
  System.exit(4)
}
String file = args[0]

class C {
  static final BLANK='.'
  static final GALAXY='#'
  static final EXPAND='*'
  static final EXPAND_ADJUSTMENT = 999999L
}

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

def copyrow(inMatrix, outMatrix, inRow, outRow, cols, altchar) {
  def col
  for (col = 0; col < cols; ++col) {
    if (altchar == "") {
      outMatrix[col][outRow] = inMatrix[col][inRow]
    } else {
      outMatrix[col][outRow] = altchar
    }
  }
}
  
def copycol(inMatrix, outMatrix, inCol, outCol, rows, altchar) {
  def row
  for (row = 0; row < rows; ++row) {
    if (altchar == "") {
      outMatrix[outCol][row] = inMatrix[inCol][row]
    } else {
      outMatrix[outCol][row] = altchar
    }
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

def prettyPath(colStart, rowStart, colEnd, rowEnd) {
  return "[" + colStart + "," + rowStart + "] -> [" + colEnd + "," + rowEnd + "]"
}

def getNormalizedPath(colA, rowA, colB, rowB) {
  return prettyPath(colA, rowA, colB, rowB)
}
   
def basicPathLength(matrix, colStart, rowStart, colEnd, rowEnd) {
  def pathLengthRow = rowEnd - rowStart
  def pathLengthCol = colEnd - colStart
  if (pathLengthRow < 0) {
    pathLengthRow = -1 * pathLengthRow
  }
  if (pathLengthCol < 0) {
    pathLengthCol = -1 * pathLengthCol
  }
  return pathLengthRow + pathLengthCol
}

def pathAdjustColThenRow(matrix, colA, rowA, colB, rowB) {
  def tmp
  if (colA > colB) {
    tmp = colA
    colA = colB
    colB = tmp
  }
  if (rowA > rowB) {
    tmp = rowA
    rowA = rowB
    rowB = tmp
  }

  def col
  def row
  def expansion = 0
  for (col = colA; col < colB; ++col) {
    if (matrix[col][rowA] == C.EXPAND) {
      expansion += C.EXPAND_ADJUSTMENT
    }
  }
  for (row = rowA+1; row < rowB; ++row) {
    if (matrix[colB][row] == C.EXPAND) {
      expansion += C.EXPAND_ADJUSTMENT
    }
  }
  return expansion
}

def pathAdjustRowThenCol(matrix, colA, rowA, colB, rowB) {
  def tmp
  if (colA > colB) {
    tmp = colA
    colA = colB
    colB = tmp
  }
  if (rowA > rowB) {
    tmp = rowA
    rowA = rowB
    rowB = tmp
  }

  def col
  def row
  def expansion = 0
  for (row = rowA; row < rowB; ++row) {
    if (matrix[colA][row] == C.EXPAND) {
      expansion += C.EXPAND_ADJUSTMENT
    }
  }
  for (col = colA; col < colB; ++col) {
    if (matrix[col][rowB] == C.EXPAND) {
      expansion += C.EXPAND_ADJUSTMENT
    }
  }
  return expansion
}

def computeLengthColRow(matrix, col, row, cols, rows, pathLengths) {
  def innerRowFirst=row+1
  def innerColStart=col
  if (innerRowFirst == rows) {
    innerColStart = col+1 
    innerRowFirst = 0
  }
  def innerCol
  def innerRow
  def innerRowStart = innerRowFirst
  //println "cols from " + innerColStart + " to " + cols + " and rows " + innerRowStart + " to " + rows
  for (innerCol = innerColStart; innerCol < cols; ++innerCol) {
    for (innerRow = innerRowStart; innerRow < rows; ++innerRow) {
      //println "check [" + innerCol + "," + innerRow + "] : " + matrix[innerCol][innerRow];
      if (matrix[innerCol][innerRow] == C.GALAXY) {
        def pathLength = basicPathLength(matrix, col, row, innerCol, innerRow)
        def pathAExpand = pathAdjustColThenRow(matrix, col, row, innerCol, innerRow)
        def pathBExpand = pathAdjustColThenRow(matrix, col, row, innerCol, innerRow)

        String path = getNormalizedPath(col, row, innerCol, innerRow)
        if (pathAExpand < pathBExpand) {
          pathLength += pathAExpand
        } else {
          pathLength += pathBExpand
        }
        pathLengths.put(path, pathLength)
        //println path + " is: " + pathLength
      }
    }
    innerRowStart = 0
  }
  return pathLengths
}

def sum(entries) {
  def i
  def sum = 0
  entries.values().each { value ->
    sum += value
  }
  return sum
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
    if (matrix[col][row] != C.BLANK) {
      allblank = false
      break
    }
  }
  if (allblank) {
    copyrow(matrix, expandedRowMatrix, row, expandedRow++, cols, C.EXPAND)
  } else {
    copyrow(matrix, expandedRowMatrix, row, expandedRow++, cols, "")
  }
}
def expandedRows = expandedRow
//printMatrix(expandedRowMatrix, expandedRows, cols)

def expandedCol = 0;
for (col = 0; col < cols; ++col) {
  def allblank = true
  for (row = 0; row < expandedRows; ++row) {
    if (expandedRowMatrix[col][row] != C.BLANK && expandedRowMatrix[col][row] != C.EXPAND) {
      allblank = false
      break
    }
  }
  if (allblank) {
    copycol(expandedRowMatrix, expandedColMatrix, col, expandedCol++, expandedRows, C.EXPAND);
  } else {
    copycol(expandedRowMatrix, expandedColMatrix, col, expandedCol++, expandedRows, "");
  }
}
def expandedCols = expandedCol


matrix = expandedColMatrix
rows = expandedRows
cols = expandedCols

printMatrix(matrix, rows, cols)
numPairs = determinePairs(matrix, rows, cols, C.GALAXY)

println "Process " + numPairs + " pairs"

//
// Walk through the matrix. The outer loop will find the next
// galaxy, and the inner loop will then find all the galaxies
// it can pair with, summing up the shortest paths
//

Map pathLengths=[:]
for (col = 0; col < cols; ++col) {
  for (row = 0; row < rows; ++row) {
    if (matrix[col][row] == C.GALAXY) {
      // Start inner loop from the next location on
      computeLengthColRow(matrix, col, row, cols, rows, pathLengths)
    }
  }
}
println "Path Length Total: " + sum(pathLengths)
