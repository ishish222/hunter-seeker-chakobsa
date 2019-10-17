Version(2)

Include(scripts/common_2/initiating.sc)
Include(scripts/common_2/debugging.sc)

DeployFolders:
    GlobPattern(ORAN1701-1/*)
    HostDeployInputGlob

    Return

Main:
    RegisterSignals(Exception)

    Call(Intro)
    Call(DeployFolders)

    SetSampleFile(sample_869d.exe)

    Call(QemuStartRevert)
    Call(DebugFileLog)

    TracerRegisterBuiltin
    DisableReactions
    Call(EnableThreadTracking)

    Continue=

CreateProcessW_end:
    Call(ExtractPIDAndTID)
    Call(AttachFileLog)

    TracerRegisterReactions(self+0x55c44,A1:A2,0x0)
    TracerRegisterReactions(self+0x56015,A2,0x0)

    ResumeAll
    Continue=

A1:
    Pause
    Continue=

A2:
    Pause
    Continue=

Default:
    Continue=

EX:
    Continue(0x80010001)=

Exception:
    Interrupt

RX:
    QemuQuit

