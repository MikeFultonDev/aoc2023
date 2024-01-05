        PRINT ON,GEN,DATA
ATOI    CSECT
ATOI    RMODE ANY
ATOI    AMODE 31
*
* Prolog
*
        SAVE (14,12)
        BASR R12,0
        USING *,R12
        LR R3,R1
        STORAGE OBTAIN,LENGTH=DYNL,ADDR=(R11)
        USING DYNAREA,R11
        LR R1,R3
        LA R2,DSA
        ST R2,8(,R13)
        ST R13,DSA+4
        LR R13,R2
*
* Convert the number in the string pointed to by R1 into a number
*
* R4: index
* R1: string
* R10: result
* R3: character
* R5: constant one
* R6: constant ten
* R7: constant '0'
* R8: constant '9'
*
        XR R10,R10
        XR R3,R3
        XR R7,R7
        XR R8,R8
        XR R4,R4

        L  R5,ONE
        L  R6,TEN
        IC R7,ZEROC
        IC R8,NINEC

LOOP    DS 0H
        IC R3,0(R4,R1)
        CR R3,R7
        BL DONE
        CR R3,R8
        BH DONE
        SR R3,R7
        MSR R10,R6
        AR R10,R3
        AR R4,R5
        B  LOOP

DONE    DS 0H
*
* Epilog
*
        L   R13,DSA+4
        STORAGE RELEASE,LENGTH=DYNL,ADDR=(R11)
        LR R15,R10
        RETURN (14,12),RC=(15)

        LTORG
*
* Statics (constants)
*
ZEROC   DC C'0'
NINEC   DC C'9'
ONE     DC F'1'
TEN     DC F'10'

*
* Dynamic (storage obtain'ed) area
*
DYNAREA DSECT
DYNL    EQU DYNEND-*
*
* Stack save area always first
*
DSA     DS 18F
DYNEND  EQU *
*
* Equates
*
R0      EQU 0
R1      EQU 1
R2      EQU 2
R3      EQU 3
R4      EQU 4
R5      EQU 5
R6      EQU 6
R7      EQU 7
R8      EQU 8
R9      EQU 9
R10     EQU 10
R11     EQU 11
R12     EQU 12
R13     EQU 13
R14     EQU 14
R15     EQU 15
        END
