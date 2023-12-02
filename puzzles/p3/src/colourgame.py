import sys

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

    for line in Lines:
        print(line.strip())

    out = line.split(",")

    return 0

if __name__ == "__main__":
    main()
