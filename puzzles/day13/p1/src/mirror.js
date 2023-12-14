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

if (process.argv.length !== 3) {
  console.error('Syntax: node', path.basename(process.argv[1]), '<mirror-file>');
  console.error(process.argv.length-2, 'parameters specified');
  process.exit(1);
}

const mirrorFile = process.argv[2];

var data = readFile(mirrorFile);
var lines = [];

lines = data.split(os.EOL);

for (const line of lines) {
  console.log(line);
}

// Check if sub-arrays equal at different spots on vertical reflection
// Need to choose smallest number for reflection
// i.e. if line length is 20 and reflection line is at 3, then 0,1,2 and 3,4,5 need to reflect
// but if reflection is at 12, then 4,5,6,7,8,9,10,11 and 12,13,14,15,16,17,18,19 need to reflect

const lineLength = lines[0].length
console.log(lineLength);
verticalReflection = 0;
for (i=1; i<lineLength-2; ++i) {
  reflect = i;
  if (lineLength-i-2 < reflect) {
    reflect = lineLength-i-2;
  }
  var sl = i-reflect;
  var el = i;
  var sr = i+1;
  var er = i+reflect+1;
  left = buildArrayVertical(lines, sl, el);
  right = buildArrayVertical(lines, sr, er);
  right = flipArrayVertical(right);

  if (equalArrays(left, right)) {
    verticalReflection = i;
    refText = 'Y';
  } else {
    refText = 'N';
  }

  leftText = sl + '->' + el;
  rightText = sr + '->' + er;
    
  console.log(leftText, ' and ', rightText, ':', refText);
}
