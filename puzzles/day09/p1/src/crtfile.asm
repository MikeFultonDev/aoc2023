*
* See WRTMSG for doc links
* RDFILE: Read a file into storage obtain'ed from STORAGE OBTAIN
* Input: Name of EBCDIC text file to read from
* Output: Buffer
* Returns: Buffer size on success, negative value on failure
*
        PRINT ON,GEN,DATA
RDFILE   CSECT
RDFILE   RMODE ANY
RDFILE   AMODE 31
*
* R1 points to parameter list
*  - Pointer to file name length
*  - Pointer to file name
*  - Pointer to buffer (output)
* R15 on entry contains buffer length (negative value if failed)
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

        LA    R3,STAT#LENGTH
        ST    R3,STATL
        MVC   BUFFERA(13),=CL13'usr/inv/nov.d'                         
        MVC   BUFLENA,=F'13'                                           
        XC    S_MODE,S_MODE                                            
        MVI   S_MODE2,S_IRUSR       User read/write, group read,       
        MVI   S_MODE3,S_IWUSR+S_IRGRP+S_IROTH           other read     
        XC    O_FLAGS(OPNF#LENGTH),O_FLAGS                             
        MVI   O_FLAGS4,O_CREAT+O_RDWR Create, open for read and write  
        SPACE ,                                                        
        CALL  BPX2OPN,              Open a file and get status         +
               (BUFLENA,             Input: Pathname length            +
               BUFFERA,              Input: Pathname                   +
               O_FLAGS,              Input: Access            BPXYOPNF +
               S_MODE,               Input: Mode    BPXYMODE, BPXYFTYP +
               STATL,                Input: Length of buffer needed    +
               STAT,                 Buffer, BPXYSTAT                  +
               RV,                   Return value:-1 or file descriptor+
               RC,                   Return code                       +
               RN),                  Reason code                       +
               VL,MF=(E,BPXOPND)     ---------------------------------- 
        L     R4,RV
        L     R5,RC
        L     R6,RN
        ST    0,0(,0)
        ICM   R10,B'1111',RV
        BL    OpenFail
        LR    R2,R10       # File Descriptor
        L     R3,ST_SIZE_L # low word of file size <2^32
Epilog  DS    0H
        L     R13,DSA+4
        STORAGE RELEASE,LENGTH=DYNL,ADDR=(R11)
        LR    R15,R10
        RETURN (14,12),RC=(15)
OpenFail DS 0H
         B  Epilog     # R10 has -1 (failed OPEN) for Return Value
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
BPX2OPN DC V(BPX2OPN)
BPXOPNS CALL ,(0,0,0,0,0,0,0,0,0),MF=L
BPXOPNL EQU *-BPXOPNS

*
*
* Input/Output Parms:
PARMS   DSECT
PPATHL  DS F
PPATHA  DS A
PBUFFA  DS A

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
BPXOPND DS  CL(BPXOPNL)
RV      DS  F
RC      DS  F
RN      DS  F

BUFLENA  DS F
BUFFERA  DS A
STATL    DS F

* openstat structures
O_FLAGS  BPXYOPNF DSECT=NO
STAT     BPXYSTAT DSECT=NO
S_MODE   BPXYMODE DSECT=NO
TYPE     BPXYFTYP DSECT=NO

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
