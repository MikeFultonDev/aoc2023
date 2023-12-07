        IDENTIFICATION DIVISION.
        PROGRAM-ID. HW.
        ENVIRONMENT DIVISION.
        INPUT-OUTPUT SECTION.
        FILE-CONTROL.
            SELECT INPUT-FILE ASSIGN TO 'INPUT'
            ORGANIZATION IS SEQUENTIAL
            ACCESS MODE IS SEQUENTIAL
            FILE STATUS IS FILE-STATUS.
        DATA DIVISION.
        FILE SECTION.
        FD INPUT-FILE
            RECORDING MODE IS F.
         01  INREC.
               05   IN-DATA PIC X(80).
        WORKING-STORAGE SECTION.
         01    IN-EOF        PIC A(1) VALUE "N".
         01    FILE-STATUS  PIC X(2).
         01    TME         PIC X(10).
         01    DISTANCE     PIC X(10).
         01    TIME-TABLE.
           05    T          PIC 9(4) OCCURS 5 TIMES.
         01    DISTANCE-TABLE.
           05    D          PIC 9(4) OCCURS 5 TIMEs.
         01    I            PIC 9(8).
         01    TI           PIC 9(8).
         01    TRAVEL-TIME  PIC 9(8).
         01    CUR-DISTANCE PIC 9(8).
         01    MAX-DISTANCE PIC 9(8).
         01    MAX-TIME     PIC 9(8).
         01    SPEED        PIC 9(8).
         01    WINS         PIC 9(8).
         01    TOT-WINS     PIC 9(8).

        PROCEDURE DIVISION.

          MAIN.
            OPEN INPUT INPUT-FILE.
            PERFORM READ-TIME.
            PERFORM READ-DISTANCE.
            COMPUTE TOT-WINS = 1.
            PERFORM DISPLAY-WINNERS VARYING I FROM 1 BY 1 UNTIL I = 5.
            DISPLAY TOT-WINS.
            CLOSE INPUT-FILE.
            STOP RUN.
            EXIT.

          READ-TIME.
            READ INPUT-FILE
              NOT AT END
                UNSTRING INREC DELIMITED BY ALL SPACE
                  INTO TME T(1) T(2) T(3) T(4) T(5)
                END-UNSTRING.

          READ-DISTANCE.
            READ INPUT-FILE
              NOT AT END
                UNSTRING INREC DELIMITED BY ALL SPACE
                  INTO DISTANCE D(1) D(2) D(3) D(4) D(5)
                END-UNSTRING.

          DISPLAY-WINNERS.
            COMPUTE WINS = 0.
            COMPUTE MAX-TIME = T(I).
            COMPUTE CUR-DISTANCE = D(I).
      *     DISPLAY 'TIME: ' MAX-TIME.
      *     DISPLAY 'DISTANCE: ' CUR-DISTANCE.
            PERFORM CALC-WINS VARYING TI FROM 1 BY 1 UNTIL TI = MAX-TIME.
            DISPLAY WINS.
            COMPUTE TOT-WINS = TOT-WINS * WINS.

          CALC-WINS.
            COMPUTE MAX-DISTANCE = TI * (MAX-TIME - TI).
            IF MAX-DISTANCE > CUR-DISTANCE THEN
              ADD 1 TO WINS
            END-IF.
      *       DISPLAY 'I: ' I ' MAX-TIME: ' MAX-TIME.
      *       DISPLAY ' TI: ' TI ' CUR-DISTANCE: ' CUR-DISTANCE.
