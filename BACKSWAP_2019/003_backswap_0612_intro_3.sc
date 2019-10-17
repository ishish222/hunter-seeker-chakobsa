# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

DeployFolders:
    GlobPattern(_MALWARE_SAMPLES/_WORKING/BACKSWAP_2019/*)
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
    SetSampleFile(backswap_0612.bin.exe)

    Call(DebugFileLog)
    Call(RegisterEnableBuiltin)
    DisableReactions

#    ExtractEP(c:\users\john\desktop\backswap_0612.bin.exe)
#    SaveEP
#    ManualSTwSelf
#    RegisterReactions(0x51cc8d,ST,0x0)
    RegisterReactions(self+0x11d56d,step_1,0x0)
    Continue=

step_1:
    ReadRegister(EAX)
    Push
    SetBase(decoded)
    RegisterReactions(decoded+0x0,ST,0x0)
    Continue=

ST:
    DisableReaction(ST)
    CurrentTID
    Push
    SetPriorityHigh

    DumpMemory
    SecureAllSections

    TracerStartTraceLight
    TracerDebugContinueInf

    Continue=

Default:
    Continue=

EX:
    Continue(0x80010001)=

Exception:
    Interrupt

RX:
    HostPrint(Finishing)

finish:
    QemuQuit

