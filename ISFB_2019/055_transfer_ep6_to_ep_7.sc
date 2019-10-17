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
    QemuLoad(ep6)

    Push(0x036b0000)
    SetBase(injected)


    Call(ReconfigureDirs)

    RegisterReactions(injected+0x341ce,ST7,0x0)
    EnableReaction(ST7)
    Continue=

ST7:
    DisableReaction(ST7)
    QemuSave(ep7)
    goto(finish)

Default:
    Continue(0x80010001)=

EX:
    Continue(0x80010001)=

Exception:
    Interrupt

RX:
    HostPrint(Finishing)

finish:
    QemuQuit

