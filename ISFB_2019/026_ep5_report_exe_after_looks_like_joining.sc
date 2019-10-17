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

    ReadRegister(EIP)
    #RegisterReactions(0x12342aa,report_exe_026,0x0)
    RegisterReactions(0x12042aa,report_exe_026,0x0)
    EnableReaction(report_exe_026)

    Continue=

report_exe_026:
    DisableReaction(report_exe_026)
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

