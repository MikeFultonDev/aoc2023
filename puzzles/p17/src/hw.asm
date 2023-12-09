* BPX1WRT: https://tech.mikefulton.ca/bpx1wrt
* BPX1RED: https://tech.mikefulton.ca/bpx1red
* HL/ASM 'RENT' https://tech.mikefulton.ca/asmrentvideo
        PRINT ON,GEN,DATA
HELLO   CSECT
* 
* Prolog
*
        SAVE (14,12)
        BASR R12,0
        USING *,R12
        STORAGE OBTAIN,LENGTH=DYNSIZE,ADDR=(R11)
        USING DYNAREA,R11
        LA R2,SAVEA
        ST R2,8(,R13)
        ST R13,SAVEA+4
        LR R13,R2
*
* Body
* Write Hello World to STDOUT
*
        MVC REC(11),=C'Hello World'
        MVC RECLEN,RECSIZE
        LA  R15,REC
        ST  R15,RECADDR
        MVC BPXWRTD(BPXWRTZ),BPXWRTM
        LA  R1,BPXWRTD
*       LINKX EP=BPX1WRT,PARAM=(R1),VL=1

*
* Epilog
*
        L   R13,SAVEA+4
        STORAGE RELEASE,LENGTH=DYNSIZE,ADDR=(R11)
        RETURN (14,12),RC=0
        LTORG
*
* Statics (constants)
*
STDOUT  DC F'0'
STDIN   DC F'1'
STDERR  DC F'2'
BPXALET DC F'0'
BPXWRTM LINKX SF=L
*
* Dynamic (storage obtain'ed) area
*
DYNAREA DSECT
DYNSIZE EQU DYNEND-*
*
* Stack save area always first
*
SAVEA   DS 18F
*
* Working storage
*
RECSIZE EQU RECEND-*
REC     DS CL80
RECLEN  DS F
RECEND  EQU *
RECADDR DS A
RV      DS F
RC      DS F
PLIST   DS 10A * Is this enough?
BPXWRTZ EQU BPXWRTE-*
BPXWRTD LINKX SF=L
BPXWRTE EQU *
*
*
* End of working storage
*
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
