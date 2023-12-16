"use strict";

const path = require('node:path'); 
const os = require('os');
const fs = require('node:fs');
 
function readFile(file) {
  try {
    const data = fs.readFileSync(mirrorFile, 'utf8');
    return data;
  } catch (err) {
    console.error(err);
    return null;
  }
}

function buildArray(lines) {
  var arr = [];
  for (var row=0; row<lines.length; ++row) {
    arr[row] = [];
    for (var col=0; col<lines[row].length; ++col) {
      arr[row][col] = lines[row].substring(col,col+1);
    }
  }
  return arr;
}

function buildArrayVertical(lines, start, end) {
  var arr = [];
  for (var row=0; row<lines.length; ++row) {
    arr[row] = [];
    for (var col=start; col<=end; ++col) {
      arr[row][col-start] = lines[row].substring(col,col+1);
    }
  }
  return arr;
}

function flipArrayVertical(arr) {
  for (var row=0; row<arr.length; ++row) {
    var ecol = (arr[row].length)-1;
    for (var col=0; col<arr[row].length/2; ++col) {
      var tmp = arr[row][col];
      arr[row][col] = arr[row][ecol-col];
      arr[row][ecol-col] = tmp;
    }
  }
  return arr;
}

function buildArrayHorizontal(lines, start, end) {
  var arr = [];
  for (var row=start; row<=end; ++row) {
    arr[row-start] = [];
    for (var col=0; col < lines[row].length; ++col) {
      arr[row-start][col] = lines[row].substring(col,col+1);
    }
  }
  return arr;
}

function flipArrayHorizontal(arr) {
  for (var row=0; row<arr.length/2; ++row) {
    var erow = (arr.length)-1;
    for (var col=0; col<arr[row].length; ++col) {
      var tmp = arr[row][col];
      arr[row][col] = arr[erow-row][col];
      arr[erow-row][col] = tmp;
    }
  }
  return arr;
}

function equalArrays(left, right) {
  for (var row=0; row<left.length; ++row) {
    for (var col=0; col<left[row].length; ++col) {
      if (left[row][col] !== right[row][col]) {
        return false;
      }
    }
  }
  return true;
}

function printArray(arr) {
  for (var row=0; row<arr.length; ++row) {
    var line = '';
    for (var col=0; col<arr[row].length; ++col) {
      var c = arr[row][col];
      line = line + c;
    }
    console.log(line);
  }
}  

function verticalReflection(lines) {
  var vr = 0;
  const lineLength = lines[0].length
  //console.log('Vertical Columns:', lineLength);
  for (var pivot=1; pivot<lineLength; ++pivot) {
    var leftLength = pivot-0;
    var rightLength = lineLength-pivot;
    var maxLength = (leftLength > rightLength) ? leftLength : rightLength;
    var sl = pivot - maxLength;
    var el = pivot - 1;
    var sr = pivot;
    var er = pivot + maxLength - 1;
    if (sl < 0) {
      er = er + sl;
      sl = 0;
    }
    if (er > lineLength - 1) {
      sl = sl + (er - lineLength + 1);
      er = lineLength - 1;
    }
    var left = buildArrayVertical(lines, sl, el);
    var right = buildArrayVertical(lines, sr, er);
    right = flipArrayVertical(right);

    var refText;
    if (equalArrays(left, right)) {
      console.log('Equal arrays at pivot: ' + pivot);
      vr = pivot;
      refText = 'Y';
    } else {
      refText = 'N';
    }
    var leftText = sl + '->' + el;
    var rightText = sr + '->' + er;
      
    console.log(leftText, ' and ', rightText, ':', refText);
  }
 console.log('Return vr: ' + vr);
  return vr;
}

function horizontalReflection(lines) {
  var hr = 0;
  //console.log('Horizontal Rows:', lines.length);
  var rowsLength = lines.length;
  for (var pivot=1; pivot<rowsLength; ++pivot) {
    var leftLength = pivot-0;
    var rightLength = rowsLength-pivot;
    var maxLength = (leftLength > rightLength) ? leftLength : rightLength;
    var sl = pivot - maxLength;
    var el = pivot - 1;
    var sr = pivot;
    var er = pivot + maxLength - 1;
    if (sl < 0) {
      er = er + sl;
      sl = 0;
    }
    if (er > rowsLength - 1) {
      sl = sl + (er - rowsLength + 1);
      er = rowsLength - 1;
    }  
    var tp = buildArrayHorizontal(lines, sl, el);
    var bottom = buildArrayHorizontal(lines, sr, er);
    bottom = flipArrayHorizontal(bottom);

    var refText;
    if (equalArrays(tp, bottom)) {
      hr = pivot;
      refText = 'Y';
    } else {
      refText = 'N';
    }

    var topText = sl + '^' + el;
    var bottomText = sr + 'v' + er;
      
    //console.log(topText, ' and ', bottomText, ':', refText);
  }
  return hr;
}

function getPossibleSmudgePoints(lines, direction) {
  var arr = buildArray(lines);

  var smudges = []; 
  var smudgeNum = 0;
  if (direction !== 'H') {
    var vertNum = [];
    for (row=0; row<arr.length; ++row) {
      vertNum[row] = 0;
      for (col=0; col<arr[0].length; ++col) {
        if (arr[row][col] == '#') {
          vertNum[row] |= (1 << col);
          //console.log('vertNum[' + row + '] ' + vertNum[row]);
        }
      }
    }
    for (var outer=0; outer<arr.length-1; ++outer) {
      for (var inner=outer+1; inner<arr.length; ++inner) {
        var diffBits = (vertNum[outer] ^ vertNum[inner]);
        var close = (diffBits & (diffBits - 1));
        //console.log(inner + ' and ' + outer + ' rows Diffbits: ' + diffBits + ' close: ' + close);
        if ((diffBits !== 0) && (close === 0)) {
          var colVal = diffBits;
          var colNum = -1;
          while (colVal > 0) {
            colVal >>= 1;
            colNum++;
          }

          console.log(outer + ' and ' + inner + ' rows could be smudged. Only column: ' + colNum + ' differs.');
          
          smudges[smudgeNum++] = [ colNum, outer ];
          smudges[smudgeNum++] = [ colNum, inner ];
        }
      }
    }
  }

  if (direction !== 'V') {
    var horNum = [];
    for (var col=0; col<arr[0].length; ++col) {
      horNum[col] = 0;
      for (var row=0; row<arr.length; ++row) {
        if (arr[row][col] == '#') {
          horNum[col] |= (1 << row);
          //console.log('horNum[' + col + '] ' + vertNum[col]);
        }
      }
    }
    for (var outer=0; outer<arr.length-1; ++outer) {
      for (var inner=outer+1; inner<arr.length; ++inner) {
        var diffBits = (horNum[outer] ^ horNum[inner]);
        var close = (diffBits & (diffBits - 1));
        //console.log(inner + ' and ' + outer + ' rows Diffbits: ' + diffBits + ' close: ' + close);
        if ((diffBits !== 0) && (close === 0)) {
          var rowVal = diffBits;
          var rowNum = -1;
          while (rowVal > 0) {
            rowVal >>= 1;
            rowNum++;
          }

          console.log(outer + ' and ' + inner + ' columns could be smudged. Only row: ' + rowNum + ' differs.');
          
          smudges[smudgeNum++] = [ outer, rowNum ];
          smudges[smudgeNum++] = [ inner, rowNum ];
        }
      }
    }
  }

  return smudges;
}


function rotateArray(lines) {
  // Check if sub-arrays equal at different spots on vertical reflection
  // Need to choose smallest number for reflection
  // i.e. if line length is 20 and reflection line is at 3, then 0,1,2 and 3,4,5 need to reflect
  // but if reflection is at 12, then 4,5,6,7,8,9,10,11 and 12,13,14,15,16,17,18,19 need to reflect

  var i;
  for (i=0; i<lines.length; ++i) {
    console.log(lines[i]);
  }

  // Determine mirror direction
  var vert = lines.slice();
  var hor = lines.slice();

  var ovr = verticalReflection(vert);
  var ohr = horizontalReflection(hor);

  var direction;
  if (ovr != 0) {
    direction = 'V';
  } else {
    direction = 'H';
  }
  direction = 'A';

  console.log('Original mirror is: ' + direction + " with vr: " + ovr + " and hr: " + ohr);

  //
  // Try each smudge and see if it works
  //
  var list = getPossibleSmudgePoints(lines, direction);

  var smudgeCol;
  var smudgeRow;
  var testMatrix = [];
  var vr = 0;
  var hr = 0;

  for (var sl=0; sl<list.length; ++sl) {
    smudgeCol = list[sl][0];
    smudgeRow = list[sl][1];

    testMatrix = lines.slice();
    printArray(testMatrix);
    console.log("Smudge: [ " + smudgeCol + "," + smudgeRow + "]");
   
    var line = testMatrix[smudgeCol];

    if (testMatrix[smudgeCol][smudgeRow] === "#") {
      // This is a string, so I have to rebuild it
      testMatrix[smudgeCol] = line.substring(0,smudgeCol) + "." + line.substring(smudgeCol+1);
    } else {
      testMatrix[smudgeCol] = line.substring(0,smudgeCol) + "#" + line.substring(smudgeCol+1);
    }
    printArray(testMatrix);

    if (direction === 'V') {
      vert = testMatrix.slice();
      vr = verticalReflection(vert);
    } else {
      hor = testMatrix.slice();
      hr = horizontalReflection(hor);
    }

    if (hr != 0 || vr != 0) {
      console.log("able to mirror. hr: " + hr + " vr: " + vr);
      break;
    }
  } 

  return vr + hr*100;
}

if (process.argv.length !== 3) {
  console.error('Syntax: node', path.basename(process.argv[1]), '<mirror-file>');
  console.error(process.argv.length-2, 'parameters specified');
  process.exit(1);
}

const mirrorFile = process.argv[2];

var data = readFile(mirrorFile);
var lines = [];

if (data === null) {
  process.exit(0);
}

lines = data.split(os.EOL);

var li=0;
var start=0;
var end=0;
var grandTotal = 0;
var maxRow = 0;
var maxCol = 0;
while (li < lines.length) {
  while (li < lines.length && (lines[li].trim() === '')) {
    li = li+1;
  }
  start = li;
  while (li < lines.length && (lines[li].trim() !== '')) {
    li = li+1;
  }
  end = li;
  if (start < lines.length) {
    var total = rotateArray(lines.slice(start, end));
    grandTotal += total;
    var rows=lines[start].length;
    var cols=end-start+1;
    if (maxRow < rows) {
      maxRow = rows;
    }
    if (maxCol < cols) {
      maxCol = cols;
    }
    console.log('Matrix: ', cols, 'x', rows, ' Total: ', total);
  }
}

console.log('Max cols: ', maxCol, ' Max rows: ', maxRow, ' Grand Total: ', grandTotal);
