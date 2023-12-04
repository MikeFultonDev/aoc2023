REM
REM Schematic reader for AoC
REM

REM
REM Written in the BASIC dialect my-basic from https://github.com/paladin-t/my_basic#introduction
REM with a small enhancement to support EOF when using the INPUT verb
REM
REM I tried to use features that were present in the Applesoft BASIC Language: xxxxhttps://www.applefritter.com/files/Applesoft%20II%202019.pdf
REM
REM While variables were historically in upper-case, I can't do that to myself so I am deviating from the traditional coding style in that regard
REM While DEF/ENDDEF was not part of Applesoft BASIC, again I can't force myself to use GOSUB with line numbers and all global variables.
REM Sorry Mike Junior
REM
REM This code expects: BASIC_EOF=EOF to be exported before running the program

DEF readline()
  INPUT line$;
  RETURN line$
ENDDEF

DEF blankline(line$)
  bl$ = ""
  FOR i = 1 TO LEN(line$)
    bl$ = bl$ + "."
  NEXT i
  RETURN bl$
ENDDEF

REM
REM We will only look 'down' or on the same line for gear ratios
REM so that we don't double-count
REM

DEF stoa(line$, s, l)
  IF l = 0 THEN RETURN -1
  ss$ = MID(line$, s, l)
  RETURN VAL(ss$)
ENDDEF

DEF getfnum(line$, start)
  FOR i = start TO LEN(line$)
    c$ = MID(line$, i, 1)
    IF c$ < "0" OR c$ > "9" THEN RETURN stoa(line$, start, i-start)
  NEXT i
ENDDEF

DEF getbnum(line$, finis)
  FOR i = finis TO 1 STEP -1
    c$ = MID(line$, i, 1)
    IF c$ < "0" OR c$ > "9" THEN RETURN stoa(line$, i+1, finis-i)
  NEXT i
ENDDEF

DEF getbindex(line$, finis)
  FOR i = finis TO 1 STEP -1
    c$ = MID(line$, i, 1)
    IF c$ < "0" OR c$ > "9" THEN RETURN i+1
  NEXT i
  RETURN 0
ENDDEF

DEF getnum(line$, i, direction)
  IF direction > 0 THEN RETURN getfnum(line$, i)
  IF direction < 0 THEN RETURN getbnum(line$, i)
ENDDEF

DEF checkexactstartnum(line$, s)
  RETURN getnum(line$, s, 1)
ENDDEF

DEF checkexactendnum(line$, f)
  RETURN getnum(line$, f, -1)
ENDDEF

REM If none of the number is in the min to max range, return 0
REM If digit is found in the range, it could be a valid number
REM BUT - there could be 2 numbers - which we need to reject

REM Catch the special case where a digit is at min and one is at max
REM We know that min and max only differ by 2

DEF checkchoicenum(line$, l, c, r)
  nl = getnum(line$, l, -1)
  nc = getnum(line$, c, 1)
  nr = getnum(line$, r, 1)

REM PRINT "choice num. nl: " + STR(nl) + " nc: " + STR(nc) + " nr: " + STR(nr);

  IF nl > 0 AND nr > 0 AND nc = -1 THEN RETURN -1
  IF nl = 0 AND nr = 0 AND nc = -1 THEN RETURN -1
  IF nl > 0 AND nc = -1 THEN RETURN nl
  IF nr > 0 AND nc = -1 THEN RETURN nr

  REM Go backwards to get to the start index of the number and then get the number

  si = getbindex(line$, l)
  
  num = getnum(line$, si, 1)
REM PRINT "num: " + STR(num) + " si: " + STR(si);
  RETURN num
ENDDEF

DEF aindex(line$, s, f)
  FOR i = s TO f
    IF MID(line$, i, 1) = "*" THEN RETURN i
  NEXT i
  RETURN 0
ENDDEF

DEF addgearratio(s$, f$, first$, second$, third$)

  sv = VAL(s$)
  fv = VAL(f$)

REM PRINT "Start: " + s$ + " Finis: " + f$;
REM PRINT "First: " + first$;
REM PRINT "Second: " + second$;
REM PRINT "Third: " + third$;

  l = fv - sv + 1
  nums$ = MID(first$, sv, l)
  g1 = VAL(nums$)

  REM Check if * to the right, and there is a number following immediately on same line
  REM Check if * is below, and there is a number immediately preceding on line of asterisk
  REM Check if * is below, and there is a number immediately following on line of asterisk
  REM Check if * is below, and there is a number on the third line touching
  REM Check if * to the left, and there is a number below
  REM Check if * to the right, and there is a number below
  REM Check if * is below, and there is a number on my line to the right

  g2 = -1
  g3 = -1
  g4 = -1
  g5 = -1
  g6 = -1
  g7 = -1
  g8 = -1
  IF MID(first$, fv+1, 1) = "*" THEN g2 = checkexactstartnum(first$, fv+2)

  asterisk = aindex(second$, sv-1, fv+1)
  IF asterisk > 0 THEN g3 = checkexactendnum(second$, asterisk-1)
  IF asterisk > 0 THEN g4 = checkexactstartnum(second$, asterisk+1)
  IF asterisk > 0 THEN g5 = checkchoicenum(third$, asterisk-1, asterisk, asterisk+1)

  IF MID(first$, sv-1, 1) = "*" THEN g6 = checkchoicenum(second$, sv-2, sv-1, sv)
  IF MID(first$, fv+1, 1) = "*" THEN g7 = checkchoicenum(second$, fv, fv+1, fv+2)
  IF MID(second$, fv+1, 1) = "*" THEN g8 = checkexactstartnum(first$, fv+2)

  REM If there are 0 adjacent numbers, return 0

  REM PRINT "Asterisk: " + STR(asterisk);
  REM PRINT "G1: " + STR(g1);
  REM PRINT "G2: " + STR(g2);
  REM PRINT "G3: " + STR(g3);
  REM PRINT "G4: " + STR(g4);
  REM PRINT "G5: " + STR(g5);
  REM PRINT "G6: " + STR(g6);
  REM PRINT "G7: " + STR(g7);
  REM PRINT "G8: " + STR(g8);

  IF g1 < 0 THEN g1 = 0
  IF g2 < 0 THEN g2 = 0
  IF g3 < 0 THEN g3 = 0
  IF g4 < 0 THEN g4 = 0
  IF g5 < 0 THEN g5 = 0
  IF g6 < 0 THEN g6 = 0
  IF g7 < 0 THEN g7 = 0
  IF g8 < 0 THEN g8 = 0

  gN = g2+g3+g4+g5+g6+g7+g8
  IF gN = 0 THEN RETURN 0

  REM If more than 1 set of numbers are adjacent, return 0

  IF gN <> g2 AND gN <> g3 AND gN <> g4 AND gN <> g5 AND gN <> g6 AND gN <> g7 AND gN <> g8 THEN RETURN 0

  PRINT STR(g1) + " * " + STR(gN) + " = " + STR(g1*gN);
  RETURN g1*gN
ENDDEF

DEF addtreegearratio(is$, line$)
  REM
  REM      *
  REM     n m
  REM

  index = VAL(is$)

  IF MID(line$, index, 1) <> "." THEN RETURN 0

  n = getnum(line$, index-1, -1)
  m = getnum(line$, index+1, 1)

  IF n <= 0 OR m <= 0 THEN RETURN 0
  PRINT STR(n) + " * " + STR(m) + " = " + STR(n*m);
  RETURN n*m
ENDDEF

DEF addgearratioline(first$, second$, third$)
  REM
  REM Add a '.' at the start and end of each line so that it's safe to navigate around 
  REM the 8 positions around each numeric digit looking for a match
  REM

  first$ = "." + first$ + "."
  second$ = "." + second$ + "."
  third$ = "." + third$ + "."

  REM
  REM Basic has the first element at index 1, but MID is 0 based
  REM

  REM Find the bounds of each number one at a time

  s = 0
  f = 0
  linetot = 0
  FOR i = 1 TO LEN(first$)-1
    c$ = MID(first$, i, 1)    
    IF c$ >= "0" AND c$ <= "9" THEN IF s = 0 THEN s = i
    IF c$ >= "0" AND c$ <= "9" THEN f = i
    ss$ = STR(s)
    fs$ = STR(f)

    REM Values have to be passed as strings

    IF c$ < "0" OR c$ > "9" AND s > 0 THEN linetot = linetot + addgearratio(ss$, fs$, first$, second$, third$)
    IF c$ < "0" OR c$ > "9" AND s > 0 THEN i=f
    IF c$ < "0" OR c$ > "9" AND s > 0 THEN s=0 
  NEXT i

  REM Check if * on first line and both numbers on either side below

  FOR j = 1 TO LEN(first$)-1
    is$ = STR(j)
    IF MID(first$, j, 1) = "*" THEN linetot = linetot + addtreegearratio(is$, second$)
  NEXT j

  RETURN linetot
ENDDEF

DEF addgearratiolastlines(second$, third$)

  gearratiotot = 0
  fourth$ = blankline(third$)
  fifth$ = blankline(third$)
  gearratiotot = gearratiotot + addgearratioline(second$, third$, fourth$)
  gearratiotot = gearratiotot + addgearratioline(third$, fourth$, fifth$)

  RETURN gearratiotot
ENDDEF
  
DEF main()
  REM No While loop in the original language, but FOR i = 1 TO was supported, but not in this emulator

  firstline$ = ""
  secondline$ = ""
  thirdline$ = ""
  done = 0
  gearratiotot = 0
  FOR i = 1 TO 1000000 
    IF firstline$ = "" THEN firstline$ = blankline(line$)
    IF secondline$ = "" THEN secondline$ = readline()
    IF thirdline$ = "" THEN thirdline$ = readline()
    IF thirdline$ = "EOF" THEN thirdline$ = blankline(secondline$) : done = 1 

    gearratiotot = gearratiotot + addgearratioline(firstline$, secondline$, thirdline$)


    IF done = 1 THEN gearratiotot = gearratiotot + addgearratiolastlines(secondline$, thirdline$) : RETURN gearratiotot

    firstline$ = secondline$ + ""
    secondline$ = thirdline$ + ""
    thirdline$ = ""
  NEXT i

ENDDEF

gearratiotot=main()
tots$=STR(gearratiotot)
REM PRINT "Gear Ratio Total is: " + tots$; <-- This total is actually wrong?

