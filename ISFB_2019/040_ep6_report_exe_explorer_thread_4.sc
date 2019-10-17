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

    Push(0x1f00000)
    SetBase(injected)
    RegisterReactions(injected+0x8a33,report_exe_040,0x0)
    EnableReaction(report_exe_040)

    Continue=

report_exe_040:
    DisableReaction(report_exe_040)

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

EX:
    Continue(0x80010001)=

Exception:
    Interrupt

RX:
    HostPrint(Finishing)
 
finish:
    QemuQuit

