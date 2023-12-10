#!/bin/env perl
use feature 'say';
use warnings;
use strict;

sub nextChoice {
  my ($row, $col) = @_;

  say "Next choice is [$row . $col]";
  return ($row, $col);
}

my $args = $#ARGV + 1;

if ($args < 1) {
  say "Syntax: walkloop <input file>";
  exit 4;
}

my $input = $ARGV[0];

open (FH, '<', $input) or die $!;

our @matrix;

my $row = 0;
my $col = 0;

my $startcol = 0;
my $startrow = 0;

my $s = 'S';

while (<FH>) {
  my $line = $_;
  say "L: $line";
  my @spl = split('', $line);
  $col = 0;
  foreach my $i (@spl) {
    $matrix[$row][$col] = $i;
    if ($i eq $s) {
      $startrow = $row;
      $startcol = $col;
    }
    $col = $col+1;
  } 
  $row = $row+1;
}

say "Start point is [$startrow . $startcol]";

# 
# Check what direction I can go
# Use variables since the characters are confusing
#

my $v  = '|';
my $ul = 'F';
my $h  = '-';
my $ur = '7';
my $bl = 'L';
my $br = 'J';

# If I have 2 choices of up or down, go the direction I am currently going
# If I have 2 choices of left or right, go the direction I am currently going

($row, $col) = nextChoice($startrow, $startcol);

my $count = 0;
while ($matrix[$row][$col] ne $s) {
  $count = $count+1;
  ($row, $col) = nextChoice($row, $col);
}

close(FH);
