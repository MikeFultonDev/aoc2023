#!/bin/env python

import sys

RMAX = 12
GMAX = 13
BMAX = 14

def game_results(line):
    possible=True
    gid=1
    return possible,gid

def main():
    if len(sys.argv) < 2:
        sys.stderr.write("Syntax: " + sys.argv[0] + " <colour game file>\n")
        return 4

    input_file = sys.argv[1]

    try:
        colour_file = open(input_file, 'r')
    except:
        sys.stderr.write("Unable to open: " + input_file + " for read\n")
        return 8

    Lines = colour_file.readlines()
    
    tgid = 0
    for line in Lines:
        possible,gid = game_results(line)
        if possible:
            tgid += gid

    print("Total of Possible Game IDs is: " + str(tgid))
    return 0

if __name__ == "__main__":
    main()
