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
    CREATEREMOTETHREAD+:thread_distribution,
    OPENPROCESS+:thread_distribution,
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

    ResumeThread
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

exception:
    Interrupt
finish:
    RunCmdHost(mkdir -p /mnt/1/output/logs/cleopatra_3)
    RunCmd(copy e:\logs\responder_log.txt \\10.0.2.4\qemu\logs\cleopatra_3)
    RunCmd(copy e:\server\log_*.txt \\10.0.2.4\qemu\logs\cleopatra_3)
    RunCmd(copy e:\logs\init_log.txt \\10.0.2.4\qemu\logs\cleopatra_3)
    RunCmd(copy e:\logs\last_log.txt \\10.0.2.4\qemu\logs\cleopatra_3)
    QemuQuit

