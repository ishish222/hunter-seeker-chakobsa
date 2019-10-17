# initial record

PrintLogo
RegisterSignals(exception)
GetOptions
GetSampleOptions
SetSampleFile(sample_869d.exe)
SetResearchDir(e:\samples\shared)
GlobPattern(/home/hs1/malware_samples/ORAN1701-1/sample_869d.exe)
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
ExtractEP(e:\samples\shared\sample_869d.exe) 
#SaveEP 
#ManualSTwSelf 
DisableReactions
#TracerRegisterReactions(self+0x55c44,A1:A2,0x0)
#TracerRegisterReactions(self+0x56015,A2,0x0)
#EnableReaction(A2)
Execute(scripts/common/enable_thread_tracking.sc)

TracerDebugContinueInf
goto(decision)

change_to_suspended:
    ReadRegister(ESP)
    Adjust(0x14)
    Push
    WriteDword(0x4)
    TracerDebugContinueInf
    goto(decision2)

set_priority:
    ReadRegister(ESP)
    Adjust(0x4)
    Push
    WriteDword(0x0)

    TracerDebugContinueInf
    goto(decision2)


A1:
    ResolveLocation(EAX+0x185)
    Push2
    TracerDebugContinueInf
    goto(decision)
 
A2:
    Pop2
    Push
    TracerRegisterReactionsAt(A3,0x0)
    EnableReaction(A3)
    TracerDebugContinueInf
    goto(decision)

A3:
    TracerRegisterReactions(EAX,ST,0x0)
    TracerDebugContinueInf
    goto(decision)

A4:
    LoadEP
    Adjust(-0x3d9d4)
    Push
    SetBase(injected)

    TracerRegisterReactions(injected+0x352aa,change_to_suspended,0x0)
    TracerRegisterReactions(injected+0x5a530,set_priority,0x0)
    TracerRegisterReactions(injected+0x54f51,ST,0x0)
    EnableReaction(ST)

    TracerDebugContinueInf
    goto(decision2)

A5:
    ReadRegister(ESP)
    Adjust(0x10)
    Push
    WriteDword(0x4)
    ReadStack
    
    TracerDebugContinueInf
    goto(decision)
    


Start:
    CurrentTID
    Push
    SetPriorityHigh

    DumpMemory
    SecureAllSections
    EnableBuiltin
    ExclusiveBuiltin
    LowerBuiltin
    EnableReaction(s0)
    RaiseReaction(s0)
    RaiseReaction(s1)

    TracerStartTrace
    TracerDebugContinueInf

decision2:
Decision=(
    CREATEPROCESSA+:thread_distribution,
    CREATEPROCESSW+:thread_distribution,
    CREATEREMOTETHREAD+:thread_distribution,
    OPENPROCESS+:OPENPROCESS+,
    SETTHREADCONTEXT+:thread_distribution,
    RESUMETHREAD+:thread_distribution,
    ST:Start,
    A1:A1,
    A2:A2,
    A3:A3,
    A4:A4,
    A5:A5,
    A6:A6,
    A7:A7,
    A8:A8,
    change_to_suspended:change_to_suspended,
    set_priority:set_priority,
    RE:re,
    RX:finish,
    default:loop
)

decision:
Decision=(
    CREATEPROCESSA+:thread_distribution,
    CREATEPROCESSW+:CREATEPROCESS+,
    CREATEPROCESSW-:CREATEPROCESS-,
    CREATEREMOTETHREAD+:CREATEREMOTETHREAD+,
    CREATEREMOTETHREAD-:CREATEREMOTETHREAD-,
    OPENPROCESS+:OPENPROCESS+,
    SETTHREADCONTEXT+:thread_distribution,
    RESUMETHREAD+:thread_distribution,
    ST:Start,
    A1:A1,
    A2:A2,
    A3:A3,
    A4:A4,
    A5:A5,
    A6:A6,
    A7:A7,
    A8:A8,
    change_to_suspended:change_to_suspended,
    set_priority:set_priority,
    RE:re,
    RX:finish,
    default:loop
)

OPENPROCESS+:
    ReadRegister(ESP)
    Adjust(0xc)
    Push
    ReadPID

    TracerDebugContinueInf
    goto(decision)

CREATEREMOTETHREAD+:
    ReadRegister(ESP)
    Adjust(0x10)
    Push
    ReadDword
    SaveEP

    ReadStack(10)

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

    LoadEP
    Push
#    ManualST
#    EnableReaction(ST)
    TracerRegisterReactionsAt(A4,0x0)

    TracerNext
    TracerNext
    TracerDebugContinueInf
    goto(decision)

CREATEREMOTETHREAD-:
    TracerNext
    TracerDebugContinueInf
    goto(decision)

CREATEPROCESS+:
    Execute(scripts/common/pause_new_process.sc)
    TracerDebugContinueInf
    goto(decision)

CREATEPROCESS-:
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

    goto(attach_resume)

attach_resume:
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

    EnableBuiltin
    ExclusiveBuiltin
    LowerBuiltin
    EnableReaction(s0)
    RaiseReaction(s0)
    RaiseReaction(s1)

    Execute(scripts/common/enable_thread_tracking.sc)

    ResumeAll
    TracerDebugContinueInf
    goto(decision)

thread_distribution:
    ReadStack
    TracerDebugContinueInf
    goto(decision)

loop:
    TracerDebugContinueInf
    goto(decision)

re:
    TracerDebugContinueInf(0x80010001)
    goto(decision)

exit:
    TracerPrev
    TracerDebugContinueInf
    goto(decision)

exception:
    Interrupt
finish:
    RunCmdHost(mkdir -p /mnt/1/output/logs/ORAN)
    RunCmd(copy e:\logs\responder_log.txt \\10.0.2.4\qemu\logs\ORAN)
    RunCmd(copy e:\server\log_*.txt \\10.0.2.4\qemu\logs\ORAN)
    RunCmd(copy e:\logs\init_log.txt \\10.0.2.4\qemu\logs\ORAN)
    RunCmd(copy e:\logs\last_log.txt \\10.0.2.4\qemu\logs\ORAN)
    QemuQuit

