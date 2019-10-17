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
    RegisterReactions(injected+0x8a33,step_1,0x0)

    Continue=

step_1:
    DisableReaction(step_1)
    RegisterReactions(injected+0x269fe,report_exe_049,0x0)
    Continue=

report_exe_049:
    DisableReaction(report_exe_049)

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

