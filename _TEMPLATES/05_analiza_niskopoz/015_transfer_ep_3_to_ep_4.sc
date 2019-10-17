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

    QemuStart
    QemuLoad(ep3)

    Call(ReconfigureDirs)

    RegisterReactions(self+0x164e,step_2,0x0)
    EnableReaction(step_2)

    Continue=

step_2:
    DisableReaction(step_2)

    ReadRegister(ESI)
    Adjust(-0x229f)
    Push
    SetBase(decoded_2)

    RegisterReactions(decoded_2+0x229f,ST3,0x0)
    EnableReaction(ST3)
    Continue=

ST3:
    DisableReaction(ST3)
    ReadRegister(ESI)

    QemuSave(ep4)
    goto(finish)

EX:
    Continue(0x80010001)=

Exception:
    Interrupt

RX:
    HostPrint(Finishing)

finish:
    QemuQuit

