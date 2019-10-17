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

    ReadRegister(EIP)
    RegisterReactions(0x01204d5d,report_exe_030,0x0)
    EnableReaction(report_exe_030)

    Continue=

report_exe_030:
    ReadStack

    ReadRegister(ESP)
    Adjust(0x4)
    Push

    ReadDword
    Adjust(0xa8)
    Push
    
    ReadDword

    Pause


    DisableReaction(report_exe_030)
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

