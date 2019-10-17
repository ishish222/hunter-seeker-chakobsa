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
    QemuLoad(ep5)

    Call(ReconfigureDirs)

    Spawn(C:\Windows\Explorer.exe)

    RegisterReactions(decoded_2+0x27b1,attach_1,0x0)
    EnableReaction(attach_1)

    Continue=

attach_1:
    GetPIDByMatch(xplorer)
    Push
    SetPID

    Call(AttachScrLog)

    RegisterReactions(0x77ef0859,ST6,0x0)
    EnableReaction(ST6)

    TracerPrev
    TracerDebugContinueInf
    TracerNext

    Continue=

ST6:
    DisableReaction(ST6)

    CurrentTID
    Push
    SetPriorityHigh

    CurrentTID
    Push
    ResumeThread

    ReadRegister(EIP)
    
    HostPrint(Run another script to adjust injected!!!)
    QemuSave(ep6)
    goto(finish)

EX:
    Continue(0x80010001)=

Exception:
    Interrupt

RX:
    HostPrint(Finishing)

finish:
    QemuQuit

