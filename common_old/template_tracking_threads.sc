# initial record

PrintLogo
RegisterSignals(exception)
GetOptions
GetSampleOptions
SetSampleFile([x])
SetResearchDir(e:\samples\shared)
GlobPattern(/home/hs1/malware_samples/[x])
SetOutDir(\\10.0.2.4\qemu)
CheckHostDir
OfflineRevertClean
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
ExtractEP(e:\samples\shared\[x]) 
SaveEP 
ManualSTwSelf 
DisableReactions
EnableReaction(ST)
TracerDebugContinueInf
goto(decision)


Start:
    SecureAllSections
    EnableBuiltin
    ExclusiveBuiltin
    LowerBuiltin
    EnableReaction(s0)
    RaiseReaction(s0)
    RaiseReaction(s1)

    Execute(scripts/common/enable_thread_tracking.sc)

    TracerDebugContinueInf

decision:
Decision=(
    ST:Start,
    CREATEPROCESSA+:CREATEPROCESS+,
    CREATEPROCESSA-:CREATEPROCESS-,
    CREATEPROCESSW+:CREATEPROCESS+,
    CREATEPROCESSW-:CREATEPROCESS-,
    CREATEREMOTETHREAD+:CREATEREMOTETHREAD+,
    CREATEREMOTETHREAD-:CREATEREMOTETHREAD-,
    OPENPROCESS+:OPENPROCESS+,
    SETTHREADCONTEXT+:thread_distribution,
    RESUMETHREAD+:thread_distribution,
    RE:re,
    RX:finish,
    default:loop
)

CREATEPROCESS+:
    ReadStack
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
    ReadRegister(ESP)
    Adjust(0x18)
    Push
    WriteDword(0x00000004)

    ReadStack(10)

    ListProcesses
    TracerDebugContinueInf
    goto(decision)
    
CREATEREMOTETHREAD-:
    goto(attach_resume)

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
    RunCmdHost(mkdir -p /mnt/1/output/logs/cleopatra_3)
    RunCmd(copy e:\logs\responder_log.txt \\10.0.2.4\qemu\logs\cleopatra_3)
    RunCmd(copy e:\server\log_*.txt \\10.0.2.4\qemu\logs\cleopatra_3)
    RunCmd(copy e:\logs\init_log.txt \\10.0.2.4\qemu\logs\cleopatra_3)
    RunCmd(copy e:\logs\last_log.txt \\10.0.2.4\qemu\logs\cleopatra_3)
    QemuQuit

