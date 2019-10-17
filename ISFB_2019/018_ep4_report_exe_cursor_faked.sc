# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

Main:
    RegisterSignals(Exception)

    Call(Intro)
    QemuStart
    QemuLoad(ep4)
    Call(ReconfigureDirs)

    RegisterReactions(0x1234113,fake_cursor_1,0x105)
    #RegisterReactions(esi+0x1e57,fake_cursor_1,0x0)
    RegisterReactions(0x1234117,fake_cursor_2,0x105)
    RegisterReactions(0x123411d,report_exe_018,0x0)
    #RegisterReactions(esi+0x1e59,report_exe_018,0x0)
    EnableReaction(fake_cursor_1)
    EnableReaction(fake_cursor_2)
    EnableReaction(report_exe_018)

    Continue=

report_exe_018:
    DisableReaction(fake_cursor_1)
    DisableReaction(fake_cursor_2)
    DisableReaction(report_exe_018)
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

