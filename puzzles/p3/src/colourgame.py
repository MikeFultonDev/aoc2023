#!/bin/env python

import sys

RMAX = 12
GMAX = 13
BMAX = 14

def game_results(line):
    if line.strip() == '':
        return False, 0

    game_info = line.strip().split(':')
    game_el = game_info[0].split(' ')
    game_id = int(game_el[1])

    reveals = game_info[1].split(';')

    for reveal in reveals:
        red_tot = green_tot = blue_tot = 0
        grabs = reveal.split(',')
        for grab in grabs:
            tok = grab.strip().split(' ')
            num = int(tok[0])
            colour = tok[1]
            match colour:
                case 'red':
                    red_tot += num
                case 'green':
                    green_tot += num
                case 'blue':
                    blue_tot += num
        if red_tot > RMAX or green_tot > GMAX or blue_tot > BMAX:
            return False, game_id

    return True, game_id

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

    lines = colour_file.readlines()
    
    tgid = 0
    for line in lines:
        possible, gid = game_results(line)
        if possible:
            tgid += gid

    print("Total of Possible Game IDs is: " + str(tgid))
    return 0

if __name__ == "__main__":
    main()
