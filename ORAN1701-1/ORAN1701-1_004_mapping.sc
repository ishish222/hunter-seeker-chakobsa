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

decision:
Decision=(
    ST:Start,
    RE:re,
    RX:finish,
    default:loop
)

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

