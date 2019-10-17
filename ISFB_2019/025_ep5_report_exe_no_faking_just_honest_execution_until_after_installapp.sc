# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

Main:
    RegisterSignals(Exception)

    Call(Intro)
    QemuStart
    QemuLoad(ep5)
    Call(ReconfigureDirs)

    #RegisterReactions(0x1234284,report_exe_025,0x0)
    RegisterReactions(0x1204284,report_exe_025,0x0)
    EnableReaction(report_exe_025)

    Continue=

report_exe_025:
    DisableReaction(report_exe_025)
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

