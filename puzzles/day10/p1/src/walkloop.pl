#!/bin/env perl
use feature 'say';
use warnings;
use strict;

# 
# Symbols in the matrix.
# Use global variables since the characters are confusing
#

our $v  = '|';
our $ul = 'F';
our $h  = '-';
our $ur = '7';
our $bl = 'L';
our $br = 'J';
our $s = 'S';

our $du = 'U';
our $dd = 'D';
our $dl = 'L';
our $dr = 'R';

#
# Make the matrix global - this is a simple program
#
our @matrix;

#
# Make the starting point global
#
our $startcol = 0;
our $startrow = 0;

#
# If I have 2 choices of up or down, go the direction I am currently going
# If I have 2 choices of left or right, go the direction I am currently going
# At the start, there will be 2 ways to being. Choose up over down and right over left
#
sub nextChoice {
  my ($row, $col, $dir) = @_;

  my $c = $matrix[$row][$col];

  if (($dir ne $dd) {
    my $up = $matrix[$row-1][$col];
    if (($up eq $v) || ($up eq $ul)) {
      if ($up eq $v) {
        $dir = $du;
      } else {
        $dir = $dl;
      }
      return ($row-1, $col);
    }
  }

  if (($dir ne $dd) {
    my $down = $matrix[$row+1][$col];
    if (($down eq $v) || ($down eq $bl)) {
      if ($down eq $v) {
        $dir = $dd;
      } else {
        $dir = $dr;
      }
      return ($row+1, $col);
    }
  }

  if ($dir ne $dl) {
    my $right = $matrix[$row][$col+1];
    if (($right eq $h) || ($right eq $ur) || ($right eq $br)) {
      if ($right eq $h) {
        $dir = $dr;
      } elsif ($right eq $ur) {
        $dir = $dd;
      } else {
        $dir = $du;
      }
      return ($row, $col+1);
    }
  }

  if ($dir ne $dr) {
    my $left = $matrix[$row][$col-1];
    if (($left eq $h) || ($left eq $ul) || ($left eq $bl)) {
      if ($left eq $h) {
        $dir = $dl;
      } elsif ($left eq $ul) {
        $dir = $dd;
      } else {
        $dir = $du;
      }
      return ($row, $col-1);
    }
  }

  say "Stuck at [$row . $col]";
  exit 8;
}

sub addLine {
  my ($row, $line) = @_;
  my $col = 0;

  my @spl = split('', $line);
  foreach my $i (@spl) {
    $matrix[$row][$col] = $i;
    if ($i eq $s) {
      $startrow = $row;
      $startcol = $col;
    }
    $col = $col+1;
  } 
}

my $args = $#ARGV + 1;

if ($args < 1) {
  say "Syntax: walkloop <input file>";
  exit 4;
}

my $input = $ARGV[0];

open (FH, '<', $input) or die $!;

my $row = 0;
my $col = 0;

my $edge = '';
while (<FH>) {
  my $line = $_;

  if ($edge eq '') {
    my $size = length($line);
    $edge = '.' x $size;
    addLine($row, $edge);
    $row = $row+1;
  }
  # Add dots on the edges so I don't have to check for out of bounds in nextChoice
  $line = ".$line."; 
  addLine($row, $line);
  $row = $row+1;
}
addLine($row, $edge);

say "Start point is [$startrow . $startcol]";

my $dir = ''

($row, $col, $dir) = nextChoice($startrow, $startcol, $dir);

my $count = 0;
while ($matrix[$row][$col] ne $s) {
  say "Next choice is [$row . $col . $dir]";
  $count = $count+1;
  ($row, $col, $dir) = nextChoice($row, $col, $dir);
}

close(FH);
