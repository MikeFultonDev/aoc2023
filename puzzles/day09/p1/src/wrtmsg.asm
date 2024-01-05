* BPX1WRT: https://tech.mikefulton.ca/bpx1wrt
* BPX1RED: https://tech.mikefulton.ca/bpx1red
* HL/ASM 'RENT' https://tech.mikefulton.ca/asmrentvideo
* BPX Service call: https://tech.mikefulton.ca/callbpxsvc
* 
        PRINT ON,GEN,DATA
WRTMSG   CSECT
WRTMSG   RMODE ANY
WRTMSG   AMODE 31
*
* R1 points to parameter list (by value)
*  - FD (typically 1 or 2) [halfword]
*  - msglen (up to 80 bytes) [halfword]
*  - msg 
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
* Body
* Write Message to FD
*

*
* Store values into parameter list
*
        XR  R6,R6
        XR  R4,R4
        LH  R6,0(,R1)   # File Descriptor
        ST  R6,FD
        LH  R4,2(,R1)   # Message Length
        ST  R4,RECL
        LA  R3,4(,R1)   # Message Text
        ST  R3,RECA
        L   R5,BPXALET
        ST  R5,ALET

        CALL  BPX1WRT,(FD,                                             x
               RECA,                                                   x
               ALET,                                                   x
               RECL,                                                   x
               RV,                                                     x
               RC,                                                     x
               RN),MF=(E,BPXWRTD)

        L   R7,RC
*
* Epilog
*
        L   R13,DSA+4
        STORAGE RELEASE,LENGTH=DYNL,ADDR=(R11)
        LR  R15,R7
        RETURN (14,12),RC=(15)

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

VARCOPY MVC 0(0,2),0(3)
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
