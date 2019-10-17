# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

DeployFolders:
    GlobPattern(zbp_samples/*)
    HostDeployInputGlob

    Return

Main:
    RegisterSignals(Exception)

    Call(Intro)
    Call(DeployFolders)

    Call(QemuStartRevert)

    RunCommand(copy \\10.0.2.4\qemu\input\bc.exe c:\users\john\desktop\bc.exe)
    SetInDir(c:\users\john\desktop)
    SetSampleFile(bc.exe)

    Call(DebugFileLog)
    Call(RegisterEnableBuiltin)
    DisableReactions

#    ExtractEP(\\10.0.2.4\qemu\input\bc.exe)
    ExtractEP(c:\users\john\desktop\bc.exe)
    SaveEP
    ManualSTwSelf

#    TracerDebugContinueInf
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

RX:
    HostPrint(Finishing)

finish:
    QemuQuit

