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
  for (row=0; row<lines.length; ++row) {
    arr[row] = [];
    for (col=0; col<lines[row].length; ++col) {
      arr[row][col] = lines[row].substring(col,col+1);
    }
  }
  return arr;
}

function buildArrayVertical(lines, start, end) {
  var arr = [];
  for (row=0; row<lines.length; ++row) {
    arr[row] = [];
    for (col=start; col<=end; ++col) {
      arr[row][col-start] = lines[row].substring(col,col+1);
    }
  }
  return arr;
}

function flipArrayVertical(arr) {
  for (row=0; row<arr.length; ++row) {
    var ecol = (arr[row].length)-1;
    for (col=0; col<arr[row].length/2; ++col) {
      var tmp = arr[row][col];
      arr[row][col] = arr[row][ecol-col];
      arr[row][ecol-col] = tmp;
    }
  }
  return arr;
}

function buildArrayHorizontal(lines, start, end) {
  var arr = [];
  for (row=start; row<=end; ++row) {
    arr[row-start] = [];
    for (col=0; col <= lines[row].length; ++col) {
      arr[row-start][col] = lines[row].substring(col,col+1);
    }
  }
  return arr;
}

function flipArrayHorizontal(arr) {
  for (row=0; row<arr.length/2; ++row) {
    var erow = (arr.length)-1;
    for (col=0; col<arr[row].length; ++col) {
      var tmp = arr[row][col];
      arr[row][col] = arr[erow-row][col];
      arr[erow-row][col] = tmp;
    }
  }
  return arr;
}

function equalArrays(left, right) {
  for (row=0; row<left.length; ++row) {
    for (col=0; col<left[row].length; ++col) {
      if (left[row][col] !== right[row][col]) {
        return false;
      }
    }
  }
  return true;
}

function printArray(arr) {
  for (row=0; row<arr.length; ++row) {
    line = '';
    for (col=0; col<arr[row].length; ++col) {
      c = arr[row][col];
      line = line + c;
    }
    console.log(line);
  }
}  

function verticalReflection(lines) {
  vr = 0;
  const lineLength = lines[0].length
  console.log('Vertical Columns:', lineLength);
  for (pivot=1; pivot<lineLength; ++pivot) {
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
    left = buildArrayVertical(lines, sl, el);
    right = buildArrayVertical(lines, sr, er);
    right = flipArrayVertical(right);

    if (equalArrays(left, right)) {
      vr = pivot;
      refText = 'Y';
    } else {
      refText = 'N';
    }
    leftText = sl + '->' + el;
    rightText = sr + '->' + er;
      
    console.log(leftText, ' and ', rightText, ':', refText);
  }
  return vr;
}

function horizontalReflection(lines) {
  hr = 0;
  console.log('Horizontal Rows:', lines.length);
  rowsLength = lines.length;
  for (pivot=1; pivot<rowsLength; ++pivot) {
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
    tp = buildArrayHorizontal(lines, sl, el);
    bottom = buildArrayHorizontal(lines, sr, er);
    bottom = flipArrayHorizontal(bottom);

    if (equalArrays(tp, bottom)) {
      hr = pivot;
      refText = 'Y';
    } else {
      refText = 'N';
    }

    topText = sl + '^' + el;
    bottomText = sr + 'v' + er;
      
    console.log(topText, ' and ', bottomText, ':', refText);
  }
  return hr;
}

function getPossibleSmudgePoints(lines) {
  arr = buildArray(lines);

  smudges = []; 
  smudgeNum = 0;
  vertNum = [];
  for (row=0; row<arr.length; ++row) {
    vertNum[row] = 0;
    for (col=0; col<arr[0].length; ++col) {
      if (arr[row][col] == '#') {
        vertNum[row] |= (1 << col);
        //console.log('vertNum[' + row + '] ' + vertNum[row]);
      }
    }
  }
  for (outer=0; outer<arr.length; ++outer) {
    for (inner=outer+1; inner<arr.length; ++inner) {
      diffBits = (vertNum[outer] ^ vertNum[inner]);
      close = (diffBits & (diffBits - 1));
      //console.log(inner + ' and ' + outer + ' rows Diffbits: ' + diffBits + ' close: ' + close);
      if ((diffBits !== 0) && (close === 0)) {
        colVal = diffBits;
        colNum = -1;
        while (colVal > 0) {
          colVal >>= 1;
          colNum++;
        }

        //console.log(inner + ' and ' + outer + ' rows could be smudged. Only column: ' + colNum + ' differs.');
        
        smudges[smudgeNum++] = [ colNum, inner ];
        smudges[smudgeNum++] = [ colNum, outer ];
      }
    }
  }

  horNum = [];
  for (col=0; col<arr[0].length; ++col) {
    horNum[col] = 0;
    for (row=0; row<arr.length; ++row) {
      if (arr[row][col] == '#') {
        horNum[col] |= (1 << row);
        //console.log('horNum[' + col + '] ' + vertNum[col]);
      }
    }
  }
  for (outer=0; outer<arr.length; ++outer) {
    for (inner=outer+1; inner<arr.length; ++inner) {
      diffBits = (horNum[outer] ^ horNum[inner]);
      close = (diffBits & (diffBits - 1));
      //console.log(inner + ' and ' + outer + ' rows Diffbits: ' + diffBits + ' close: ' + close);
      if ((diffBits !== 0) && (close === 0)) {
        rowVal = diffBits;
        rowNum = -1;
        while (rowVal > 0) {
          rowVal >>= 1;
          rowNum++;
        }

        //console.log(inner + ' and ' + outer + ' columns could be smudged. Only row: ' + rowNum + ' differs.');
        
        smudges[smudgeNum++] = [ inner, rowNum ];
        smudges[smudgeNum++] = [ outer, rowNum ];
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

  for (i=0; i<lines.length; ++i) {
    console.log(lines[i]);
  }

  //
  // Try each smudge and see if it works
  //
  list = getPossibleSmudgePoints(lines);

  for (var i=0; i<list.length; ++i) {
    var smudgeCol = list[i][0];
    var smudgeRow = list[i][1];

    console.log("Smudge: [ " + smudgeCol + "," + smudgeRow + "]");
  
    var testMatrix = lines.slice();
    
    var temp = testMatrix[smudgeCol][smudgeRow];
    if (testMatrix[smudgeCol][smudgeRow] == '#') {
      testMatrix[smudgeCol][smudgeRow] = '.';
    } else {
      testMatrix[smudgeCol][smudgeRow] = '#';
    }
    vert = testMatrix.slice();
    hor = testMatrix.slice();

    vr = verticalReflection(vert);
    hr = horizontalReflection(hor);

    if (hr != 0 || vr != 0) {
      console.log("able to mirror");
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
    total = rotateArray(lines.slice(start, end));
    grandTotal += total;
    rows=lines[start].length;
    cols=end-start+1;
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
