# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

DeployFolders:
    GlobPattern(_MALWARE_SAMPLES/_WORKING/ZBP-ransomware-24.04.19/*)
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
    SetSampleFile(report.exe)

    Call(DebugFileLog)
    Call(RegisterEnableBuiltin)
    DisableReactions

    ExtractEP(c:\users\john\desktop\report.exe)
    RegisterReactions(self+0x2988,ST,0x0)

    EnableReaction(ST)

    Continue=

ST:
    DisableReaction(ST)
    QemuSave(ep2)
    goto(finish)

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

