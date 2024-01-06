        PRINT ON,GEN,DATA
*
* 'main' program that gets the first parameter 
* from the USS command line and calls the underlying
* function, passing the parameter
*
* USS Linkage description: https://tech.mikefulton.ca/USSExecLinkage
* Picture: https://tech.mikefulton.ca/USSExecLinkagePicture 
*
MAIN    CSECT
MAIN    RMODE ANY
MAIN    AMODE 31
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

        L  R2,0(,R1)   # Argument count pointer
        L  R2,0(,R2)   # Argument count
        CHI R2,MinParm # Need 1 parameter plus program name
        BL  NeedMoreParms

        L  R3,4(,R1)   # Argument length list

        L  R9,4(,R3)   # Pointer to second parameter

        L  R8,0(,R9)  # second parameter length
        AHI R8,-1     # Subtract off NULL terminator
        ST R8,FileNameLength
        LA R10,4(,R9)  # Pointer to second parameter text
        ST R10,FileName

*       LHI R2,2
*       STH R2,FD
*       STH R8,Len
*       MVC Txt(80),0(R10)
*       LA R1,Msg
*       L  R15,=V(WRTMSG)
*       BASR R14,R15

        LA R0,0
        ST R0,Buffer

* Call RDFILE
        LA R1,RDPARMS
        L  R15,=V(RDFILE)
        BASR R14,R15
        LR   R7,R15
*
* Epilog
*
Epilog  DS 0H
        L   R13,DSA+4
        STORAGE RELEASE,LENGTH=DYNL,ADDR=(R11)
        LR  R15,R7
        RETURN (14,12),RC=(15)

NeedMoreParms DS 0F
        LA R1,NeedMoreParmsMsg
        L R15,=V(WRTMSG)
        BASR R14,R15
        LA R7,4
        B Epilog

Literals            LTORG
NeedMoreParmsMsg    DS 0F
Stdout              DC H'1'
Length              DC H'28'
Text                DC C'Need to specify a parameter'
Newline             DC X'15'
MinParm             EQU 2            
       

*
* Dynamic (storage obtain'ed) area
*
DYNAREA DSECT
DYNL    EQU DYNEND-*
*
* Stack save area always first
*
DSA     DS 18F
*
RDPARMS        DS 0H
FileNameLength DS F
FileName       DS A
Buffer         DS A

*Msg            DS 0H
*FD             DS H
*Len            DS H
*Txt            DS CL80

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
