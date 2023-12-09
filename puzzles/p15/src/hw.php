<?
$data = file($argv[1]);

for ($line = 0; $line < count($data); $line++) {
  //echo $data[$line];
}

$direction=$data[0];

echo "Directions: " . $direction . "\n";
// Map = (Left, Right)
for ($line = 2; $line < count($data); $line++) {
  $words = explode(" ", $data[$line]);
  $name = $words[0];
  $left = substr($words[2],1,-1);
  $right = substr($words[3],0,-2);

  $entry[$line-2]['name'] = $name;
  $entry[$line-2]['left'] = $left;
  $entry[$line-2]['right'] = $right;

  $map[$name]['left'] = $left;
  $map[$name]['right'] = $right;

  echo $entry[$line-2]['name'] . " -> " . $entry[$line-2]['left'] . ", " . $entry[$line-2]['right'] . "\n";
}

$dira = str_split(trim($direction));

$start = "AAA";
$cur = $start;
$steps=0;
for ($loop = 0; $loop < 1000000; $loop++) {
  for ($i = 0; $i < count($dira); $i++) {
    $dir = $dira[$i];
    if ($dir == "L") {
      $cur = $map[$cur]['left'];
    } elseif ($dir == "R") {
      $cur = $map[$cur]['right'];
    } else {
      echo "Unknown Direction: " . $dir . "\n";
    }
    //echo " => " . $cur;
    $steps++;
    if ($cur == "ZZZ") {
      echo "\n (" . $steps . " steps)\n";
      exit(0);
    }
  }
}
echo "Failed to complete after " . $steps . " steps\n";
?>
