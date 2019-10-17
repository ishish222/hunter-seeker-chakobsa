# what if mutex wxists
# initial record

PrintLogo
RegisterSignals(exception)
GetOptions
GetSampleOptions
SetSampleFile(22277.exe)
SetResearchDir(e:\samples\shared)
GlobPattern(/home/hs1/malware_samples/2017-08-09-august/9034/22277.exe)
SetOutDir(\\10.0.2.4\qemu)
CheckHostDir
RevertClean
EnableLogging
PrepareStats
PreparePipes
GlobMethod
DiskGlob
StartQemuFull
QemuMountDisks

start_controller:
SpawnInternalController
QemuConnectDevSocket
IsSocketConnected=(Y:success,N:fail)

fail:
Wait10
goto(start_controller)

success:
KillExplorer
ResetTracers
SpawnTracerController
SpawnTracerFileLog
TracerConfigureSample
TracerConfigureOutDir
TracerConfigureOutPrefix
TracerConfigureInDir
TracerPrepareTrace

TracerRegisterBuiltin
DisableReactions
TracerDebugSample
TracerDebugContinueInf

# RR
#ExtractEP(e:\samples\shared\22277.exe) 
#SaveEP 
#ManualSTwSelf 
TracerRegisterReactions(self+0x13ec,A1,0x0)

DisableReactions
EnableReaction(A1)
TracerDebugContinueInf
goto(decision_process_1)


Start:
    SpawnResponder(9999)
    Push2(Test)
    Push2(Test4)
    Push2(Test3)
    Push2(Test2)
    Push2(Test1)
    Push2(Test0)
    Push2(Test9)
    Push2(Test8)
    Push2(Test7)
    Push2(Test6)
    Push2(Test5)
    Push2(Test4)
    Push2(Test3)
    Push2(Test2)
    Push2(Test1)
    Push2(zAkjhsadlfkjhsaldfkhaslkdfjhlksajfhlksajdhflkasjhflkasjhdflkasjhdflkashjfdlkjasdhflkashjfdlkjashdflkashjdfkljhasdflkjhasdlfkjhasflkj)
    Push2(zaaaaaaaaaaaaaaa)
    Push2(zAkjhsadlfkjhsaldfkhaslkdfjhlksajfhlksajdhflkasjhflkasjhdflkasjhdflkashjfdlkjasdhflkashjfdlkjashdflkashjdfkljhasdflkjhasdlfkjhasflkj)
#    Push2(Taskdjfnlkasjdfnlksajfdnlkasjdnfklsajndflkasjnflkjasndfkljsnadlfkjhsadlfkjhsaldfkhaslkdfjhlksajfhlksajdhflkasjhflkasjhdflkasjhdflkashjfdlkjasdhflkashjfdlkjashdflkashjdfkljhasdflkjhasdlfkjhasflkjhasdlkfjhalskfdjhest9aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa)
#    Push2(Taskdjfnlkasjdfnlksajfdnlkasjdnfklsajndflkasjnflkjasndfkljsnadlfkjhsadlfkjhsaldfkhaslkdfjhlksajfhlksajdhflkasjhflkasjhdflkasjhdflkashjfdlkjasdhflkashjfdlkjashdflkashjdfkljhasdflkjhasdlfkjhasflkjhasdlkfjhalskfdjhest9aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa)
    Pop2
    NextResponse


    #DumpMemory
    EnableBuiltin
    ExclusiveBuiltin
    LowerBuiltin
    EnableReaction(s0)
    RaiseReaction(s0)
    RaiseReaction(s1)
    EnableReaction(SHELLEXEC+2)
    
    Execute(scripts/common/enable_context_mod_detection.sc)

    # we dont need for now, we pass by first creation
    #TracerStartTrace
    # hook proces creation
    EnableReaction(CREATEPROCESSA-)
    EnableReaction(CREATEPROCESSW-)

    TracerDebugContinueInf



decision_process_1:
Decision=(
    ST:Start,
    A1:A1,
    CREATEPROCESSA-:CREATEPROCESS,
    CREATEPROCESSW-:CREATEPROCESS,
    SETTHREADCONTEXT+:SETTHREADCONTEXT,
    RESUMETHREAD+:RESUMETHREAD,
    SHELLEXEC+2:inspect_shellexecute,
    RE:re,
    default:loop
)

CREATEPROCESS:
    ReadRegister(ESP)

    Adjust(0x2c)
    Push
    ReadDword

    Adjust(0x8)
    Push
    ReadPID

    ReadRegister(ESP)

    Adjust(0x2c)
    Push
    ReadDword

    Adjust(0xc)
    Push
    ReadTID

    EnableReaction(SETTHREADCONTEXT+)
    TracerDebugContinueInf
    goto(decision_process_1)

SETTHREADCONTEXT:
    ReadRegister(ESP)

    Adjust(0x8)
    Push
    ReadDword

    # extract EIP
    #EAX

    Adjust(0xb0)
    Push
    ReadDword

    SaveEP
    EnableReaction(RESUMETHREAD+)
    TracerDebugContinueInf
    goto(decision_process_1)

RESUMETHREAD:
    SpawnTracerLog
    TracerConfigureSamplePID
    TracerConfigureOutDir
    TracerConfigureOutPrefix
    TracerAppendPIDPrefix
    TracerConfigureInDir
    TracerPrepareTrace
    TracerRegisterBuiltin
    DisableReactions
    TracerAttachSample

    #RR
    LoadEP
    #ManualST
    TracerRegisterReactions(0x407276,ST2,0x0)

    TracerRegisterReactions(0x407132,select,0x0)

    # mutex flip
    TracerRegisterReactions(0x401634,flip1,0x105)

    # sth w GetTickCount
    TracerRegisterReactions(0x407126,flip2,0x108)

    # second HttpQueryInfo result to success
    TracerRegisterReactions(0x401e1d,flip3,0x105)

    # remainder length is 0xf?
    # TracerRegisterReactions(0x402395,flip4,0x105)

    # CryptDecrypt into success
    TracerRegisterReactions(0x402412,flip5,0x105)

    # Signature verification into success
    TracerRegisterReactions(0x402436,set_esi,0x0)

    # overwrite the remainder
    TracerRegisterReactions(0x406991,overwrite_remainder,0x0)

    # check (its 0x100)
    #TracerRegisterReactions(0x406995,read_eax,0x0)

    # interfere with verification of remainder
    TracerRegisterReactions(0x402a12,set_verification_correct,0x0)

    # interfere with verification of remainder
    TracerRegisterReactions(0x40170c,flip7,0x108)
    AutorepeatReaction(flip7)

    # interfere with verification of remainder
#    TracerRegisterReactions(0x4072a4,pause,0x0)

    ResumeThread
    # values for writing in EAX
    SetCounter(0x1)
    Enqueue(0x2)
    Enqueue(0x3)
    Enqueue(0x3)

    EnableReaction(INTERNETCONNECTW+1)
    EnableReaction(HTTPSENDREQUESTW+1)
    EnableReaction(INTERNETSETOPTION+1)
    EnableReaction(HTTPOPENREQUESTW+1)
    EnableReaction(HTTPOPENREQUESTA+1)
    EnableReaction(HTTPQUERYINFOW+1)
    EnableReaction(INTERNETREADFILE+1)
#    EnableReaction(GETLASTERROR+1)
    TracerDebugContinueInf

decision:
Decision=(
    ST2:Start2,
    select:select,
    CREATEPROCESSA-:CREATEPROCESS,
    CREATEPROCESSW-:CREATEPROCESS,
    SETTHREADCONTEXT+:SETTHREADCONTEXT,
    RESUMETHREAD+:RESUMETHREAD,
    SHELLEXEC+2:inspect_shellexecute,
    RE:re,
    INTERNETCONNECTW+1:internetconnectw,
    INTERNETCONNECTW-1:internetopen,
    HTTPSENDREQUESTW-1:read_eax,
    GETLASTERROR-1:read_eax_disable_gle,
    INTERNETSETOPTION+1:disable_ssl,
    HTTPOPENREQUESTW+1:httpopenrequestw,
    HTTPOPENREQUESTA+1:httpopenrequesta,
    HTTPQUERYINFOW+1:httpqueryinfow_3,
    HTTPQUERYINFOW-1:httpqueryinfow_4,
    INTERNETREADFILE+1:internetreadfile_1,
    INTERNETREADFILE-1:internetreadfile_2,
    update_read_1:update_read_1,
    set_esi:set_esi,
    set_eax:set_eax,
    read_ebx:read_ebx,
    read_eax:read_eax2,
    overwrite_remainder:overwrite_remainder,
    set_verification_correct:set_verification_correct,
    pause:pause,
    default:loop
)

pause:
    Pause
    TracerDebugContinueInf
    goto(decision)
    

set_verification_correct:
    Push(0x0)
    WriteRegister(EAX)
    TracerDebugContinueInf
    goto(decision)

read_eax2:
    ReadRegister(EAX)
    TracerDebugContinueInf
    goto(decision)

read_ebx:
    ReadRegister(EBX)
    TracerDebugContinueInf
    goto(decision)

set_eax:
    Push(0x1)
    WriteRegister(EAX)
    TracerDebugContinueInf
    goto(decision)
    
set_esi:
    Push(0x1)
    WriteRegister(ESI)
    TracerDebugContinueInf
    goto(decision)
    
overwrite_remainder:
    Pop2
    Push
    ReadRegister(ECX)
    WriteAnsi

    ReadRegister(ECX)
    Push
    WriteDword(0x100)

    TracerDebugContinueInf
    goto(decision)
    

select:
    CheckCounter=(Y:disable_select,N:ignore_select)
disable_select:
    DisableReaction(select)
ignore_select:
    Dequeue
    Push
    WriteRegister(EAX)
    TracerDebugContinueInf
    goto(decision)

Start2:
    DisableReaction(ST2)
    DisableReaction(select)
    DumpMemory
    SecureAllSections
    EnableBuiltin
    ExclusiveBuiltin
    LowerBuiltin
    EnableReaction(s0)
    RaiseReaction(s0)
    RaiseReaction(s1)
    EnableReaction(SHELLEXEC+2)
    
    Execute(scripts/common/enable_context_mod_detection.sc)

    TracerStartTrace
    TracerDebugContinueInf
    goto(decision)

check_size_read_3:
    ReadRegister(EBX)
check_send_req:
check_size_read_1:
check_size_read_2:
check_size_read_4:
check_buffer:
check_int_read:
    ReadRegister(EAX)
    #Pause
    TracerDebugContinueInf
    goto(decision)

A1:
    ResolveLocation(self+0xf4a4)
    Push
    ReadDword

    SaveEP
    ManualST
    EnableReaction(ST)
    TracerDebugContinueInf
    goto(decision_process_1)

read_arg_1:
    ReadStack
    ReadRegister(ESP)

    Adjust(0x0)
    Push
    ReadDword

    Push
    TracerDebugContinueInf
    goto(decision)

set_arg_1:
    WriteDword(0x109)
    TracerDebugContinueInf
    goto(decision)

read_arg_3:
    ReadStack
    ReadRegister(ESP)

    Adjust(0x8)
    Push
    ReadDword
    Push
    TracerDebugContinueInf
    goto(decision)

set_arg_3:
    Pop
    WriteDword(0x109)
    TracerDebugContinueInf
    goto(decision)

update_content_len:
    Push(0xd8)
    WriteRegister(EAX)
    TracerDebugContinueInf
    goto(decision)

read_eax_disable_gle:
    DisableReaction(GETLASTERROR+1)
    DisableReaction(GETLASTERROR-1)
    ReadRegister(EAX)
    Print
    TracerDebugContinueInf
    goto(decision)

read_eax_enable_gle:
    EnableReaction(GETLASTERROR+1)
    ReadRegister(EAX)
    Print
    TracerDebugContinueInf
    goto(decision)

read_eax:
    ReadRegister(EAX)
    Print
    TracerDebugContinueInf
    goto(decision)

disable_ssl:
    ReadRegister(ESP)

    Adjust(0x8)
    Push
    ReadDword

    CheckEqual(0x1f)=(Y:disable,N:ignore)

disable:
    ReadRegister(ESP)

    Adjust(0xc)
    Push
    ReadDword

    Push
    WriteDword(0x0)

ignore:
    TracerDebugContinueInf
    goto(decision)

httpopenrequestw:
    ReadStack
    ReadRegister(ESP)
    Adjust(0x8)
    Push
    ReadDword
    Push
    ReadUnicode
    goto(httpopenrequest_disable_ssl)

httpopenrequesta:
    ReadStack
    ReadRegister(ESP)
    Adjust(0x8)
    Push
    ReadDword
    Push
    ReadAscii
    goto(httpopenrequest_disable_ssl)

httpopenrequest_disable_ssl:
    ReadRegister(ESP)

    Adjust(0x1c)
    Push
    WriteDword(0x0)

    TracerDebugContinueInf
    goto(decision)

httpqueryinfow_3:
    ReadRegister(ESP)
    Adjust(0xc)
    Push
    TracerDebugContinueInf
    goto(decision)
    
httpqueryinfow_4:
    WriteDword(0x100)
    TracerDebugContinueInf
    goto(decision)

httpqueryinfow_1:
    ReadStack
    ReadRegister(ESP)

    Adjust(0xc)
    Push
    ReadDword

    SaveOffset

    ReadRegister(ESP)
    Adjust(0x10)
    Push
    ReadDword

    Push
    ReadDword

    SaveSize

    TracerDebugContinueInf
    goto(decision)

httpqueryinfow_2:
    OutRegion
    TracerDebugContinueInf
    goto(decision)

internetreadfile_1:
    ReadStack
    ReadRegister(ESP)
    Adjust(0x8)
    Push
    ReadDword
    SaveOffset

    ReadRegister(ESP)
    Adjust(0xc)
    Push
    ReadDword
    SaveSize

    TracerDebugContinueInf
    goto(decision)

internetreadfile_2:
    OutRegion
    TracerDebugContinueInf
    goto(decision)

update_read_1:
    ReadRegister(ECX)
    Push
    WriteDword(0x100)
    TracerDebugContinueInf
    goto(decision)
    

internetconnectw:
    ReadStack
    ReadRegister(ESP)
    Adjust(0x8)
    Push
    ReadDword
    WriteUnicode(127.0.0.1)

    ReadRegister(ESP)
    Adjust(0xc)
    #9999
    Push
    WriteDword(0x270f)
    #and now port

    # check
    ReadStack
    ReadRegister(ESP)
    Adjust(0x8)
    Push
    ReadDword
    Push
    ReadUnicode

    TracerDebugContinueInf
    goto(decision)

internetopen:
    ReadRegister(EAX)
    TracerDebugContinueInf
    goto(decision)


inspect_shellexecute:
    DumpFile(C:\ProgramData\netapi64.dll)
    Beep
    TracerDebugContinueInf
    goto(decision)

overwrite:
    ReadRegister(ESP)
    Adjust(0x4)
    WriteDword(0x1)
    TracerDebugContinueInf
    goto(decision)

overwrite2:
    ReadRegister(ESP)
    Adjust(0x0)
    WriteDword(0xa)
    TracerDebugContinueInf
    goto(decision)

loop:
    TracerDebugContinueInf
    goto(decision)

re:
    TracerDebugContinueInf(0x80010001)
    goto(decision)

exception:
    Interrupt
    RunCmdHost(mkdir -p /mnt/1/output/logs/cleopatra_3)
    RunCmd(copy e:\logs\responder_log.txt \\10.0.2.4\qemu\logs\cleopatra_3)
    RunCmd(copy e:\server\log_*.txt \\10.0.2.4\qemu\logs\cleopatra_3)
    RunCmd(copy e:\logs\init_log.txt \\10.0.2.4\qemu\logs\cleopatra_3)
    RunCmd(copy e:\logs\last_log.txt \\10.0.2.4\qemu\logs\cleopatra_3)
    QemuQuit

