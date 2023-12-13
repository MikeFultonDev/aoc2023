/* REXX */
Parse Arg fn .
Trace 'O'

rc = ReadFile(fn)

i=1
Do While i <= line.0
  Say line.i
  Parse Var line.i string numbers
  matches = CountChoices(string, numbers)
  i = i+1
End
Exit 0

Matches: Procedure
Parse Arg string, index, number

  /*
   * Input number from data file is 0-index, so add 1
   */
  number = number+1
  tryBroken = TRANSLATE(string, '#', '?')
  If (SUBSTR(tryBroken, 1, number) == COPIES('#', number)) Then Do
    nextChar = SUBSTR(string, number+1, 1);
    If (nextChar <> '#') Then Do
      Return 1
    End
  End
Return 0

CountChoices: Procedure
Parse Arg string, numbers

  Parse Var numbers nextNumber ',' numbers 

  numMatches = Matches(string, 1, nextNumber)
  If (numMatches > 0) Then Do
    Parse Var numbers nextNumber ',' numbers
    nextString = STRIP(SUBSTR(string, nextNumber))
    If (nextString <> '') Then Do
      numMatches = numMatches * CountChoices(nextString, numbers)
    End
    Return numMatches
  End
  Else Do
    Return 0
  End

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

  count = 0
  line.0 = count
  Do Until bytes <= 0
    'read' fd 'bytes 80'
    If retval = -1 Then
    Do
      Say 'Unable to read file: ' fn 'Error: ' errno errnojr
      Return 8
    End
    count=count+1
    nocr = SUBSTR(bytes, 1, length(bytes)-1)
    line.count = STRIP(nocr)
  End
  line.0 = count

  'close' fd

Return 0 
