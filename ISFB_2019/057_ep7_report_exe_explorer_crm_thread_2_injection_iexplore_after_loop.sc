# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

Main:
    RegisterSignals(Exception)

    Call(Intro)
    QemuStart
    QemuLoad(ep7)
    Call(ReconfigureDirs)

    Spawn(C:\Program Files\Internet Explorer\iexplore.exe)

    Push(0x036b0000)
    SetBase(injected)
    SecureSection(0x036b0000, 0x1000000)

    RegisterReactions(injected+0x2349,step_1,0x0)
    EnableReaction(step_1)

    Continue=

step_1:
    DisableReaction(step_1)
    RegisterReactions(injected+0x16cee,report_exe_057,0x0)
    Continue=


report_exe_057:
    DisableReaction(report_exe_057)

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

