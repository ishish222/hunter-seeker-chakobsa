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
    RegisterReactions(injected+0x8660,zero_out,0x0)
    RegisterReactions(injected+0x8685,flip1,0x105)
    RegisterReactions(injected+0x4e7d,zero1,0x102)
    RegisterReactions(injected+0x868c,report_exe_065,0x0)
    EnableReaction(report_exe_065)

    Continue=

zero_out:
    ReadStack
    ReadRegister(ESP)
    Push
    WriteDword(0x0)
    ReadStack
    Continue=

report_exe_065:
    DisableReaction(report_exe_065)

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

