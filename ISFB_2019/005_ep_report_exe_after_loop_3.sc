# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

Main:
    RegisterSignals(Exception)

    Call(Intro)
    QemuStart
    QemuLoad(ep)
    Call(ReconfigureDirs)

    RegisterReactions(self+0x2988,report_exe_005,0x0)
    EnableReaction(report_exe_005)

    TracerDebugContinueInf

report_exe_005:
    DisableReaction(report_exe_005)
    CurrentTID
    Push
    SetPriorityHigh

    DumpMemory
    SecureAllSections

    Call(RegisterEnableBuiltin)

    TracerStartTraceLight
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

