PrintLogo
RegisterSignals(exception)
GetOptions
GetSampleOptions
SetSampleFile(arab.exe)
SetResearchDir(e:\samples\shared)
GlobPattern(/home/hs1/malware_samples/arab.exe)
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
#KillExplorer
ResetTracers
SpawnTracerController
SpawnTracerFileLog
TracerConfigureSample
TracerConfigureOutDir
TracerConfigureOutPrefix
TracerConfigureInDir
TracerPrepareTrace
#TracerSetParameters(test1 test2 test3)

# configure responder
SpawnResponder(11443)
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
Enqueue(Test9)
Dequeue
NextResponse

# start debug
# czemu to dziala tylko przed DebugSample??
TracerRegisterReactions(self+0x1317,mod1:mod2,0x100;EAX+0x9ab,mod2,0x0)
TracerRegisterBuiltin
DisableReactions
TracerDebugSample
TracerDebugContinueInf

# RR
#ExtractEP(e:\samples\shared\arab.exe)
#SaveEP
#ManualSTwSelf
EnableReaction(mod1)
TracerDebugContinueInf

#sledzimy wstrzykiwanie

EnableReaction(CREATEPROCESSA+)
EnableReaction(CREATEPROCESSW+)
EnableReaction(CREATEREMOTETHREAD+)

#DumpMemory
#TracerStartTrace
TracerDebugContinueInf

### First CreateProcess

TracerDebugContinueInf
TracerDebugContinueInf

### Second CreateProcess

TracerDebugContinueInf
TracerDebugContinueInf

### Third CreateProcess

# pass function prologue
TracerDebugContinueInf

# get PID and TID
ReadRegister(ESP)
Adjust(0x2c)
ReadDword
Adjust(0x8)
ReadPID
ReadRegister(ESP)
Adjust(0x2c)
ReadDword
Adjust(0xc)
ReadTID
EnableReaction(SETTHREADCONTEXT+)
TracerDebugContinueInf

# waiting for setthread
ReadRegister(ESP)
Adjust(0x8)
ReadDword
# extract EIP
#EAX
Adjust(0xb0)
ReadDword
SaveEP
EnableReaction(RESUMETHREAD+)
TracerDebugContinueInf

# We have everything we need and attempt to resume
SpawnTracerLog
TracerConfigureSamplePID
TracerConfigureOutDir
TracerConfigurePIDPrefix
TracerConfigureInDir
TracerPrepareTrace
TracerRegisterBuiltin
DisableReactions
TracerAttachSample

# RR
#LoadEP
#ManualST

#TracerRegisterReactions(self+0x3657,ST,0x0)
#TracerRegisterReactions(self+0x3b43,ST,0x0)
#TracerRegisterReactions(self+0x3b85,ST,0x0)
#TracerRegisterReactions(self+0x49f3,ST,0x0)
#TracerRegisterReactions(self+0x35a3,flip1:flip2,0x103;self+0x117e3,flip2,0x104)
#TracerRegisterReactions(self+0xd95c,flip3,0x102)
#TracerRegisterReactions(self+0x3c01,flip4,0x102)
# selecting process for injection based on checksum
# need to control this flag in order to prevent or force injection, use with counter
#TracerRegisterReactions(self+0x4a5d,flip5,0x0:0x102)
#TracerRegisterReactions(self+0x4a7a,flip6,0x100)
TracerRegisterReactions(self+0x6953,CREATE1,0x0)
#TracerRegisterReactions(self+0x1f05,SELECTCASE,0x0)
TracerRegisterReactions(self+0x1f07,REPORTSELECTED,0x330)
TracerRegisterReactions(self+0xd869,NULLIFY_SLEEP,0x0)

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

EnableReaction(SELECTCASE)
EnableReaction(REPORTSELECTED)
EnableReaction(NULLIFY_SLEEP)
EnableReaction(INTERNETCONNECTW+1)
EnableReaction(HTTPREQUESTW+1)
EnableReaction(INTERNETSETOPTION+1)
EnableReaction(HTTPQUERYINFOW+1)
EnableReaction(INTERNETREADFILE+1)

EnableBuiltin
EnableReaction(filp1)
EnableReaction(flip3)
EnableReaction(flip4)
EnableReaction(flip6)
EnableReaction(OPENPROCESS+)
AutorepeatReaction(flip3)
AutorepeatReaction(NULLIFY_SLEEP)

TracerPrev
TracerDebugContinueInf
TracerNext
TracerDebugContinueInf

decision:
Decision=(
    ST:Start,
    RE:re,
    CREATE1:create1,
    flip5:beep,
    RX:beep,
    SELECTCASE:SelectCase,
    NULLIFY_SLEEP:nullify_sleep,
    INTERNETCONNECTW+1:overwrite_address,
    INTERNETCONNECTW-1:internetopen,
    HTTPREQUESTW-1:httpsend,
    INTERNETSETOPTION+1:disable_ssl,
    HTTPOPENREQUESTW+1:disable2,
    HTTPQUERYINFOW+1:get_info1,
    HTTPQUERYINFOW-1:get_info2,
    INTERNETREADFILE+1:get_info3,
    INTERNETREADFILE-1:get_info4,
    CREATEREMOTETHREAD+:create_2,
    CREATEREMOTETHREAD-:create_3,
    OPENPROCESS+:open_1,
    default:loop
)

Start:
    DumpMemory
    SecureAllSections
    TracerStartTrace
    EnableBuiltin
    ExclusiveBuiltin
    LowerBuiltin
    EnableReaction(s0)
    RaiseReaction(s0)
    Execute(scripts/arab/enable_context_mod_detection.sc)

    # for catching in Explorer.exe
    TracerDebugContinueInf
    goto(decision)

open_1:
    ReadRegister(ESP)
    Adjust(0xc)
    ReadPID

    TracerDebugContinueInf
    goto(decision)

create1:
    #TracerRegisterRegions([ESP+0x8]:0x3000)
    #RunRoutine(0x201)
    #TracerRegisterReactions([ESP+0x4]+0xb3,ST,0x0)
    #EnableReaction(ST)
    EnableReaction(CREATEREMOTETHREAD+)
    TracerDebugContinueInf
    goto(decision)

create_2:
    ReadRegister(ESP)
    Adjust(0x10)
    ReadDword
    SaveEP

    ReadStack(10)
    ReadRegister(ESP)
    Adjust(0x18)
    WriteDword(0x00000004)
    ReadStack(10)
    ListProcesses
    TracerDebugContinueInf
    goto(decision)

create_3:
    ReadRegister(ESP)
    Adjust(0x20)
    ReadDword
    ReadTID

    SpawnTracerLog
    TracerConfigureSamplePID
    TracerConfigureOutDir
    TracerConfigurePIDPrefix
    TracerConfigureInDir
    TracerPrepareTrace
    TracerRegisterBuiltin
    DisableReactions
    TracerAttachSample

    LoadEP
    ManualST
    EnableReaction(ST)
    SuspendAllExcept
    ResumeThread

    Beep
    TracerDebugContinueInf
    goto(decision)

beep:
    Beep
    TracerDebugContinueInf
    goto(decision)

nullify_sleep:
    Push(0x0)
    WriteRegister(EAX)
    TracerDebugContinueInf
    goto(decision)

overwrite_address:
    ReadArgUni(1)
    ReadStack
    ReadRegister(ESP)
    Adjust(0x8)
    ReadDword
    WriteUnicode(127.0.0.1)
    ReadArgUni(1)
    TracerDebugContinueInf
    goto(decision)

internetopen:
    ReadRegister(EAX)
    TracerDebugContinueInf
    goto(decision)

zero_to_1:
    # zero out timeout
    ReadStack
    ReadRegister(ESP)
    Adjust(0x8)
    WriteDword(0x0)
    TracerDebugContinueInf
    goto(decision)

zero_to_2:
    # zero out timeout
    ReadStack
    ReadRegister(ESP)
    Adjust(0x10)
    WriteDword(0x0)
    TracerDebugContinueInf
    goto(decision)

zero_eax:
    RunRoutine(0x104)
    TracerDebugContinueInf
    goto(decision)

zero_to_3:
    # zero out timeout
    ReadStack
    ReadRegister(ESP)
    Adjust(0x4)
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

SelectCase:
    Push(0x0)
    WriteRegister(EAX)
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
    Beep
    TracerDebugContinueInf(0x80010001)
    goto(decision)

exception:
    Interrupt
    RunCmdHost(mkdir -p /mnt/1/output/logs/cleopatra_1_032)
    RunCmd(copy e:\logs\responder_log.txt \\10.0.2.4\qemu\logs\cleopatra_1_032)
    RunCmd(copy e:\server\log_*.txt \\10.0.2.4\qemu\logs\cleopatra_1_032)
    RunCmd(copy e:\logs\init_log.txt \\10.0.2.4\qemu\logs\cleopatra_1_032)
    RunCmd(copy e:\logs\last_log.txt \\10.0.2.4\qemu\logs\cleopatra_1_032)
    QemuQuit

