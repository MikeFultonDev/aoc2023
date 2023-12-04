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

DEF addgearratio(s$, f$, before$, this$, after$)
  sv = VAL(s$)
  fv = VAL(f$)

  l = fv - sv + 1
  nums$ = MID(this$, sv, l)
  num = VAL(nums$)

  PRINT "Process Number: " + num$;

  RETURN 0
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
  i = 1
  WHILE i < LEN(first$)
    c$ = MID(first$, i, 1)    
    IF c$ >= "0" AND c$ <= "9" THEN IF s = 0 THEN s = i
    IF c$ >= "0" AND c$ <= "9" THEN f = i
    ss$ = STR(s)
    fs$ = STR(f)

    REM Values have to be passed as strings

    IF c$ < "0" OR c$ > "9" AND s > 0 THEN linetot = linetot + addgearratio(ss$, fs$, before$, this$, after$)
    IF c$ < "0" OR c$ > "9" AND s > 0 THEN i=f
    IF c$ < "0" OR c$ > "9" AND s > 0 THEN s=0 

    i=i+1
  WEND

  RETURN linetot
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

    IF done = 1 THEN RETURN gearratiotot

    firstline$ = secondline$ + ""
    secondline$ = thirdline$ + ""
    thirdline$ = ""
  NEXT i

ENDDEF

gearratiotot=main()
tots$=STR(gearratiotot)
PRINT "Gear Ratio Total is: " + tots$;

