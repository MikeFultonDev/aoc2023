#!/bin/env python

import sys

def game_results(line):
    if line.strip() == '':
        return False, 0

    game_info = line.strip().split(':')
    game_el = game_info[0].split(' ')
    game_id = int(game_el[1])

    reveals = game_info[1].split(';')

    min_red = min_green = min_blue = 0
    for reveal in reveals:
        grabs = reveal.split(',')
        for grab in grabs:
            tok = grab.strip().split(' ')
            num = int(tok[0])
            colour = tok[1]
            match colour:
                case 'red':
                    if num > min_red:
                        min_red = num
                case 'green':
                    if num > min_green:
                        min_green = num
                case 'blue':
                    if num > min_blue:
                        min_blue = num

    power = min_red * min_green * min_blue
    return True, power

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
        possible, power = game_results(line)
        if possible:
            tgid += power

    print("Total of Powers for Possible Game IDs is: " + str(tgid))
    return 0

if __name__ == "__main__":
    main()
