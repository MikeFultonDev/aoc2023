/* REXX */
Parse Arg fn .
Trace 'O'

rc = ReadFile(fn)

i=1
Say line.0 'lines'
totMatches = 0
Do While i <= line.0
  Parse Var line.i string numbers
  Say line.i
  matches = CountChoices(string, numbers, 1, '')
  Say 'Matches:' matches
  totMatches = totMatches + matches
  i = i+1
End

Say 'Total Matches:' totMatches
Exit 0

Matches: Procedure
Parse Arg string, index, number
  If (number = '') Then Do
    Return 0
  End
  tryBroken = TRANSLATE(string, '#', '?')
  If (SUBSTR(tryBroken, 1, number) == COPIES('#', number)) Then Do
    nextChar = SUBSTR(string, number+1, 1);
    If (nextChar <> '#') Then Do
      /* Say string " " number-1 ": " matches */
      Return 1
    End
  End
Return 0

CountChoices: Procedure
Parse Arg string, numbers, numMatches, builtString

  Parse Var numbers nextNumber ',' nextNumbers 

  firstChar = LEFT(string, 1)
  If (firstChar = '') Then Do
    If (nextNumber = '') Then Do
      /* Say "--> " builtString */
      Return numMatches
    End
    Else Do
      Return 0
    End
  End

  numMatchesA = Matches(string, 1, nextNumber)
  If (numMatchesA > 0) Then Do
    nextString = STRIP(SUBSTR(string, nextNumber+1))
    firstNextChar = LEFT(nextString, 1)
    If (firstNextChar = '.' | firstNextChar = '?') Then Do
      nextString = SUBSTR(nextString, 2)
      broken = COPIES('#', nextNumber)
      builtStringA = builtString broken "."
      numMatchesA = CountChoices(nextString, nextNumbers, 1, builtStringA)
    End
    Else Do
      If (firstNextChar = '' & nextNumbers = '') Then Do
        Say "--> " builtString
        Return numMatches
      End
      Else Do
        numMatchesA = 0
      End
    End
  End
  If (firstChar = '.' | firstChar = '?') Then Do
    builtStringB = builtString "."
    numMatchesB = CountChoices(SUBSTR(string, 2), numbers, 1, builtStringB)
  End
  Else Do
    numMatchesB = 0
  End

  Return (numMatchesA + numMatchesB)*numMatches

ReadFile: Procedure Expose line.
  Parse Arg fn

  Call syscalls 'ON'
  Address syscall 
  path=fn
  'open (path)' O_RDONLY 000
  If retval = -1 Then 
  Do
    Say 'Unable to open file: ' fn 'Error: ' errno errnojr
    Return 4
  End

  fd = retval

  'read' fd 'bytes 100000'
  If retval = -1 Then
  Do
    Say 'Unable to read file: ' fn 'Error: ' errno errnojr
    Return 8
  End
  cr=x2c(15)
  count = 0
  line.0 = count
  Do Until LENGTH(bytes) = 0
    count=count+1
    Parse Var bytes rec (cr) remainder
    line.count = rec
    bytes = remainder
  End
  line.0 = count

  'close' fd

Return 0 
