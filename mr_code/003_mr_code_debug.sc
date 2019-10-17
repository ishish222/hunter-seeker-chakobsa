# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

DeployFolders:
    GlobPattern(_MALWARE_SAMPLES/_WORKING/mr_code/*)
    HostDeployInputGlob
    Return

Main:
    RegisterSignals(Exception)

    Call(Intro)
    Call(DeployFolders)

    Call(QemuStartRevert)
#    QemuStart
#    QemuLoad(clean)

    RunCommand(copy \\10.0.2.4\qemu\input\* c:\users\john\desktop\)
    SetInDir(c:\users\john\desktop)
    SetSampleFile(mrcode.exe)

    SpawnTracerFileLog
    TracerConfigureSample
    TracerSetParameters(aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa)
    TracerConfigureOutDir
    TracerConfigureOutPrefix
    TracerConfigureInDir
    TracerPrepareTrace
    TracerDebugSample

    RegisterReactions(self+0x1569,ST,0x0)

    Continue=

ST:
    DisableReaction(ST)
    CurrentTID
    Push
    SetPriorityHigh

    DumpMemory
    SecureAllSections

    #Call(RegisterEnableBuiltin)

    TracerStartTraceDebug
    TracerDebugContinueInf

    Continue=

Default:
    Continue=

EXCEPTION:
EX:
    Continue(0x80010001)=

Exception:
    Interrupt

RX:
    HostPrint(Finishing)

finish:
    QemuQuit

