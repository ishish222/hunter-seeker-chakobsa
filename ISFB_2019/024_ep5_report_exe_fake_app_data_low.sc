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

    RegisterReactions(0x1201ea8,fake_install,0x0)
    EnableReaction(fake_install)
    RegisterReactions(0x0120428d,fake_app_data_low,0x0)
    EnableReaction(fake_app_data_low)
    RegisterReactions(0x012042b2,report_exe_024,0x0)
    EnableReaction(report_exe_024)

    Continue=


fake_install:
    DisableReaction(fake_install)

    RunAdminCmd(reg add HKLM\Software\Microsoft\Windows\CurrentVersion\Run /v comutext /d c:\users\john\desktop\report.exe /t REG_EXPAND_SZ)

fake_name:
    RunRoutine(0x105)
    Continue=

fake_app_data_low:
    RunRoutine(0x105)
    Continue=

report_exe_024:
    DisableReaction(report_exe_024)
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

