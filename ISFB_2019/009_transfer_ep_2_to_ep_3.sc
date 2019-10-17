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
    QemuLoad(ep2)

    Call(ReconfigureDirs)

    RegisterReactions([self+0x716d8]+0x91e,step_1,0x0)
    EnableReaction(step_1)

    Continue=

step_1:
    DisableReaction(step_1)

#    Int16(0x4716d8)
#    Push
#    ReadDword
#    ReadRegister(EIP)
#    ReadRegister(EAX)

    RegisterReactions(EAX,ST2,0x0)
    EnableReaction(ST2)
    Continue=

ST2:
    DisableReaction(ST2)
    QemuSave(ep3)
    goto(finish)

EX:
    Continue(0x80010001)=

Exception:
    Interrupt

RX:
    HostPrint(Finishing)

finish:
    QemuQuit

