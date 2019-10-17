# Initial record

PrintLogo
RegisterSignals(exception)
GetOptions
GetSampleOptions
SetSampleFile(DoubleFantasy_2A12630FF976BA0994143CA93FECD17F.exe)
SetResearchDir(e:\samples\shared)
GlobPattern(/home/hs1/malware_samples/apt_29_selected/DoubleFantasy_2A12630FF976BA0994143CA93FECD17F.exe)
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

SpawnResponder(8080)
Enqueue(Test)
Enqueue(Test4)
Enqueue(Test3)
Enqueue(Test2)
Enqueue(Test1)
Enqueue(Test0)
Enqueue(Test9)
Enqueue(Test8)
Enqueue(Test7)
Enqueue(Test6)
Enqueue(Test5)
Enqueue(Test4)
Enqueue(Test3)
Enqueue(Test2)
Enqueue(Test1)
Enqueue(Test0)
Enqueue(Taskdjfnlkasjdfnlksajfdnlkasjdnfklsajndflkasjnflkjasndfkljsnadlfkjhsadlfkjhsaldfkhaslkdfjhlksajfhlksajdhflkasjhflkasjhdflkasjhdflkashjfdlkjasdhflkashjfdlkjashdflkashjdfkljhasdflkjhasdlfkjhasflkjhasdlkfjhalskfdjhest9)
Dequeue
NextResponse

TracerRegisterBuiltin
ExtractEP(e:\samples\shared\DoubleFantasy_2A12630FF976BA0994143CA93FECD17F.exe)
SaveEP
ManualSTwSelf
DisableReactions
EnableReaction(ST)
TracerDebugSample
TracerDebugContinueInf

# RR
#TracerRegisterReactions(self+0x1867,print1,0x360)
#TracerRegisterReactions(self+0x1877,flip1,0x105)
#TracerRegisterReactions(self+0x18d8,flip2,0x105)
#TracerRegisterReactions(self+0x137a,ST,0x0)

#TracerRegisterReactions(self+0x137a,A1:ST,0x100;,ST,0x0)



    TracerRegisterReactions(
        WININET.dll+0x20494,INTERNETCONNECTW+1:INTERNETCONNECTW-1,0x0;
        WININET.dll+0x20546,INTERNETCONNECTW-1:INTERNETCONNECTW+1,0x330
    )

    TracerRegisterReactions(
        WININET.dll+0x1eef5,HTTPREQUESTW+1:HTTPREQUESTW-1,0x100;
        WININET.dll+0x1ef6c,HTTPREQUESTW-1:HTTPREQUESTW+1,0x330
    )

    TracerRegisterReactions(
        WININET.dll+0x14ea3,INTERNETSETOPTION+1:INTERNETSETOPTION-1,0x0;
        WININET.dll+0x14f6c,INTERNETSETOPTION-1:INTERNETSETOPTION+1,0x100
    )

    TracerRegisterReactions(
        WININET.dll+0x20615,HTTPOPENREQUESTW+1:HTTPOPENREQUESTW-1,0x0;
        WININET.dll+0x20845,HTTPOPENREQUESTW-1:HTTPOPENREQUESTW+1,0x100
    )

    TracerRegisterReactions(
        WININET.dll+0x22d7d,HTTPQUERYINFOW+1:HTTPQUERYINFOW-1,0x0;
        WININET.dll+0x22e74,HTTPQUERYINFOW-1:HTTPQUERYINFOW+1,0x0
    )

    TracerRegisterReactions(
        WININET.dll+0x1e2a6,INTERNETREADFILE+1:INTERNETREADFILE-1,0x0;
        WININET.dll+0x1e2e5,INTERNETREADFILE-1:INTERNETREADFILE+1,0x0
    )

    # modifications
    TracerRegisterReactions(
        SHELL32.dll+0x0141f2,SHELLEXEC+2:SHELLEXEC-1,0x0;
        SHELL32.dll+0x014276,SHELLEXEC-1:SHELLEXEC+2,0x100;
        )

DisableReactions
#EnableReaction(print1)
#EnableReaction(flip1)
#EnableReaction(flip2)
EnableReaction(ST)
EnableReaction(INTERNETCONNECTW+1)
EnableReaction(HTTPREQUESTW+1)
EnableReaction(INTERNETSETOPTION+1)
EnableReaction(HTTPQUERYINFOW+1)
EnableReaction(INTERNETREADFILE+1)


    TracerDebugContinueInf
    goto(decision)


Start:
    DumpMemory
    SecureAllSections
    EnableBuiltin
    ExclusiveBuiltin
    LowerBuiltin
    EnableReaction(s0)
    RaiseReaction(s0)
    RaiseReaction(s1)
    EnableReaction(SHELLEXEC+2)
    
Execute(scripts/arab/enable_context_mod_detection.sc)

# we dont need for now, we pass by first creation
TracerStartTrace
TracerDebugContinueInf

decision:
Decision=(
    ST:Start,
    SHELLEXEC+2:inspect_shellexecute,
    RE:re,
    INTERNETCONNECTW+1:overwrite_address,
    INTERNETCONNECTW-1:internetopen,
    HTTPREQUESTW+1:HTTPREQUESTW+1,
    HTTPREQUESTW-1:httpsend,
    INTERNETSETOPTION+1:disable_ssl,
    HTTPOPENREQUESTW+1:disable2,
    HTTPQUERYINFOW+1:get_info1,
    HTTPQUERYINFOW-1:get_info2,
    INTERNETREADFILE+1:get_info3,
    INTERNETREADFILE-1:get_info4,
    default:loop
)

# zero out security flags
HTTPREQUESTW+1:
    ReadRegister(ESP)
    Adjust(0x1c)
    WriteDword(0x0)
    TracerDebugContinueInf
    goto(decision)

httpsend:
    ReadRegister(EAX)
    Print
    TracerDebugContinueInf
    goto(decision)

disable_ssl:
    ReadRegister(ESP)
    Adjust(0x8)
    ReadDword
    CheckEqual(0x1f)=(Y:disable,N:ignore)

disable:
    ReadRegister(ESP)
    Adjust(0xc)
    ReadDword
    WriteDword(0x0)

ignore:
    TracerDebugContinueInf
    goto(decision)

disable2:
    ReadRegister(ESP)
    Adjust(0x1c)
    WriteDword(0x0)
    TracerDebugContinueInf
    goto(decision)

get_info1:
    ReadStack
    ReadRegister(ESP)
    Adjust(0xc)
    ReadDword
    SaveOffset
    ReadRegister(ESP)
    Adjust(0x10)
    ReadDword
    ReadDword
    SaveSize
    TracerDebugContinueInf
    goto(decision)

get_info2:
    OutRegion
    TracerDebugContinueInf
    goto(decision)

get_info3:
    ReadStack
    ReadRegister(ESP)
    Adjust(0x8)
    ReadDword
    SaveOffset
    ReadRegister(ESP)
    Adjust(0xc)
    ReadDword
    SaveSize
    TracerDebugContinueInf
    goto(decision)

get_info4:
    OutRegion
    TracerDebugContinueInf
    goto(decision)

overwrite_address:
    ReadStack
    ReadRegister(ESP)
    Adjust(0x8)
    ReadDword
    WriteUnicode(127.0.0.1)

    ReadRegister(ESP)
    Adjust(0xc)
    # 8080
    WriteDword(0x1f90)
    #and now port
    ReadStack

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
    RunCmdHost(mkdir -p /mnt/1/output/logs/xagent_1_006)
    RunCmd(copy e:\logs\responder_log.txt \\10.0.2.4\qemu\logs\xagent_1_006)
    RunCmd(copy e:\server\log_*.txt \\10.0.2.4\qemu\logs\xagent_1_006)
    RunCmd(copy e:\logs\init_log.txt \\10.0.2.4\qemu\logs\xagent_1_006)
    RunCmd(copy e:\logs\last_log.txt \\10.0.2.4\qemu\logs\xagent_1_006)
    QemuQuit

