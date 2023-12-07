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
           05    T          PIC X(4) OCCURS 4 TIMES.
         01    DISTANCE-TABLE.
           05    D          PIC X(4) OCCURS 4 TIMEs.
         01    I            PIC 9.

        PROCEDURE DIVISION.

          MAIN.
            OPEN INPUT INPUT-FILE.
            PERFORM READ-TIME.
            PERFORM READ-DISTANCE.
            CLOSE INPUT-FILE.
            STOP RUN.
            EXIT.

          READ-TIME.
            READ INPUT-FILE
              NOT AT END
                UNSTRING INREC DELIMITED BY ALL SPACE
                  INTO TME T(1) T(2) T(3) T(4)
                END-UNSTRING.
            DISPLAY '--' TME '--'.
            PERFORM VARYING I FROM 1 BY 1 UNTIL I = 4
              DISPLAY T(I)
            END-PERFORM.

          READ-DISTANCE.
            READ INPUT-FILE
              NOT AT END
                UNSTRING INREC DELIMITED BY ALL SPACE
                  INTO DISTANCE D(1) D(2) D(3) D(4)
                END-UNSTRING.
            DISPLAY '--' DISTANCE.
            PERFORM VARYING I FROM 1 BY 1 UNTIL I = 4
              DISPLAY D(I)
            END-PERFORM.
