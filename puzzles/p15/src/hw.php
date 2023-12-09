<?
var_dump($argc); //number of arguments passed
    var_dump($argv); //the arguments passed
$data = file($argv[1]);

for ($line = 0; $line < count($data); $line++) {
  echo $data[$line];
}

$direction=$data[0];

// Map = (Left, Right)
for ($line = 2; $line < count($data); $line++) {
  $words = explode(" ", $data[$line]);
  $name = $words[0];
  $left = $words[2];
  $right = $words[3];
  $map[$line-2]["name"] = $name;
  $map[$line-2]["left"] = $left;
  $map[$line-2]["right"] = $right;

  echo $map[$line-2]
}

?>
