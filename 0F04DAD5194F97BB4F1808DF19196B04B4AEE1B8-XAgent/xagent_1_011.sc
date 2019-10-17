# with responder - failed size check

PrintLogo
RegisterSignals(exception)
GetOptions
GetSampleOptions
SetSampleFile(netapi64.dll)
SetResearchDir(e:\samples\shared)
GlobPattern(/home/hs1/malware_samples/netapi64.dll)
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
SetSampleFile(rundll32.exe)
SetResearchDir(c:\windows\system32)
KillExplorer
ResetTracers
SpawnTracerController
SpawnTracerFileLog
TracerConfigureSample
TracerConfigureOutDir
TracerConfigureOutPrefix
TracerConfigureInDir
TracerPrepareTrace

SpawnResponder(9999)
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
DisableReactions
TracerSetParameters(e:\samples\shared\netapi64.dll,init)
TracerDebugSample
TracerDebugContinueInf

# RR
# od poczatku liba
TracerRegisterReactions(netapi64.dll+0x28f9,ST,0x0)
# niedlugo przed pobieraniem
#TracerRegisterReactions(self+28e5,ST,0x0)
EnableReaction(ST)

TracerRegisterReactions(netapi64.dll+0x28f7,flip1,0x105)
EnableReaction(flip1)


Execute(scripts/common/enable_internet_detection.sc)

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
    
    Execute(scripts/common/enable_context_mod_detection.sc)

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
    HTTPSENDREQUESTW+1:HTTPSENDREQUESTW+1,
    HTTPSENDREQUESTW-1:httpsend,
    INTERNETSETOPTION+1:disable_ssl,
    HTTPOPENREQUESTW+1:disable2,
    HTTPOPENREQUESTA+1:disable2,
    HTTPQUERYINFOW+1:get_info1,
    HTTPQUERYINFOW-1:get_info2,
    INTERNETREADFILE+1:get_info3,
    INTERNETREADFILE-1:get_info4,
    default:loop
)

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
    #9999
    WriteDword(0x270f)
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
    RunCmdHost(mkdir -p /mnt/1/output/logs/xagent_1_011)
    RunCmd(copy e:\logs\responder_log.txt \\10.0.2.4\qemu\logs\xagent_1_011)
    RunCmd(copy e:\server\log_*.txt \\10.0.2.4\qemu\logs\xagent_1_011)
    RunCmd(copy e:\logs\init_log.txt \\10.0.2.4\qemu\logs\xagent_1_011)
    RunCmd(copy e:\logs\last_log.txt \\10.0.2.4\qemu\logs\xagent_1_011)
    QemuQuit

