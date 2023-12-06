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
            RECORDING MODE IS V.
         01  INREC.
               05   IN-DATA PIC X(80).
        WORKING-STORAGE SECTION.
         01    IN-EOF        PIC A(1) VALUE "N".
         01    FILE-STATUS  PIC X(2).

        PROCEDURE DIVISION.

          MAIN.
            OPEN INPUT INPUT-FILE.
            PERFORM READ-LINE THRU READ-LINE-EXIT UNTIL IN-EOF = "Y".
            CLOSE INPUT-FILE.
            STOP RUN.
          MAIN-EXIT.
            EXIT.

          READ-LINE.
            READ INPUT-FILE
              AT END
                MOVE "Y" TO IN-EOF
              NOT AT END
                DISPLAY '*******', INREC
            END-READ.
          READ-LINE-EXIT.
            EXIT.
