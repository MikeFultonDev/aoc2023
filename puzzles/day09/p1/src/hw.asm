* BPX1WRT: https://tech.mikefulton.ca/bpx1wrt
* BPX1RED: https://tech.mikefulton.ca/bpx1red
* HL/ASM 'RENT' https://tech.mikefulton.ca/asmrentvideo
* BPX Service call: https://tech.mikefulton.ca/callbpxsvc
* 
        PRINT ON,GEN,DATA
HELLO   CSECT
HELLO   RMODE ANY
HELLO   AMODE 31
* 
* Prolog
*
        SAVE (14,12)
        BASR R12,0
        USING *,R12
        STORAGE OBTAIN,LENGTH=DYNL,ADDR=(R11)
        USING DYNAREA,R11

        LA R2,DSA
        ST R2,8(,R13)
        ST R13,DSA+4
        LR R13,R2
*
* Body
* Write Hello World to STDOUT
*

*
* Store values into parameter list
*
        MVC REC(HWL),HW
        LA  R1,REC
        ST  R1,RECA
        LA  R1,HWL
        ST  R1,RECL
        L   R1,STDOUT
        ST  R1,FD
        L   R1,BPXALET
        ST  R1,ALET

        CALL  BPX1WRT,(FD,                                             x
               RECA,                                                   x
               ALET,                                                   x
               RECL,                                                   x
               RV,                                                     x
               RC,                                                     x
               RN),MF=(E,BPXWRTD)

        L   R8,RV
        L   R9,RC
        L   R10,RN
*
* Epilog
*
        L   R13,DSA+4
        STORAGE RELEASE,LENGTH=DYNL,ADDR=(R11)
        RETURN (14,12),RC=0

*
* Statics, Dynamic Storage, Equates follows
* 
* Naming convention:
* Suffixes:
*  L : length
*  S : static
*  D : dynamic
*  A : address

        LTORG
*
* Statics (constants)
*
STDIN   DC F'0'
STDOUT  DC F'1'
STDERR  DC F'2'
BPXALET DC F'0'
BPX1WRT DC V(BPX1WRT)

BPXWRTS CALL  ,(0,0,0,0,0,0,0),MF=L
BPXWRTL EQU *-BPXWRTS

HW      DC C'Hello World'
NEWLINE DC X'15'
HWL     EQU *-HW

*
* Dynamic (storage obtain'ed) area
*
DYNAREA DSECT
*
* Dynamic Save Area regs always first
*
DSA   DS 18F

*
* Working storage
*
FD      DS  F

RECSIZE EQU RECEND-*
REC     DS CL80
RECEND  EQU *
RECA    DS  A
BPXWRTD DS  CL(BPXWRTL)
ALET    DS  F
RECL    DS  F
RV      DS  F
RC      DS  F
RN      DS  F

DYNL EQU *-DYNAREA
*
*
* End of working storage
*

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
