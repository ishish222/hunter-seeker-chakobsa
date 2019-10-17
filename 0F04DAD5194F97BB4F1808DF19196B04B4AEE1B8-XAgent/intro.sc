# initial record
Version(2)
Include(scripts/common_2/initiating.sc)
Include(scripts/common_2/debugging.sc)

DeployFolders:
    GlobPattern(zbp_samples/*)
    HostDeployInputGlob

    Return

Main:
    RegisterSignals(Exception)

    Call(Intro)
    Call(DeployFolders)

    SetSampleFile(bc.exe)

    Call(QemuStartRevert)
    Call(DebugFileLog)
    Call(RegisterEnableBuiltin)
    DisableReactions

    ExtractEP(\\10.0.2.4\qemu\input\bc.exe)
    SaveEP
    ManualSTwSelf

    TracerDebugContinueInf
    Continue=

ST:
    DisableReaction(ST)

    CurrentTID
    Push
    SetPriorityHigh

    DumpMemory
    SecureAllSections

    Call(RegisterEnableBuiltin)

    TracerStartTrace
    TracerDebugContinueInf

    Continue=

EX:
    Continue(0x80010001)=

Exception:
    Interrupt

finish:
    QemuQuit

