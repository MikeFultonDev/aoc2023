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

DEF addpartnumber(s$, f$, before$, this$, after$)
  sv = VAL(s$)
  fv = VAL(f$)

  l = fv - sv + 1
  num = MID(this$, sv, l)
  PRINT "  Number: " + num;
ENDDEF

DEF addpartnumbers(before$, this$, after$)
  REM
  REM Add a '.' at the start and end of each line so that it's safe to navigate around 
  REM the 8 positions around each numeric digit looking for a match
  REM

  before$ = "." + before$ + "."
  this$ = "." + this$ + "."
  after$ = "." + after$ + "."

  PRINT "before: " + before$;
  PRINT "this:   " + this$;
  PRINT "after:  " + after$;

  REM
  REM Basic has the first element at index 1
  REM

  REM Find the bounds of each number one at a time

  s = 0
  f = 0
  tot = 0
  PRINT "Processing: " + this$;
  FOR i = 1 TO LEN(this$) - 1
    c$ = MID(this$, i, 1)    
    IF c$ >= "0" AND c$ <= "9" THEN IF s = 0 THEN s = i
    IF c$ >= "0" AND c$ <= "9" THEN f = i
    ss$ = STR(s)
    fs$ = STR(f)

    REM Values have to be passed as strings

    IF c$ < "0" OR c$ > "9" AND s > 0 THEN tot = tot + addpartnumber(ss$, fs$, before$, this$, after$) : s = 0
  NEXT i

  RETURN tot
ENDDEF

DEF main()
  REM No While loop in the original language, but FOR i = 1 TO was supported, but not in this emulator
  linebefore$ = ""
  lineafter$ = ""
  line$ = ""
  done = 0
  FOR i = 1 TO 1000000 
    IF line$ = "" THEN line$ = readline()
    IF linebefore$ = "" THEN linebefore$ = blankline(line$)
    IF lineafter$ = "" THEN lineafter$ = readline()
    IF lineafter$ = "EOF" THEN lineafter$ = blankline(line$) : done = 1
    thisline$ = line$

    linepartnumbertotal = addpartnumbers(linebefore$, thisline$, lineafter$)

    IF done = 1 THEN RETURN 0

    linebefore$ = thisline$ + ""
    line$ = lineafter$ + ""
    lineafter$ = ""
  NEXT i

ENDDEF

main()

