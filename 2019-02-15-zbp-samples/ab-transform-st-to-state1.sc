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
    #Call(QemuStartRevert)

    goto(ST)

    Continue=

ST:
    QemuStart
    QemuLoad(ST)
    #Call(QemuReconnect)

    DisableReaction(ST)
    RegisterReactions(self+0x3483,STATE1,0x0)

    CurrentTID
    Push
    SetPriorityHigh

#    DumpMemory
    SecureAllSections

    Call(RegisterEnableBuiltin)

    TracerStartTrace
    TracerDebugContinueInf

    Continue=

STATE1:
    Continue=

EX:
    Continue(0x80010001)=

Exception:
    Interrupt

RX:
    HostPrint(Finishing)

finish:
    QemuQuit

