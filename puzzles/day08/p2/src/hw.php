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

$e = 0;
for ($i = 0; $i < count($entry); $i++) {
  if (str_ends_with($entry[$i]['name'], "A")) {
    $cur[$e++] = $entry[$i]['name'];
  }
}

echo "Starting points:\n";
for ($entry = 0; $entry < count($cur); ++$entry) {
  $index = $cur[$entry];
  echo $index . "\n";
}
for ($entry = 0; $entry < count($cur); ++$entry) {
  $complete[$entry] = 0;
}
$steps=0;
$loops = 0;
for ($loop = 0; $loop < 1000000; $loop++) {
  for ($i = 0; $i < count($dira); $i++) {
    $dir = $dira[$i];
    for ($entry = 0; $entry < count($cur); ++$entry) {
      if ($dir == "L") {
        $cur[$entry] = $map[$cur[$entry]]['left'];
      } elseif ($dir == "R") {
        $cur[$entry] = $map[$cur[$entry]]['right'];
      } else {
        echo "Unknown Direction: " . $dir . "\n";
      }
    }
    $steps++;
    for ($entry = 0; $entry < count($cur); ++$entry) {
      if (str_ends_with($cur[$entry], "Z")) {
        if ($complete[$entry] == 0) {
          $complete[$entry] = $steps;
          $loops++;
        }
      }
    }
    if ($loops == count($cur)) {
      echo "All step counts known. Compute LCM of:\n";
      for ($entry = 0; $entry < count($cur); ++$entry) {
        echo $complete[$entry] . " ";
      }
      echo "\n";
      exit(0);
    }
  }
}
echo "Failed to complete after " . $steps . " steps\n";
?>
