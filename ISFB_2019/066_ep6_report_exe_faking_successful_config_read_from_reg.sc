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
    RegisterReactions(injected+0x216d8,report_exe_066,0x0)
    RegisterReactions(injected+0x1dec3,flip1,0x105)
    EnableReaction(report_exe_066)

    Continue=

report_exe_066:
    DisableReaction(report_exe_066)

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
EX:
    Continue(0x80010001)=

Exception:
    Interrupt

RX:
    HostPrint(Finishing)
 
finish:
    QemuQuit

