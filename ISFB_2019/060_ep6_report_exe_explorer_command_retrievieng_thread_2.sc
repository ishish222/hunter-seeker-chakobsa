# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

Main:
    RegisterSignals(Exception)

    Call(Intro)
    QemuStart
    QemuLoad(ep6)
    Call(ReconfigureDirs)

    Push(0x3670000)
    SetBase(injected)
    RegisterReactions(injected+0x20acc,report_exe_060,0x0)

    Continue=

report_exe_060:
    DisableReaction(report_exe_060)

    CurrentTID
    Push
    SetPriorityHigh

    CurrentTID
    Push
    SuspendAllExcept

    DumpMemory
    SecureAllSections

    Call(RegisterEnableBuiltin)

    TracerStartTraceLight
    Continue=

Default:
    Continue=

RE:
    Continue(0x80010001)=

Exception:
    Interrupt

RX:
    HostPrint(Finishing)
 
finish:
    QemuQuit

