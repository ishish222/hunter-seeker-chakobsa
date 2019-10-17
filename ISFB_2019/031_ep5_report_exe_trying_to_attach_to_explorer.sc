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

    
    Spawn(C:\Windows\Explorer.exe)

    #RegisterReactions(0x01204d5d,attach_1,0x0)
    RegisterReactions(0x012027b1,attach_1,0x0)
    EnableReaction(attach_1)

    Continue=

attach_1:
    GetPIDByMatch(xplorer)
    Push
    SetPID

    Call(AttachScrLog)

    RegisterReactions(0x77ef0859,report_exe_031,0x0)
    EnableReaction(report_exe_031)

    TracerPrev
    TracerDebugContinueInf
    TracerNext

    Continue=

report_exe_031:
    DisableReaction(report_exe_031)

    CurrentTID
    Push
    SetPriorityHigh

    CurrentTID
    Push
    ResumeThread

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

