# 
# Simple TCL Program
#
if { $argc != 1 } {
    puts "Syntax: camel.tcl <camel hands file>"
    exit 4
} else {
    set file [lindex $argv 0]
}

set f [open $file]
while {[gets $f line] >= 0} {
    puts $line
}
close $f

