#!/bin/env perl
use feature 'say';
use warnings;
use strict;
my $args = $#ARGV + 1;

if ($args < 1) {
  say "Syntax: walkloop <input file>";
  exit 4;
}

my $input = $ARGV[0];

open (FH, '<', $input) or die $!;

my @matrix;

my $row = 0;
my $col = 0;

my $startcol = 0;
my $startrow = 0;

while (<FH>) {
  my $line = $_;
  say "L: $line";
  my @spl = split('', $line);
  $col = 0;
  foreach my $i (@spl) {
    $matrix[$row][$col] = $i;
    if ($i eq 'S') {
      $startrow = $row;
      $startcol = $col;
    }
    $col = $col+1;
  } 
  $row = $row+1;
}

say "Start point is [$startrow . $startcol]";

close(FH);
