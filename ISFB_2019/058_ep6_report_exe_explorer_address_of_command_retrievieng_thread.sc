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
    RegisterReactions(injected+0x1fa1d,report_exe_058,0x0)
    RegisterReactions(injected+0x1c08c,dump_stack,0x0)
    DisableReaction(report_exe_058)

    Continue=

dump_stack:
    ReadStack(0x10)

    Pause
    Continue=

report_exe_058:
    DisableReaction(report_exe_058)

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

