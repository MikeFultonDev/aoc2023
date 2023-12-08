# 
# Simple TCL Program
#
array set cv {
  J 1
  2 2
  3 3
  4 4
  5 5
  6 6
  7 7
  8 8
  9 9
  T 10
  Q 11
  K 12
  A 13
}

array set nc { }

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
    set ::nc($i) 0
  }
  set cardarr [split "$cards" ""]
  foreach {card} $cardarr {
    set v $::cv($card)
    set ::nc($v) [expr $::nc($v)+1]
  }

  # Categorize types of hands by simply scanning multiple times.
  # and return bid. 6 for 5 of a Kind, 5 for 4 of a Kind, ... 1 for One Pair, 0 for High card
  # Start at 2 so jokers are skipped

  for {set i 2} {$i < 15} {incr i} {
    set c1 $::nc($i)
    if { $c1 == 5 } { return 6 }
    if { $c1 == 4 } { return 5 }
    if { $c1 == 3 } {
      for {set j 2} {$j < 15} {incr j} {
        # If 2 found, full house
        set c2 $::nc($j)
        if { $c2 == 2 } { return 4 }
      }
      # Just 3 of a kind
      return 3
    }
    if { $c1 == 2 } {
      for {set j 2} {$j < 15} {incr j} {
        set c2 $::nc($j)
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

proc adjustbid { bid cards } {
  set jokers $::nc(1)
  if { $jokers == 0 } {
    puts "cards: $cards no jokers"
    return $bid
  }
  puts "$jokers jokers"

  # I have at least one joker

  for {set i 2} {$i < 15} {incr i} {
    set c1 $::nc($i)
    # With 4 of a kind, it must be one joker, so bump to 5 of a kind (6)
    if { $c1 == 4 } { 
      return 6 
    } 

    # With 3 of a kind, can not be full house (since there are jokers)
    # so must be 1 or 2 jokers which gives us either 4 of a kind or 5 of a kind
    if { $c1 == 3 } { 
      return [ expr $bid + 1 + $jokers ]
    }
  }

  for {set i 2} {$i < 15} {incr i} {
    set c1 $::nc($i)
    # With a pair, can be one pair or two
    # If one pair, bump to 3 of a kind, 4 of a kind, 5 of a kind
    # If two pair, bump to full house, 4 of a kind, 5 of a kind
    if { $c1 == 2 } { 
      if { $bid == 1 } {
        if { $jokers == 1 } {
          return [ expr $bid + 1 + $jokers ]
        } else {
          return [ expr $bid + 2 + $jokers ]
        }
      } else {
        return [ expr $bid + 1 + $jokers ]
      }
    }
  }

  # Only jokers of value
  # Map to values for 2, 3, 4, 5 of a kind
  if { $jokers == 1 } {
    return 1
  } elseif { $jokers == 2 } {
    return 3
  } elseif { $jokers == 3 } {
    return 5
  } elseif { $jokers == 4 } {
    return 6
  } elseif { $jokers == 5 } {
    return 6
  } else {
    puts "Logic error. Did not expect $jokers"
  }
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
    set bid [adjustbid $bid $cards]
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
