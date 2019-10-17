# initial record
# transfer to after the cursor check
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
    QemuLoad(ep4)

    Call(ReconfigureDirs)

    RegisterReactions(decoded_2+0x411d,ST4,0x0)
    EnableReaction(ST4)
    HostPrint(Move the cursor)
    Continue=

ST4:
    DisableReaction(ST4)
    QemuSave(ep5)
    goto(finish)

EX:
    Continue(0x80010001)=

Exception:
    Interrupt

RX:
    HostPrint(Finishing)

finish:
    QemuQuit

