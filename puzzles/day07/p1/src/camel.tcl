# 
# Simple TCL Program
#
array set cv {
  2 2
  3 3
  4 4
  5 5
  6 6
  7 7
  8 8
  9 9
  T 10
  J 11
  Q 12
  K 13
  A 14
}

proc sorthand { left right } {
  lassign $left lcards lvalue lbid
  lassign $right rcards rvalue rbid

  if { $lbid != $rbid } {
    return [expr $lbid - $rbid]
  }

  # Same bid, so check cards from left to right

  set larr [split "$lcards" ""]
  set rarr [split "$rcards" ""]
  set i 0
  foreach {lcard} $larr {
    set lv $::cv($lcard)
    set rcard [lindex $rarr $i]
    set rv $::cv($rcard)
    if { $lv != $rv } {
      return [expr $lv - $rv]
    }
    set i [incr i]
  }
  return 0
}

proc computebid { cards } {
  for {set i 0} {$i < 15} {incr i} {
    set nc($i) 0
  }
  set cardarr [split "$cards" ""]
  foreach {card} $cardarr {
    set v $::cv($card)
    set nc($v) [expr $nc($v)+1]
  }

  # Categorize types of hands by simply scanning multiple times.
  # and return bid. 6 for 5 of a Kind, 5 for 4 of a Kind, ... 1 for One Pair, 0 for High card

  for {set i 0} {$i < 15} {incr i} {
    set c1 $nc($i)
    if { $c1 == 5 } { return 6 }
    if { $c1 == 4 } { return 5 }
    if { $c1 == 3 } {
      for {set j 0} {$j < 15} {incr j} {
        # If 2 found, full house
        set c2 $nc($j)
        if { $c2 == 2 } { return 4 }
      }
      # Just 3 of a kind
      return 3
    }
    if { $c1 == 2 } {
      for {set j 0} {$j < 15} {incr j} {
        set c2 $nc($j)
        # If 2 found, two pair
        if { $c2 == 2 } {
          if { $i != $j } {
            return 2 
          }
        }
        # If 3 found, full house
        if { $c2 == 3 } { 
          return 4 
        }
      }
      # Just 2 of a kind
      return 1
    }
  }
  return 0
}

if { $argc != 1 } {
    puts "Syntax: camel.tcl <camel hands file>"
    exit 4
} else {
    set file [lindex $argv 0]
}

list set hands {}

set f [open $file]
while {[gets $f line] >= 0} {
    set fields [split $line]
    lassign $fields cards value
    set bid [computebid $cards]
    lappend hands [list $cards $value $bid]
}
close $f

set shands [lsort -command sorthand $hands]

set i 1
set tot 0
foreach {hand} $shands {
  lassign $hand cards value bid
  set rank [expr $i * $value]
  puts "Cards: $cards Value: $value Hand Value: $bid Rank: $rank"
  set tot [expr $tot + $rank]
  set i [incr i]
}
puts "Total Winnings: $tot"
