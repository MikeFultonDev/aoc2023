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
      vr = (pivot+1);
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
  for (i=1; i<lines.length-2; ++i) {
    reflect = i;
    if (lines.length-i-2 < reflect) {
      reflect = lines.length-i-2;
    }
    var su = i-reflect;
    var eu = i;
    var sb = i+1;
    var eb = i+reflect+1;
    tp = buildArrayHorizontal(lines, su, eu);
    bottom = buildArrayHorizontal(lines, sb, eb);
    bottom = flipArrayHorizontal(bottom);

    if (equalArrays(tp, bottom)) {
      hr = (i+1);
      refText = 'Y';
    } else {
      refText = 'N';
    }

    topText = su + '^' + eu;
    bottomText = sb + 'v' + eb;
      
    console.log(topText, ' and ', bottomText, ':', refText);
  }
  return hr;
}

function rotateArray(lines) {
  // Check if sub-arrays equal at different spots on vertical reflection
  // Need to choose smallest number for reflection
  // i.e. if line length is 20 and reflection line is at 3, then 0,1,2 and 3,4,5 need to reflect
  // but if reflection is at 12, then 4,5,6,7,8,9,10,11 and 12,13,14,15,16,17,18,19 need to reflect

  for (i=0; i<lines.length; ++i) {
    console.log(lines[i]);
  }

  vert = lines.slice();
  hor = lines.slice();

  vr = verticalReflection(vert);
  hr = horizontalReflection(hor);

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
    console.log('Total: ', total);
  }
}

console.log('Grand Total: ', grandTotal);
