# initial record
Version(2)
Include(scripts/common_2/initiating.sc)
Include(scripts/common_2/debugging.sc)

DeployFolders:
    Return

Main:
    RegisterSignals(Exception)

    Call(Intro)
    Call(DeployFolders)

    SetInDir(C:\ProgramData\Ableton\Live 9 Trial\Program)
    SetSampleFile(Ableton Live 9 Trial.exe)

    Call(QemuStart)

    Spawn(C:\ProgramData\Ableton\Live 9 Trial\Program\Ableton Live 9 Trial.exe)
    Pause

    ListProcesses
    GetPIDByMatch(Ableton Live 9 Trial.exe)
    Push
    SetPID

    Call(AttachFileLog)

    TracerRegisterReactions(self+0xe93211,ST,0x0)
    EnableReaction(ST)

    Continue=

ST:
    CurrentTID
    Push
    SetPriorityHigh

    DumpMemory
    SecureAllSections

    Call(RegisterEnableBuiltin)

    Continue=

Default:
    Continue=

EX:
    Continue(0x80010001)=

Exception:
    Interrupt
finish:
    QemuQuit

