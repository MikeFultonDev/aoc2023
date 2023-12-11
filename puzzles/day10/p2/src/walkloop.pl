#!/bin/env perl
use feature 'say';
use warnings;
use strict;
use v5.10;

# 
# Symbols in the matrix.
# Use global variables since the characters are confusing
#

our $v  = '|'; our $av  = '║';  
our $ul = 'F'; our $aul = '╔';
our $h  = '-'; our $ah  = '═';
our $ur = '7'; our $aur = '╗';
our $bl = 'L'; our $abl = '╚';
our $br = 'J'; our $abr = '╝';
our $s  = 'S';
our $x  = '.';
our $i  = 'I';

our $ds = 'S';
our $de = 'E';
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
  my ($row, $col, $dir, $clean) = @_;

  my $c = $matrix[$row][$col];

  if ($dir eq $du || $dir eq $ds) {
    my $up = $matrix[$row-1][$col];
    if ($up eq $s) {
      return ($row-1, $col, $de);
    }
    if (($up eq $v) || ($up eq $ul) || ($up eq $ur)) {
      if ($up eq $v) {
        $dir = $du;
      } elsif ($up eq $ul) {
        $dir = $dr;
      } else {
        $dir = $dl;
      }
      if ($clean == 1 && $matrix[$row][$col-1] eq $x) {
        $matrix[$row][$col-1] = $i;
      }
      return ($row-1, $col, $dir);
    }
  }

  if ($dir eq $dd || $dir eq $ds) {
    my $down = $matrix[$row+1][$col];
    if ($down eq $s) {
      return ($row+1, $col, $de);
    }
    if (($down eq $v) || ($down eq $bl) || ($down eq $br)) {
      if ($down eq $v) {
        $dir = $dd;
      } elsif ($down eq $bl) {
        $dir = $dr;
      } else {
        $dir = $dl;
      }
      if ($clean == 1 && $matrix[$row][$col+1] eq $x) {
        $matrix[$row][$col+1] = $i;
      }
      return ($row+1, $col, $dir);
    }
  }

  if ($dir eq $dr || $dir eq $ds) {
    my $right = $matrix[$row][$col+1];
    if ($right eq $s) {
      return ($row, $col+1, $de);
    }
    if (($right eq $h) || ($right eq $ur) || ($right eq $br)) {
      if ($right eq $h) {
        $dir = $dr;
      } elsif ($right eq $ur) {
        $dir = $dd;
      } else {
        $dir = $du;
      }
      if ($clean == 1 && $matrix[$row-1][$col] eq $x) {
        $matrix[$row-1][$col] = $i;
      }
      return ($row, $col+1, $dir);
    }
  }

  if ($dir eq $dl || $dir eq $ds) {
    my $left = $matrix[$row][$col-1];
    if ($left eq $s) {
      return ($row, $col-1, $de);
    }
    if (($left eq $h) || ($left eq $ul) || ($left eq $bl)) {
      if ($left eq $h) {
        $dir = $dl;
      } elsif ($left eq $ul) {
        $dir = $dd;
      } else {
        $dir = $du;
      }
      if ($clean == 1 && $matrix[$row+1][$col] eq $x) {
        $matrix[$row+1][$col] = $i;
      }
      return ($row, $col-1, $dir);
    }
  }

  say "Stuck at [$row . $col . $dir]";
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

sub otherPathChar {
  my ($c) = @_;

  given($c) {
    when($v)   { return $av;  }
    when($ul)  { return $aul; }
    when($h)   { return $ah;  }
    when($ur)  { return $aur; }
    when($bl)  { return $abl; }
    when($br)  { return $abr; }
    when($av)  { return $v;   }
    when($aul) { return $ul;  }
    when($ah)  { return $h;   }
    when($aur) { return $ur;  }
    when($abl) { return $bl;  }
    when($abr) { return $br;  }
    default:   { return $c;   }
  }
}

sub clearOriginalPathChar {
  my ($numrows, $numcols) = @_;
  for (my $row = 0; $row < $numrows; ++$row) {
    for (my $col = 0; $col < $numcols; ++$col) {
      my $c = $matrix[$row][$col];

      # Put dash ($h) at end so it does not get mis-interpreted as range
      $c =~ s/([$v$ul$ur$bl$br$h])/$x/g;
      $matrix[$row][$col] = $c;
    }
  }
}

sub switchPathChar {
  my ($numrows, $numcols) = @_;
  for (my $row = 0; $row < $numrows; ++$row) {
    for (my $col = 0; $col < $numcols; ++$col) {
      my $c = $matrix[$row][$col];

      $c = otherPathChar($c);
      $matrix[$row][$col] = $c;
    }
  }
}

sub printMatrix {
  my $row=0;
  my $col=0;

  foreach my $line (@matrix) {
    print join("", @{$line}), "\n";
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
my $numcols = 0;

while (<FH>) {
  my $line = $_;

  # Remove leading/trailing spaces
  $line =~ s/^\s+|\s+$//g ;

  if ($edge eq '') {
    $numcols = length($line)+2;
    $edge = '.' x $numcols;
    addLine($row, $edge);
    $row = $row+1;
  }
  # Add dots on the edges so I don't have to check for out of bounds in nextChoice
  $line = ".$line."; 
  addLine($row, $line);
  $row = $row+1;
}
addLine($row, $edge);
my $numrows = $row;

say "Start point is [$startrow . $startcol]";

my $dir = $ds;

my $clean = 0;
($row, $col, $dir) = nextChoice($startrow, $startcol, $dir, $clean);

my $count = 0;
while ($matrix[$row][$col] ne $s) {
  $count = $count+1;
  my $prevrow = $row;
  my $prevcol = $col;
  ($row, $col, $dir) = nextChoice($row, $col, $dir, $clean);

  $matrix[$prevrow][$prevcol] = otherPathChar($matrix[$prevrow][$prevcol]);
}

my $farthest = $count/2;

printMatrix();

#
# Clear the broken pipes on the ground
#
clearOriginalPathChar($numrows, $numcols);

printMatrix();

#
# Switch the pipe characters back again
#
switchPathChar($numrows, $numcols);

printMatrix();

$matrix[$startrow][$startcol] = $s;
$dir = $ds;
($row, $col, $dir) = nextChoice($startrow, $startcol, $dir, $clean);
$clean = 1;

while ($matrix[$row][$col] ne $s) {
  say "Next choice is [$row . $col . $dir]";
  $count = $count+1;
  my $prevrow = $row;
  my $prevcol = $col;
  ($row, $col, $dir) = nextChoice($row, $col, $dir, $clean);
}

#
# Switch the pipe characters back again
#
switchPathChar($numrows, $numcols);
printMatrix();

say "Loop walk was $count steps. Farthest point away is $farthest";

close(FH);
